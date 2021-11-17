package antessio.dynamoplus.sdk.domain.system.aggregation;

import antessio.dynamoplus.sdk.domain.system.aggregation.payload.AggregationPayloadInterface;

public class Aggregation {
    private String name;
    private AggregationConfigurationType type;
    private AggregationPayloadInterface payload;

    public Aggregation() {
    }

    public Aggregation(String name, AggregationConfigurationType type, AggregationPayloadInterface payload) {
        this.name = name;
        this.type = type;
        this.payload = payload;
    }

    public String getName() {
        return this.name;
    }


    public AggregationConfigurationType getType() {
        return this.type;
    }


    public AggregationPayloadInterface getPayload() {
        return payload;
    }
}
