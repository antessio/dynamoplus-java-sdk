package antessio.dynamoplus.sdk;


import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.json.JsonParser;
import antessio.dynamoplus.sdk.domain.system.clientauthorization.ClientAuthorizationApiKey;
import antessio.dynamoplus.sdk.domain.system.clientauthorization.ClientAuthorizationHttpSignature;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.index.Index;


import java.util.*;


public class SDK extends AbstractSDK {


    protected SDK(String host, JsonParser jsonParser, SdkHttpClient sdkHttpClient) {
        super(host, jsonParser, sdkHttpClient);
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

    //================================== [system] client authorization =============================

    public Either<ClientAuthorizationApiKey, SdkException> createClientAuthorizationApiKey(ClientAuthorizationApiKey clientAuthorization) {
        return post(CLIENT_AUTHORIZATION_COLLECTION_NAME, clientAuthorization, ClientAuthorizationApiKey.class);
    }

    public Either<ClientAuthorizationHttpSignature, SdkException> createClientAuthorizationHttpSignature(ClientAuthorizationHttpSignature clientAuthorization) {
        return post(CLIENT_AUTHORIZATION_COLLECTION_NAME, clientAuthorization, ClientAuthorizationHttpSignature.class);
    }

    public Either<ClientAuthorizationApiKey, SdkException> getClientAuthorizationApiKey(String clientId) {
        return get(clientId, CLIENT_AUTHORIZATION_COLLECTION_NAME, ClientAuthorizationApiKey.class);
    }

    public Either<ClientAuthorizationHttpSignature, SdkException> getClientAuthorizationHttpSignature(String clientId) {
        return get(clientId, CLIENT_AUTHORIZATION_COLLECTION_NAME, ClientAuthorizationHttpSignature.class);
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


}
