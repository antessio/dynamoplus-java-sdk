package antessio.dynamoplus.sdk;

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

public class SDK {

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final String host;
    private final String environment;
    private final ObjectMapper objectMapper;

    public SDK(String host, String environment) {
        this.host = host;
        this.environment = environment;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
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
        Request request = createRequestBuilder(collectionName, id)
                .get()
                .build();
        return getResponseBody(request, cls);

    }

    private <T> Either<T, SdkException> post(String collectionName, Object body, Class<T> cls) {
        try {
            Request request = createRequestBuilderJsonHeader(collectionName)
                    .post(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)))
                    .build();
            return getResponseBody(request, cls);
        } catch (JsonProcessingException e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }
    }

    private <T> Either<PaginatedResult<T>, SdkException> query(String collectionName, String queryName, Query<T> body, Class<T> cls) {
        try {
            Request request = createRequestBuilderJsonHeader(collectionName, "query", queryName)
                    .post(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)))
                    .build();
            return getResponseBodyPaginated(request, responseBody -> paginatedResultConverter(responseBody, cls));
        } catch (JsonProcessingException e) {
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
            Request request = createRequestBuilderJsonHeader(collectionName, id)
                    .put(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)))
                    .build();
            return getResponseBody(request, cls);
        } catch (JsonProcessingException e) {
            return Either.error(new SdkPayloadException("unable to serialize the request", e));
        }

    }

    private Optional<SdkException> delete(String id, String collectionName) {
        Request request = createRequestBuilder(collectionName, id)
                .delete()
                .build();
        return getResponseBody(request, String.class).error();

    }

    private <T> Either<T, SdkException> getResponseBody(Request request, Class<T> cls) {
        try {
            Response response = client.newCall(request).execute();
            String responseBodyStr = response.body().string();
            System.out.println(responseBodyStr);
            return response.isSuccessful() ?
                    Either.ok(objectMapper.readValue(responseBodyStr, cls))
                    :
                    Either.error(new SdkHttpException(response.code(), responseBodyStr));
        } catch (IOException e) {
            return Either.error(new SdkPayloadException("unable to deserialize response", e));
        }
    }


    private <T> Either<T, SdkException> getResponseBodyPaginated(Request request, Function<String, Either<T, SdkException>> converter) {
        try {
            Response response = client.newCall(request).execute();
            String responseBodyStr = response.body().string();
            System.out.println(responseBodyStr);
            return response.isSuccessful() ?
                    converter.apply(responseBodyStr) : Either.error(new SdkHttpException(response.code(), responseBodyStr));
        } catch (IOException e) {
            return Either.error(new SdkPayloadException("unable to deserialize response", e));
        }
    }

    private Request.Builder createRequestBuilderJsonHeader(String... pathParameters) {
        StringBuilder sb = new StringBuilder(getBaseUrl());
        for (String p : pathParameters) {
            sb.append("/").append(p);
        }
        return new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url(sb.toString());
    }

    private String getBaseUrl() {
        return host + "/" + environment + "/dynamoplus";
    }


    private Request.Builder createRequestBuilder(String... pathParameters) {
        StringBuilder sb = new StringBuilder(getBaseUrl());
        for (String p : pathParameters) {
            sb.append("/").append(p);
        }
        return new Request.Builder()
                .url(sb.toString());
    }


}
