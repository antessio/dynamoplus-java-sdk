package antessio.dynamoplus.sdk.domain.system.index;

import antessio.dynamoplus.sdk.domain.system.collection.Collection;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Index {

    private String name;
    private Collection collection;
    private List<String> conditions;
    private String orderingKey;
    private IndexConfiguration indexConfiguration;

    public Index() {
    }


    public Index(String name, Collection collection, List<String> conditions, String orderingKey, IndexConfiguration indexConfiguration) {
        this.name = name;
        this.collection = collection;
        this.conditions = conditions;
        this.orderingKey = orderingKey;
        this.indexConfiguration = indexConfiguration;
    }

    public Index(String name, Collection collection, List<String> conditions, IndexConfiguration indexConfiguration) {
        this.name = name;
        this.collection = collection;
        this.conditions = conditions;
        this.indexConfiguration = indexConfiguration;
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

    public IndexConfiguration getIndexConfiguration() {
        return indexConfiguration;
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
                indexConfiguration == index.indexConfiguration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, collection, conditions, orderingKey, indexConfiguration);
    }

    @Override
    public String toString() {
        return "Index{" +
                "  name='" + name + '\'' +
                ", collection=" + collection +
                ", conditions=" + conditions +
                ", orderingKey='" + orderingKey + '\'' +
                ", indexConfiguration=" + indexConfiguration +
                '}';
    }
}
