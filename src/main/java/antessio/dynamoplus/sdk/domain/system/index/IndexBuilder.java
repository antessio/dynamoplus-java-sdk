package antessio.dynamoplus.sdk.domain.system.index;

import antessio.dynamoplus.sdk.domain.system.collection.Collection;
import antessio.dynamoplus.sdk.domain.system.collection.CollectionBuilder;

import java.util.List;

public class IndexBuilder {
    private String name;
    private Collection collection;
    private List<String> conditions;
    private String orderingKey;
    private IndexConfiguration configuration;

    public IndexBuilder collection(String collectionName) {
        this.collection = new CollectionBuilder()
                .name(collectionName)
                .createCollection();
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

    public IndexBuilder configuration(IndexConfiguration indexConfiguration) {
        this.configuration = indexConfiguration;
        return this;
    }

    public Index build() {
        return new Index(name, collection, conditions, orderingKey, configuration);
    }
}
