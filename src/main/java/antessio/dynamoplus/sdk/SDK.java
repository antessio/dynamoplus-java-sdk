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
import java.util.function.Consumer;
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

    public Collection createCollection(Collection collection) {
        System.out.println("creating collection " + collection.getName());
        return post(collection.getName(), collection, Collection.class);
    }


    public Collection getCollection(String id) {
        System.out.println("get collection " + id);
        return get(id, "collection", Collection.class);
    }


    //================================== [system] index =============================

    public Index createIndex(Index index) {
        System.out.println("creating index " + index);
        return post("index", index, Index.class);
    }

    //================================== [domain] document =============================


    public <T> T createDocument(String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return post(collectionName, document, cls);
    }

    public <T> T getDocument(String id, String collectionName, Class<T> cls) {
        System.out.println("get document by id " + id);
        return get(id, collectionName, cls);
    }

    public <T> T updateDocument(String id, String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return put(id, collectionName, document, cls);

    }

    public void deleteDocument(String id, String collectionName) {
        System.out.println("remove document " + id + " in collection " + collectionName);
        delete(id, collectionName);
    }


    public <T> PaginatedResult<T> queryByIndex(String collectionName, String indexName, Query<T> query, Class<T> cls) {
        System.out.println("querying " + collectionName + " by " + indexName);
        System.out.println("query.getMatches() = " + query.getMatches());
        System.out.println("query.getLimit() = " + query.getLimit());
        System.out.println("query.getStartFrom() = " + query.getStartFrom());
        return query(collectionName, indexName, query, cls);
    }

    //================================== private =============================

    private <T> T get(String id, String collectionName, Class<T> cls) {
        Request request = createRequestBuilder(collectionName, id)
                .get()
                .build();
        try {
            Either<T, SdkException> r = getResponseBody(request, cls);
            return r.left.orElseThrow(() -> r.right.orElse(new SdkException(500, "boom")));
        } catch (IOException e) {
            throw new SdkException(e);
        }
    }

    private <T> T post(String collectionName, Object body, Class<T> cls) {
        try {
            Request request = createRequestBuilderJsonHeader(collectionName)
                    .post(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)))
                    .build();
            Either<T, SdkException> r = getResponseBody(request, cls);
            return r.left.orElseThrow(() -> r.right.orElse(new SdkException(500, "boom")));
        } catch (IOException e) {
            throw new SdkException(e);
        }
    }

    private <T> PaginatedResult<T> query(String collectionName, String queryName, Query<T> body, Class<T> cls) {
        try {
            Request.Builder requestBuilder = createRequestBuilderJsonHeader(collectionName, "query", queryName)
                    .post(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)));
            Request request = requestBuilder
                    .build();
            Either<PaginatedResult<T>, SdkException> r = getResponseBodyPaginated(request, responseBody -> {
                try {
                    JavaType type = objectMapper.getTypeFactory().constructParametricType(PaginatedResult.class, cls);

                    return objectMapper.readValue(responseBody, type);
                } catch (JsonProcessingException e) {
                    throw new SdkException(e);
                }
            });
            return r.left.orElseThrow(() -> r.right.orElse(new SdkException(500, "boom")));
        } catch (IOException e) {
            throw new SdkException(e);
        }
    }

    private <T> T put(String id, String collectionName, T body, Class<T> cls) {
        try {
            Request request = createRequestBuilderJsonHeader(collectionName, id)
                    .put(RequestBody.create(JSON, this.objectMapper.writeValueAsString(body)))
                    .build();
            Either<T, SdkException> r = getResponseBody(request, cls);
            return r.left.orElseThrow(() -> r.right.orElse(new SdkException(500, "boom")));
        } catch (IOException e) {
            throw new SdkException(e);
        }
    }

    private void delete(String id, String collectionName) {
        try {
            Request request = createRequestBuilder(collectionName, id)
                    .delete()
                    .build();
            Optional<SdkException> maybeError = getResponseBody(request, String.class).right;
            if (maybeError.isPresent()) {
                throw maybeError.get();
            }
        } catch (IOException e) {
            throw new SdkException(e);
        }
    }

    private <T> Either<T, SdkException> getResponseBody(Request request, Class<T> cls) throws IOException {
        Response response = client.newCall(request).execute();
        String responseBodyStr = response.body().string();
        System.out.println(responseBodyStr);
        return response.isSuccessful() ?
                Either.left(objectMapper.readValue(responseBodyStr, cls))
                :
                Either.right(new SdkException(response.code(), responseBodyStr));
    }


    private <T> Either<T, SdkException> getResponseBodyPaginated(Request request, Function<String, T> converter) throws IOException {
        Response response = client.newCall(request).execute();
        String responseBodyStr = response.body().string();
        System.out.println(responseBodyStr);
        return response.isSuccessful() ?
                Either.left(converter.apply(responseBodyStr))
                :
                Either.right(new SdkException(response.code(), responseBodyStr));
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


    static final class Either<L, R> {
        public static <L, R> Either<L, R> left(L value) {
            return new Either<>(Optional.of(value), Optional.empty());
        }

        public static <L, R> Either<L, R> right(R value) {
            return new Either<>(Optional.empty(), Optional.of(value));
        }

        private final Optional<L> left;
        private final Optional<R> right;

        private Either(Optional<L> l, Optional<R> r) {
            left = l;
            right = r;
        }

        public <T> T map(
                Function<? super L, ? extends T> lFunc,
                Function<? super R, ? extends T> rFunc) {
            return left.<T>map(lFunc).orElseGet(() -> right.map(rFunc).get());
        }

        public <T> Either<T, R> mapLeft(Function<? super L, ? extends T> lFunc) {
            return new Either<>(left.map(lFunc), right);
        }

        public <T> Either<L, T> mapRight(Function<? super R, ? extends T> rFunc) {
            return new Either<>(left, right.map(rFunc));
        }

        public void apply(Consumer<? super L> lFunc, Consumer<? super R> rFunc) {
            left.ifPresent(lFunc);
            right.ifPresent(rFunc);
        }
    }

}
