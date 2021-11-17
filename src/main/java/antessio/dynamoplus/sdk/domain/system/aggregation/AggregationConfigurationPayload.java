package antessio.dynamoplus.sdk.domain.system.aggregation;

import java.util.Objects;
import java.util.Set;

public class AggregationConfigurationPayload {
    private Set<AggregationConfigurationTrigger> on;
    private String targetField;

    public AggregationConfigurationPayload() {
    }

    public AggregationConfigurationPayload(Set<AggregationConfigurationTrigger> on, String targetField) {
        this.on = on;
        this.targetField = targetField;
    }

    public Set<AggregationConfigurationTrigger> getOn() {
        return on;
    }

    public String getTargetField() {
        return targetField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationConfigurationPayload that = (AggregationConfigurationPayload) o;
        return Objects.equals(on, that.on) && Objects.equals(targetField, that.targetField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(on, targetField);
    }

    @Override
    public String toString() {
        return "AggregationPayload{" +
                "on=" + on +
                ", targetField='" + targetField + '\'' +
                '}';
    }
}
