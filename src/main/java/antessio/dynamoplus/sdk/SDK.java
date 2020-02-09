package antessio.dynamoplus.sdk;

import antessio.dynamoplus.authentication.CredentialsProvider;
import antessio.dynamoplus.http.HttpConfiguration;
import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.http.SdkHttpResponse;
import antessio.dynamoplus.json.JsonParser;
import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.domain.authentication.Credentials;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.index.Index;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class SDK {

    public static final String COLLECTION_COLLECTION_NAME = "collection";
    public static final String INDEX_COLLECTION_NAME = "index";
    private final String host;
    private final SdkHttpClient sdkHttpClient;
    private final JsonParser jsonParser;
    private final CredentialsProvider credentialsProvider;


    protected SDK(String host, JsonParser jsonParser, SdkHttpClient sdkHttpClient, CredentialsProvider credentialsProvider) {
        this.host = host;
        this.sdkHttpClient = sdkHttpClient;
        this.jsonParser = jsonParser;
        this.credentialsProvider = credentialsProvider;
    }


    //================================== [system] collections =============================

    public Either<Collection, SdkException> createCollection(Collection collection) {
        System.out.println("creating collection " + collection.getName());
        return post(COLLECTION_COLLECTION_NAME, collection, Collection.class);
    }


    public Either<Collection, SdkException> getCollection(String id) {
        System.out.println("get collection " + id);
        return get(id, COLLECTION_COLLECTION_NAME, Collection.class);
    }

    public Either<PaginatedResult<Collection>, SdkException> getAllCollections(Integer limit, String startFrom) {
        Query<Collection> query = new QueryBuilder<Collection>()
                .limit(limit)
                .startFrom(startFrom)
                .build();
        return query(COLLECTION_COLLECTION_NAME, null, query, Collection.class);
    }

    public Either<PaginatedResult<Collection>, SdkException> getAllCollections() {
        return getAllCollections(null, null);
    }
    //================================== [system] index =============================

    public Either<Index, SdkException> createIndex(Index index) {
        System.out.println("creating index " + index);
        return post(INDEX_COLLECTION_NAME, index, Index.class);
    }

    //================================== [domain] document =============================


    public <T> Either<T, SdkException> createDocument(String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return post(collectionName, document, cls);
    }

    public <T> Either<T, SdkException> getDocument(String id, String collectionName, Class<T> cls) {
        System.out.println("get document by id " + id);
        return get(id, collectionName, cls);
    }

    public <T> Either<T, SdkException> updateDocument(String id, String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return put(id, collectionName, document, cls);

    }

    public Optional<SdkException> deleteDocument(String id, String collectionName) {
        System.out.println("remove document " + id + " in collection " + collectionName);
        return delete(id, collectionName);
    }

    public <T> Either<PaginatedResult<T>, SdkException> queryByIndex(String collectionName, String indexName, Query<T> query, Class<T> cls) {
        System.out.println("querying " + collectionName + " by " + indexName);
        System.out.println("query.getMatches() = " + query.getMatches());
        System.out.println("query.getLimit() = " + query.getLimit());
        System.out.println("query.getStartFrom() = " + query.getStartFrom());
        return query(collectionName, indexName, query, cls);
    }

    public <T> Either<PaginatedResult<T>, SdkException> queryAll(String collectionName, Integer limit, String startFrom, Class<T> cls) {
        return queryByIndex(collectionName, null, new Query<>(null, limit, startFrom), cls);
    }

    //================================== private =============================

    private <T> Either<T, SdkException> get(String id, String collectionName, Class<T> cls) {
        try {
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), SdkHttpRequest.HttpMethod.GET, getRequestHeaders(), null);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private <T> Either<T, SdkException> post(String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.jsonParser.objectToJsonString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName), SdkHttpRequest.HttpMethod.POST, getRequestHeaders(), requestBody);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    private <T> Either<T, SdkException> put(String id, String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.jsonParser.objectToJsonString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), SdkHttpRequest.HttpMethod.PUT, getRequestHeaders(), requestBody);
            return getResponseBody(request, cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private Optional<SdkException> delete(String id, String collectionName) {
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

    private <T> Either<PaginatedResult<T>, SdkException> query(String collectionName, String queryName, Query<T> body, Class<T> cls) {

        try {
            String requestBody = this.jsonParser.objectToJsonString(body);
            SdkHttpRequest partialRequest = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, "query", queryName), SdkHttpRequest.HttpMethod.POST, getRequestHeaders(), requestBody);
            SdkHttpRequest request = Optional.ofNullable(credentialsProvider)
                    .map(p -> p.getCredentials(partialRequest))
                    .map(Credentials::getHeader)
                    .map(authHeader -> addAll(partialRequest.getHeaders(), authHeader))
                    .map(headers -> new SdkHttpRequest(partialRequest.getBaseUrl(), partialRequest.getPath(), partialRequest.getMethod(), headers, partialRequest.getBody()))
                    .orElse(partialRequest);

            return getResponseBodyPaginated(this.sdkHttpClient.execute(request), responseBody -> paginatedResultConverter(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    private List<String> getRequestHeaders() {
        return Collections.singletonList("Content-Type:application/json");
    }

    private <T> Either<PaginatedResult<T>, SdkException> paginatedResultConverter(String responseBody, Class<T> cls) {
        try {

            return Either.ok(this.jsonParser.jsonStringToPaginatedResult(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to de-serialize the response", e));
        }
    }

    private <T> List<T> addAll(java.util.Collection<T> collection, T elem) {
        List<T> list = new ArrayList<>(collection);
        list.add(elem);
        return list;
    }

    private <T> Either<T, SdkException> getResponseBody(SdkHttpRequest request, Class<T> cls) {
        try {
            SdkHttpRequest r = Optional.ofNullable(credentialsProvider)
                    .map(p -> p.getCredentials(request))
                    .map(Credentials::getHeader)
                    .map(authHeader -> addAll(request.getHeaders(), authHeader))
                    .map(headers -> new SdkHttpRequest(request.getBaseUrl(), request.getPath(), request.getMethod(), headers, request.getBody()))
                    .orElse(request);

            SdkHttpResponse response = this.sdkHttpClient.execute(r);
            if (isSuccessfull(response.getHttpStatusCode())) {
                return Either.ok(jsonParser.jsonStringToObject(response.getResponseBody(), cls));
            } else {
                return Either.error(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
            }
        } catch (JsonParsingException e) {
            return Either.error(new SdkPayloadException("unable to deserialize response", e));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to call the server"));
        }
    }


    private <T> Either<T, SdkException> getResponseBodyPaginated(SdkHttpResponse<T> response, Function<String, Either<T, SdkException>> converter) {
        if (isSuccessfull(response.getHttpStatusCode())) {
            return converter.apply(response.getResponseBody());
        } else {
            return Either.error(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
        }
    }

    private boolean isSuccessfull(int httpStatusCode) {
        return httpStatusCode >= 200 && httpStatusCode < 300;
    }


    private String buildUrl(String... pathParameters) {
        return Stream.of(pathParameters)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("/"));
    }

    private String getBaseUrl() {
        StringBuilder sb = new StringBuilder(host);
        sb.append("/dynamoplus");
        return sb.toString();
    }


}
