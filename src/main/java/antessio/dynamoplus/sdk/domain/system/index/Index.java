package antessio.dynamoplus.sdk.domain.system.index;

import antessio.dynamoplus.sdk.domain.system.collection.Collection;

import java.util.List;
import java.util.Objects;

public class Index {

    private String name;
    private Collection collection;
    private List<String> conditions;
    private String orderingKey;
    private IndexConfiguration configuration;

    public Index() {
    }


    public Index(String name, Collection collection, List<String> conditions, String orderingKey, IndexConfiguration configuration) {
        this.name = name;
        this.collection = collection;
        this.conditions = conditions;
        this.orderingKey = orderingKey;
        this.configuration = configuration;
    }

    public Index(String name, Collection collection, List<String> conditions, IndexConfiguration configuration) {
        this.name = name;
        this.collection = collection;
        this.conditions = conditions;
        this.configuration = configuration;
    }

    public String getName() {
        return name;
    }

    public Collection getCollection() {
        return collection;
    }

    public List<String> getConditions() {
        return conditions;
    }

    public String getOrderingKey() {
        return orderingKey;
    }

    public IndexConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return Objects.equals(name, index.name) &&
                Objects.equals(collection, index.collection) &&
                Objects.equals(conditions, index.conditions) &&
                Objects.equals(orderingKey, index.orderingKey) &&
                configuration == index.configuration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collection, conditions, orderingKey, configuration);
    }

    @Override
    public String toString() {
        return "Index{" +
                "  name='" + name + '\'' +
                ", collection=" + collection +
                ", conditions=" + conditions +
                ", orderingKey='" + orderingKey + '\'' +
                ", indexConfiguration=" + configuration +
                '}';
    }
}
