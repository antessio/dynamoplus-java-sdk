package antessio.dynamoplus.sdk;


import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.json.JsonParser;
import antessio.dynamoplus.sdk.domain.conditions.Eq;
import antessio.dynamoplus.sdk.domain.document.query.Query;
import antessio.dynamoplus.sdk.domain.system.aggregation.AggregationConfiguration;
import antessio.dynamoplus.sdk.domain.system.clientauthorization.ClientAuthorizationApiKey;
import antessio.dynamoplus.sdk.domain.system.clientauthorization.ClientAuthorizationHttpSignature;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.index.Index;

import java.util.Optional;


public final class SDKV2 extends AbstractSDK {


    protected SDKV2(String host, JsonParser jsonParser, SdkHttpClient sdkHttpClient) {
        super(host, jsonParser, sdkHttpClient);
    }


    //================================== [system] collections =============================

    public Collection createCollection(Collection collection) {
        return get(post(COLLECTION_COLLECTION_NAME, collection, Collection.class));
    }

    public Collection getCollection(String id) {
        return get(get(id, COLLECTION_COLLECTION_NAME, Collection.class));
    }

    public PaginatedResult<Collection> getAllCollections(Integer limit, String startFrom) {
        return getPaginated(getAll(COLLECTION_COLLECTION_NAME, Collection.class, limit, startFrom));
    }

    public PaginatedResult<Collection> getAllCollections() {
        return getAllCollections(null, null);
    }

    public void deleteCollection(Collection collection) {
        Optional<SdkException> maybeError = delete(collection.getName(), COLLECTION_COLLECTION_NAME);
        if (maybeError.isPresent()) {
            throw maybeError.get();
        }
    }
    //================================== [system] index =============================

    public Index createIndex(Index index) {
        return get(post(INDEX_COLLECTION_NAME, index, Index.class));
    }

    public PaginatedResult<Index> getIndexByCollectionName(String collectionName, Integer limit, String startFrom) {
        return query(INDEX_COLLECTION_NAME, new Query(new Eq("collection.name", collectionName)), Index.class, 1, startFrom);
    }

    public void deleteIndex(Index index) {
        Optional<SdkException> maybeError = delete(index.getName(), INDEX_COLLECTION_NAME);
        if (maybeError.isPresent()) {
            throw maybeError.get();
        }
    }

    //================================== [system] client authorization =============================

    public ClientAuthorizationApiKey createClientAuthorizationApiKey(ClientAuthorizationApiKey clientAuthorization) {
        return get(post(CLIENT_AUTHORIZATION_COLLECTION_NAME, clientAuthorization, ClientAuthorizationApiKey.class));
    }

    public ClientAuthorizationHttpSignature createClientAuthorizationHttpSignature(ClientAuthorizationHttpSignature clientAuthorization) {
        return get(post(CLIENT_AUTHORIZATION_COLLECTION_NAME, clientAuthorization, ClientAuthorizationHttpSignature.class));
    }

    public ClientAuthorizationApiKey getClientAuthorizationApiKey(String clientId) {
        return get(get(clientId, CLIENT_AUTHORIZATION_COLLECTION_NAME, ClientAuthorizationApiKey.class));
    }

    public ClientAuthorizationHttpSignature getClientAuthorizationHttpSignature(String clientId) {
        return get(get(clientId, CLIENT_AUTHORIZATION_COLLECTION_NAME, ClientAuthorizationHttpSignature.class));
    }

    //================================== [system] aggregation =============================

    public AggregationConfiguration createAggregation(AggregationConfiguration aggregationConfiguration) {
        return get(post(AGGREGATION_CONFIGURATION_COLLECTION_NAME, aggregationConfiguration, AggregationConfiguration.class));
    }

    public AggregationConfiguration getAggregation(String name) {
        return get(get(name, AGGREGATION_CONFIGURATION_COLLECTION_NAME, AggregationConfiguration.class));
    }

    public PaginatedResult<AggregationConfiguration> getAggregationConfigurations(String collectionName, Integer limit, String startFrom) {
        return get(getAll(collectionName + "/" + AGGREGATION_CONFIGURATION_COLLECTION_NAME, AggregationConfiguration.class, limit, startFrom));
    }

    //================================== [domain] document =============================

    public <T> PaginatedResult<T> getAll(String collectionName, Integer limit, String startFrom, Class<T> cls) {
        return getPaginated(getAll(collectionName, cls, limit, startFrom));
    }

    public <T> T createDocument(String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return get(post(collectionName, document, cls));
    }

    public <T> T getDocument(String id, String collectionName, Class<T> cls) {
        System.out.println("get document by id " + id);
        return get(get(id, collectionName, cls));
    }

    public <T> T updateDocument(String id, String collectionName, T document, Class<T> cls) {
        System.out.println("creating document " + document);
        return get(put(id, collectionName, document, cls));

    }

    public void deleteDocument(String id, String collectionName) {
        Optional<SdkException> e = delete(id, collectionName);
        if (e.isPresent()) {
            throw e.get();
        }
    }

    public <T> PaginatedResult<T> query(String collectionName, Query query, Class<T> cls, Integer limit, String startFrom) {
        return getPaginated(super.executeQuery(collectionName, query, cls, limit, startFrom));
    }


}
