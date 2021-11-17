package antessio.dynamoplus.sdk.domain.system.aggregation.payload.avg;

import antessio.dynamoplus.sdk.domain.system.aggregation.payload.AggregationPayloadInterface;

public class AggregationAvgPayload implements AggregationPayloadInterface {
    private Double avg;

    public AggregationAvgPayload() {
    }

    public AggregationAvgPayload(Double avg) {
        this.avg = avg;
    }

    public Double getAvg() {
        return avg;
    }
}
