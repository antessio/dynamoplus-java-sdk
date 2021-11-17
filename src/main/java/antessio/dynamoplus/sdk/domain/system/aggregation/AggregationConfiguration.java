package antessio.dynamoplus.sdk.domain.system.aggregation;

import antessio.dynamoplus.sdk.domain.conditions.Predicate;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;

import java.util.Objects;

public class AggregationConfiguration {
    private AggregationConfigurationType type;
    private Collection collection;
    private AggregationConfigurationPayload configuration;
    private Predicate matches;
    private AggregationConfigurationJoin join;
    private Aggregation aggregation;

    public AggregationConfiguration() {
    }

    public AggregationConfiguration(AggregationConfigurationType type, Collection collection, AggregationConfigurationPayload configuration, Predicate matches, AggregationConfigurationJoin join, Aggregation aggregation) {
        this.type = type;
        this.collection = collection;
        this.configuration = configuration;
        this.matches = matches;
        this.join = join;
        this.aggregation = aggregation;
    }

    public AggregationConfigurationType getType() {
        return type;
    }

    public Collection getCollection() {
        return collection;
    }

    public AggregationConfigurationPayload getConfiguration() {
        return configuration;
    }

    public Predicate getMatches() {
        return matches;
    }

    public AggregationConfigurationJoin getJoin() {
        return join;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    @Override
    public String toString() {
        return "AggregationConfiguration{" +
                "type=" + type +
                ", collection=" + collection +
                ", configuration=" + configuration +
                ", matches=" + matches +
                ", join=" + join +
                ", aggregation=" + aggregation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationConfiguration that = (AggregationConfiguration) o;
        return type == that.type && Objects.equals(collection, that.collection) && Objects.equals(configuration, that.configuration) && Objects.equals(matches, that.matches) && Objects.equals(join, that.join) && Objects.equals(aggregation, that.aggregation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, collection, configuration, matches, join, aggregation);
    }
}
