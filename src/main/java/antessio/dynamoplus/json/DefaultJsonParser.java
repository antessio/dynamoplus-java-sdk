package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import antessio.dynamoplus.sdk.domain.conditions.*;
import antessio.dynamoplus.sdk.domain.system.aggregation.AggregationConfigurationType;
import antessio.dynamoplus.sdk.domain.system.aggregation.payload.AggregationPayloadInterface;
import antessio.dynamoplus.sdk.domain.system.aggregation.payload.avg.AggregationAvgPayload;
import antessio.dynamoplus.sdk.domain.system.aggregation.payload.count.AggregationCountPayload;
import antessio.dynamoplus.sdk.domain.system.aggregation.payload.sum.AggregationSumPayload;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DefaultJsonParser implements JsonParser {

    private final Gson gson;

    public DefaultJsonParser() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(AggregationPayloadInterface.class, (JsonDeserializer<AggregationPayloadInterface>) (json, typeOfT, context) -> {
                    JsonObject root = json.getAsJsonObject();
                    if (root.has("avg")) {
                        return new AggregationAvgPayload(root.get("avg").getAsDouble());
                    } else if (root.has("count")) {
                        return new AggregationCountPayload(root.get("count").getAsInt());
                    } else if (root.has("sum")) {
                        return new AggregationSumPayload(root.get("sum").getAsDouble());
                    }
                    return null;
                })
                .registerTypeAdapter(Predicate.class, (JsonDeserializer<Predicate>) (json, typeOfT, context) -> {
                    JsonObject root = json.getAsJsonObject();
                    if (root.has("eq")) {
                        JsonObject eq = root.getAsJsonObject("eq");
                        return new Eq(eq.get("field_name").getAsString(), eq.get("value").getAsString());
                    } else if (root.has("range")) {
                        JsonObject range = root.getAsJsonObject("range");
                        return new Range(range.get("field_name").getAsString(), range.get("from").getAsString(), range.get("to").getAsString());
                    } else if (root.has("and")) {
                        JsonArray andJsonArray = root.getAsJsonArray("and");
                        List<Predicate> predicates = new ArrayList<>();
                        andJsonArray.iterator()
                                .forEachRemaining(j -> predicates.add(context.deserialize(j, Predicate.class)));
                        return new And(predicates);
                    }
                    return null;
                })
                .registerTypeHierarchyAdapter(Predicate.class, (JsonSerializer<Predicate>) (src, typeOfSrc, context) -> {
                    JsonObject condition = new JsonObject();
                    if (src instanceof And) {
                        JsonArray andJsonArray = new JsonArray();
                        And and = (And) src;
                        for (Predicate c : and.getAnd()) {
                            andJsonArray.add(context.serialize(c));
                        }
                        condition.add("and", andJsonArray);
                    } else if (src instanceof Eq) {
                        JsonObject eqJson = new JsonObject();
                        Eq eq = (Eq) src;
                        eqJson.addProperty("field_name", eq.getFieldName());
                        eqJson.addProperty("value", eq.getValue());
                        condition.add("eq", eqJson);
                    } else if (src instanceof Range) {
                        Range range = (Range) src;
                        JsonObject rangeJson = new JsonObject();
                        rangeJson.addProperty("field_name", range.getFieldName());
                        rangeJson.addProperty("from", range.getFrom());
                        rangeJson.addProperty("to", range.getTo());
                        condition.add("range", rangeJson);
                    }
                    return condition;
                })
                .create();

    }

    @Override
    public <T> String objectToJsonString(T obj) throws JsonParsingException {
        try {
            return this.gson.toJson(obj);
        } catch (JsonIOException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public <T> T jsonStringToObject(String json, Class<T> cls) throws JsonParsingException {
        try {
            return this.gson.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public <T> PaginatedResult<T> jsonStringToPaginatedResult(String json, Class<T> cls) throws JsonParsingException {
        try {

            JsonObject root = new com.google.gson.JsonParser().parse(json).getAsJsonObject();

            List<T> data = new ArrayList<>();
            for (JsonElement jsonElement : root.get("data").getAsJsonArray()) {
                data.add(gson.fromJson(jsonElement, cls));
            }
            Boolean hasMore = null;
            if (root.has("has_more")) {
                hasMore = Optional.ofNullable(root.get("has_more"))
                        .filter(jsonElement -> !jsonElement.equals(JsonNull.INSTANCE))
                        .map(JsonElement::getAsBoolean)
                        .orElse(null);
            }
            return new PaginatedResult<T>(data, hasMore);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }
}
