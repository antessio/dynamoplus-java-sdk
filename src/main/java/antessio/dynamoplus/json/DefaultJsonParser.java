package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DefaultJsonParser implements JsonParser {

    private final Gson gson;

    public DefaultJsonParser() {
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
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
            Type paginatedResultType = new TypeToken<PaginatedResult<T>>() {
            }.getType();
            return this.gson.fromJson(json, paginatedResultType);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public <T> List<T> jsonStringToList(String json, Class<T> cls) throws JsonParsingException {
        try {
            Type listType = new TypeToken<List<T>>() {
            }.getType();
            return this.gson.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }
}
