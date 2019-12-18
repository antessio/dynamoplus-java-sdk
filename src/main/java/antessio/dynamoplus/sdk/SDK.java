package antessio.dynamoplus.sdk;

import antessio.dynamoplus.sdk.http.DefaultSdkHttpClient;
import antessio.dynamoplus.sdk.http.SdkHttpClient;
import antessio.dynamoplus.sdk.http.SdkHttpRequest;
import antessio.dynamoplus.sdk.http.SdkHttpResponse;
import antessio.dynamoplus.sdk.system.collection.Collection;
import antessio.dynamoplus.sdk.system.index.Index;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SDK {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final String host;
    private final String environment;
    private final SdkHttpClient sdkHttpClient;
    private final ObjectMapper objectMapper;

    public static class Builder {
        private final String host;
        private final String environment;
        private ObjectMapper objectMapper;
        private SdkHttpClient sdkHttpClient;

        public Builder(String host, String environment) {
            this.host = host;
            this.environment = environment;
        }

        public Builder withObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }


        public Builder withSdkHttpClient(SdkHttpClient sdkHttpClient) {
            this.sdkHttpClient = sdkHttpClient;
            return this;
        }

        public SDK build() {
            ObjectMapper om = Optional.ofNullable(objectMapper).orElseGet(defaultObjectMapper());
            SdkHttpClient sdkHttpClient = Optional.ofNullable(this.sdkHttpClient)
                    .orElseGet(DefaultSdkHttpClient::new);
            return new SDK(host, environment, om, sdkHttpClient);
        }


        private Supplier<ObjectMapper> defaultObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return () -> objectMapper;
        }


    }

    private SDK(String host, String environment, ObjectMapper objectMapper, SdkHttpClient sdkHttpClient) {
        this.host = host;
        this.environment = environment;
        this.sdkHttpClient = sdkHttpClient;
        this.objectMapper = objectMapper;

    }


    //================================== [system] collections =============================

    public Either<Collection, SdkException> createCollection(Collection collection) {
        System.out.println("creating collection " + collection.getName());
        return post(collection.getName(), collection, Collection.class);
    }


    public Either<Collection, SdkException> getCollection(String id) {
        System.out.println("get collection " + id);
        return get(id, "collection", Collection.class);
    }

    public Either<PaginatedResult<Collection>, SdkException> getAllCollections(Integer limit, String startFrom) {
        Query<Collection> query = new QueryBuilder<Collection>()
                .limit(limit)
                .startFrom(startFrom)
                .build();
        return query("collection", null, query, Collection.class);
    }

    public Either<PaginatedResult<Collection>, SdkException> getAllCollections() {
        return getAllCollections(null, null);
    }
    //================================== [system] index =============================

    public Either<Index, SdkException> createIndex(Index index) {
        System.out.println("creating index " + index);
        return post("index", index, Index.class);
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
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), Collections.singletonList("Content-Type:application/json"), null);
            return getResponseBody(this.sdkHttpClient.get(request), cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private <T> Either<T, SdkException> post(String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.objectMapper.writeValueAsString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName), Collections.singletonList("Content-Type:application/json"), requestBody);
            return getResponseBody(this.sdkHttpClient.post(request), cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    private <T> Either<PaginatedResult<T>, SdkException> query(String collectionName, String queryName, Query<T> body, Class<T> cls) {

        try {
            String requestBody = this.objectMapper.writeValueAsString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, "query", queryName), Collections.singletonList("Content-Type:application/json"), requestBody);
            return getResponseBodyPaginated(this.sdkHttpClient.post(request), responseBody -> paginatedResultConverter(responseBody, cls));
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    private <T> Either<PaginatedResult<T>, SdkException> paginatedResultConverter(String responseBody, Class<T> cls) {
        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(PaginatedResult.class, cls);

            return Either.ok(objectMapper.readValue(responseBody, type));
        } catch (JsonProcessingException e) {
            return Either.error(new SdkPayloadException("unable to de-serialize the response", e));
        }
    }

    private <T> Either<T, SdkException> put(String id, String collectionName, T body, Class<T> cls) {
        try {
            String requestBody = this.objectMapper.writeValueAsString(body);
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), Collections.singletonList("Content-Type:application/json"), requestBody);
            return getResponseBody(this.sdkHttpClient.put(request), cls);
        } catch (Exception e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private Optional<SdkException> delete(String id, String collectionName) {
        try {
            SdkHttpRequest request = new SdkHttpRequest(getBaseUrl(), buildUrl(collectionName, id), Collections.singletonList("Content-Type:application/json"), null);
            SdkHttpResponse response = this.sdkHttpClient.delete(request);
            if (!isSuccessfull(response.getHttpStatusCode())) {
                return Optional.of(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.of(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private <T> Either<T, SdkException> getResponseBody(SdkHttpResponse<T> response, Class<T> cls) {

        try {
            if (isSuccessfull(response.getHttpStatusCode())) {
                return Either.ok(objectMapper.readValue(response.getResponseBody(), cls));
            } else {
                return Either.error(new SdkHttpException(response.getHttpStatusCode(), response.getResponseBody()));
            }
        } catch (IOException e) {
            return Either.error(new SdkPayloadException("unable to deserialize response", e));
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
        return host + "/" + environment + "/dynamoplus/";
    }


}
