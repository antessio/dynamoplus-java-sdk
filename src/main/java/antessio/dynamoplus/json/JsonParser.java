package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;

public interface JsonParser {
    <T> String objectToJsonString(T obj) throws JsonParsingException;

    <T> T jsonStringToObject(String json, Class<T> cls) throws JsonParsingException;

    <T> PaginatedResult<T> jsonStringToPaginatedResult(String json, Class<T> cls) throws JsonParsingException;

}
