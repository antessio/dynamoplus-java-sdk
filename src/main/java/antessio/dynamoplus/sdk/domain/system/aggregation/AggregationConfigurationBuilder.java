package antessio.dynamoplus.sdk.domain.system.aggregation;

import antessio.dynamoplus.sdk.domain.conditions.Predicate;
import antessio.dynamoplus.sdk.domain.system.collection.Collection;

public class AggregationConfigurationBuilder {
    private AggregationConfigurationType type;
    private Collection collection;
    private AggregationConfigurationPayload configuration;
    private Predicate matches;
    private AggregationConfigurationJoin join;
    private Aggregation aggregation;
    

    public AggregationConfigurationBuilder withType(AggregationConfigurationType type) {
        this.type = type;
        return this;
    }

    public AggregationConfigurationBuilder withCollection(Collection collection) {
        this.collection = collection;
        return this;
    }

    public AggregationConfigurationBuilder withConfiguration(AggregationConfigurationPayload configuration) {
        this.configuration = configuration;
        return this;
    }

    public AggregationConfigurationBuilder withMatches(Predicate matches) {
        this.matches = matches;
        return this;
    }

    public AggregationConfigurationBuilder withJoin(AggregationConfigurationJoin join) {
        this.join = join;
        return this;
    }

    public AggregationConfigurationBuilder withAggregation(Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }

    public AggregationConfiguration build() {
        return new AggregationConfiguration(type, collection, configuration, matches, join, aggregation);
    }
}
