package antessio.dynamoplus.sdk;

import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.http.SdkHttpRequestBuilder;
import antessio.dynamoplus.http.SdkHttpResponse;
import antessio.dynamoplus.json.JsonParser;
import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.domain.document.query.Query;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractSDK {
    public static final String COLLECTION_COLLECTION_NAME = "collection";
    public static final String INDEX_COLLECTION_NAME = "index";
    public static final String CLIENT_AUTHORIZATION_COLLECTION_NAME = "client_authorization";
    public static final String AGGREGATION_CONFIGURATION_COLLECTION_NAME = "aggregation_configuration";

    protected final String host;
    protected final SdkHttpClient sdkHttpClient;
    protected final JsonParser jsonParser;

    protected AbstractSDK(String host,
                          JsonParser jsonParser,
                          SdkHttpClient sdkHttpClient) {
        this.host = host;
        this.sdkHttpClient = sdkHttpClient;
        this.jsonParser = jsonParser;
    }

    protected <T> Either<T, SdkException> get(String id, String collectionName, Class<T> cls) {
        try {
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), SdkHttpRequest.HttpMethod.GET, getRequestHeaders(), null);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }


    protected <T> Either<PaginatedResult<T>, SdkException> getAll(String collectionName, Class<T> cls, Integer limit, String startFrom) {
        try {
            //SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName), SdkHttpRequest.HttpMethod.GET, getRequestHeaders(), null);
            SdkHttpRequest request = SdkHttpRequestBuilder.aSdkHttpRequest()
                    .withBaseUrl(getBaseUrl())
                    .withPath(buildUrl(collectionName))
                    .withMethod(SdkHttpRequest.HttpMethod.GET)
                    .withHeaders(getRequestHeaders())
                    .withBody(null)
                    .withQueryParameters(getPaginationQueryParameters(limit, startFrom))
                    .build();
            return getResponseBodyPaginated(this.sdkHttpClient.execute(request), responseBody -> paginatedResultConverter(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private Map<String, String> getPaginationQueryParameters(Integer limit, String startFrom) {
        Map<String, String> queryParameters = new HashMap<>();
        Optional.ofNullable(limit)
                .map(Object::toString)
                .ifPresent(l -> queryParameters.put("limit", l));
        Optional.ofNullable(startFrom)
                .ifPresent(s -> queryParameters.put("start_from", s));
        return queryParameters;
    }

    protected <T> Either<T, SdkException> post(String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.jsonParser.objectToJsonString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName), SdkHttpRequest.HttpMethod.POST, getRequestHeaders(), requestBody);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    protected <T> Either<T, SdkException> put(String id, String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.jsonParser.objectToJsonString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), SdkHttpRequest.HttpMethod.PUT, getRequestHeaders(), requestBody);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    protected Optional<SdkException> delete(String id, String collectionName) {
        try {
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), SdkHttpRequest.HttpMethod.DELETE, getRequestHeaders(), null);
            SdkHttpResponse response = this.sdkHttpClient.execute(request);
            if (!isSuccessfull(response.getHttpStatusCode())) {
                return Optional.of(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.of(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    protected <T> Either<PaginatedResult<T>, SdkException> executeQuery(
            String collectionName,
            Query query,
            Class<T> cls,
            Integer limit,
            String startFrom) {

        try {
            String requestBody = this.jsonParser.objectToJsonString(query);
            SdkHttpRequest request = SdkHttpRequestBuilder.aSdkHttpRequest()
                    .withBaseUrl(getBaseUrl())
                    .withPath(buildUrl(collectionName, "query")).withMethod(SdkHttpRequest.HttpMethod.POST).withHeaders(getRequestHeaders())
                    .withBody(requestBody)
                    .withQueryParameters(getPaginationQueryParameters(limit, startFrom))
                    .build();
            return getResponseBodyPaginated(this.sdkHttpClient.execute(request), responseBody -> paginatedResultConverter(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    protected List<String> getRequestHeaders() {
        return Collections.singletonList("Content-Type:application/json");
    }

    protected <T> Either<PaginatedResult<T>, SdkException> paginatedResultConverter(String responseBody, Class<T> cls) {
        try {

            return Either.ok(this.jsonParser.jsonStringToPaginatedResult(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to de-serialize the response", e));
        }
    }

    protected <T> List<T> addAll(java.util.Collection<T> collection, List<T> elems) {
        List<T> list = new ArrayList<>(collection);
        list.addAll(elems);
        return list;
    }

    protected <T> Either<T, SdkException> getResponseBody(SdkHttpRequest request, Class<T> cls) {
        try {

            SdkHttpResponse response = this.sdkHttpClient.execute(request);
            if (isSuccessfull(response.getHttpStatusCode())) {
                return Either.ok(jsonParser.jsonStringToObject(response.getResponseBody(), cls));
            } else {
                return Either.error(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
            }
        } catch (JsonParsingException e) {
            return Either.error(new SdkPayloadException("unable to deserialize response", e));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to call the server", e));
        }
    }


    protected <T> Either<T, SdkException> getResponseBodyPaginated(SdkHttpResponse<T> response, Function<String, Either<T, SdkException>> converter) {
        if (isSuccessfull(response.getHttpStatusCode())) {
            return converter.apply(response.getResponseBody());
        } else {
            return Either.error(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
        }
    }

    protected boolean isSuccessfull(int httpStatusCode) {
        return httpStatusCode >= 200 && httpStatusCode < 300;
    }


    protected String buildUrl(String... pathParameters) {
        return Stream.of(pathParameters)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("/"));
    }

    protected String getBaseUrl() {
        StringBuilder sb = new StringBuilder(host);
        sb.append("/dynamoplus");
        return sb.toString();
    }

    public <T> T get(Either<T, SdkException> either) {
        return either.ok()
                .orElseThrow(() -> either.error().orElseThrow(() -> new IllegalStateException("unexpected error")));
    }

    public <T> PaginatedResult<T> getPaginated(Either<PaginatedResult<T>, SdkException> either) {
        return either.ok()
                .orElseThrow(() -> either.error().orElseThrow(() -> new IllegalStateException("unexpected error")));
    }
}
