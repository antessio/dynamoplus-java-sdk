package antessio.dynamoplus.json;

import antessio.dynamoplus.json.exception.JsonParsingException;
import antessio.dynamoplus.sdk.PaginatedResult;

import java.util.List;

public interface JsonParser {
    <T> String objectToJsonString(T obj) throws JsonParsingException;

    <T> T jsonStringToObject(String json, Class<T> cls) throws JsonParsingException;

    <T> PaginatedResult<T> jsonStringToPaginatedResult(String json, Class<T> cls) throws JsonParsingException;

    <T> List<T> jsonStringToList(String json, Class<T> cls) throws JsonParsingException;
}
