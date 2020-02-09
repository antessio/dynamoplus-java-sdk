package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultJsonParser implements JsonParser {

    private final Gson gson;

    public DefaultJsonParser() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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
            String lastKey = null;
            if (root.has("last_key")) {
                lastKey = Optional.ofNullable(root.get("last_key"))
                        .filter(jsonElement -> !jsonElement.equals(JsonNull.INSTANCE))
                        .map(JsonElement::getAsString)
                        .orElse(null);
            }
            return new PaginatedResult<T>(data, lastKey);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }
}
