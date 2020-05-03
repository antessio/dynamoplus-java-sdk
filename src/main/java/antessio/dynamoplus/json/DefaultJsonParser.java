package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import antessio.dynamoplus.sdk.domain.conditions.*;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultJsonParser implements JsonParser {

    private final Gson gson;

    public DefaultJsonParser() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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
                    /*if (root.has("type")) {
                        ConditionTypeEnum type = context.deserialize(root.get("type"), ConditionTypeEnum.class);
                        switch (type) {
                            case EQ:
                                return new Eq(root.get("field_name").getAsString(), root.get("value").getAsString());
                            case AND:

                            case RANGE:
                                return new Range(root.get("field_name").getAsString(), root.get("from").getAsString(), root.get("to").getAsString());
                        }
                    }*/

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
//                .registerTypeAdapter(PaginatedResult.class, new JsonDeserializer<PaginatedResult>() {
//                    @Override
//                    public PaginatedResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                        JsonObject root = jsonElement.getAsJsonObject();
//                        List data = jsonDeserializationContext.deserialize(root.getAsJsonArray("data"), List.class);
//                        String lastKey = null;
//                        if (root.has("last_key")) {
//                            //lastKey = Optional.ofNullable(root.get("last_key")).map(JsonElement::getAsString).filter(s -> !s.equals("null")).orElse(null);
//                            System.out.println("root = " + root);
//                        }
//                        return new PaginatedResult(data, lastKey);
//                    }
//                })
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
