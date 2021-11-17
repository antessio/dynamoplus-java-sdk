package antessio.dynamoplus.sdk.domain.system.aggregation.payload.count;

import antessio.dynamoplus.sdk.domain.system.aggregation.payload.AggregationPayloadInterface;

public class AggregationCountPayload implements AggregationPayloadInterface {
    private Integer count;

    public AggregationCountPayload() {
    }

    public AggregationCountPayload(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }
}
