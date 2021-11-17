package antessio.dynamoplus.sdk.domain.system.aggregation.payload.sum;

import antessio.dynamoplus.sdk.domain.system.aggregation.payload.AggregationPayloadInterface;

public class AggregationSumPayload implements AggregationPayloadInterface {
    private Double sum;

    public AggregationSumPayload() {
    }

    public AggregationSumPayload(Double sum) {
        this.sum = sum;
    }

    public Double getSum() {
        return sum;
    }
}
