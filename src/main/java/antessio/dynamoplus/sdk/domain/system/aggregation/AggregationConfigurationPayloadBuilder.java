package antessio.dynamoplus.sdk.domain.system.aggregation;

import java.util.Set;

public final class AggregationConfigurationPayloadBuilder {
    private Set<AggregationConfigurationTrigger> on;
    private String targetField;

    public AggregationConfigurationPayloadBuilder withOn(Set<AggregationConfigurationTrigger> on) {
        this.on = on;
        return this;
    }

    public AggregationConfigurationPayloadBuilder withTargetField(String targetField) {
        this.targetField = targetField;
        return this;
    }

    public AggregationConfigurationPayload build() {
        return new AggregationConfigurationPayload(on, targetField);
    }
}
