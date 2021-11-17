package antessio.dynamoplus.sdk.domain.system.aggregation;

import java.util.Objects;

public class AggregationConfigurationJoin {
    private String collectionName;
    private String usingField;

    public AggregationConfigurationJoin() {
    }

    public AggregationConfigurationJoin(String collectionName, String usingField) {
        this.collectionName = collectionName;
        this.usingField = usingField;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getUsingField() {
        return usingField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationConfigurationJoin that = (AggregationConfigurationJoin) o;
        return Objects.equals(collectionName, that.collectionName) && Objects.equals(usingField, that.usingField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionName, usingField);
    }

    @Override
    public String toString() {
        return "AggregationJoin{" +
                "collectionName='" + collectionName + '\'' +
                ", usingField='" + usingField + '\'' +
                '}';
    }
}
