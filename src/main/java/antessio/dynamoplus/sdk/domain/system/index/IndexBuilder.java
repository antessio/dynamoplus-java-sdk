package antessio.dynamoplus.sdk.domain.system.index;

import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.collection.CollectionBuilder;

import java.util.List;
import java.util.UUID;

public class IndexBuilder {
    private UUID uid;
    private String name;
    private Collection collection;
    private List<String> conditions;
    private String orderingKey;
    private IndexConfiguration indexConfiguration;

    public IndexBuilder collection(String collectionName) {
        this.collection = new CollectionBuilder()
                .name(collectionName)
                .createCollection();
        return this;
    }

    public IndexBuilder uid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public IndexBuilder name(String name) {
        this.name = name;
        return this;
    }

    public IndexBuilder collection(Collection collection) {
        this.collection = collection;
        return this;
    }

    public IndexBuilder conditions(List<String> conditions) {
        this.conditions = conditions;
        return this;
    }

    public IndexBuilder orderingKey(String orderingKey) {
        this.orderingKey = orderingKey;
        return this;
    }

    public IndexBuilder indexConfiguration(IndexConfiguration indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
        return this;
    }

    public Index build() {
        return new Index(uid, name, collection, conditions, orderingKey, indexConfiguration);
    }
}
