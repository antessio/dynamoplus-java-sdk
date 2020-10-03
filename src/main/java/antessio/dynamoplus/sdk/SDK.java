package antessio.dynamoplus.sdk;


import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.json.JsonParser;
import antessio.dynamoplus.sdk.domain.document.query.Query;
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
        return post(COLLECTION_COLLECTION_NAME, collection, Collection.class);
    }


    public Either<Collection, SdkException> getCollection(String id) {
        return get(id, COLLECTION_COLLECTION_NAME, Collection.class);
    }

    public Either<PaginatedResult<Collection>, SdkException> getAllCollections(Integer limit, String startFrom) {
        return getAll("collection", Collection.class, limit, startFrom);
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

    public <T> Either<PaginatedResult<T>, SdkException> getAll(String collectionName, Integer limit, String startFrom, Class<T> cls) {
        return getAll(collectionName, cls, limit, startFrom);
    }

    public <T> Either<T, SdkException> createDocument(String collectionName, T document, Class<T> cls) {
        return post(collectionName, document, cls);
    }

    public <T> Either<T, SdkException> getDocument(String id, String collectionName, Class<T> cls) {
        return get(id, collectionName, cls);
    }

    public <T> Either<T, SdkException> updateDocument(String id, String collectionName, T document, Class<T> cls) {
        return put(id, collectionName, document, cls);

    }

    public Optional<SdkException> deleteDocument(String id, String collectionName) {
        return delete(id, collectionName);
    }


    public <T> Either<PaginatedResult<T>, SdkException> executeQuery(String collectionName, Query query, Class<T> cls, Integer limit, String startFrom) {
        return executeQuery(collectionName, query, cls, limit, startFrom);
    }

}
