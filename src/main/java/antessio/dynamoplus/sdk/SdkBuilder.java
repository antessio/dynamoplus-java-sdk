package antessio.dynamoplus.sdk;

import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.json.DefaultJsonParser;
import antessio.dynamoplus.json.JsonParser;

import java.util.Optional;
import java.util.function.Supplier;

public class SdkBuilder {
    private final String host;
    private JsonParser jsonParser;
    private SdkHttpClient sdkHttpClient;

    public SdkBuilder(String host, SdkHttpClient sdkHttpClient) {
        this.host = host;
        this.sdkHttpClient = sdkHttpClient;
    }

    public SdkBuilder withJsonParser(JsonParser objectMapper) {
        this.jsonParser = objectMapper;
        return this;
    }


    public SDK buildV1() {
        JsonParser om = Optional.ofNullable(jsonParser).orElseGet(defaultObjectMapper());
        return new SDK(host, om, sdkHttpClient);
    }


    private Supplier<JsonParser> defaultObjectMapper() {
        return DefaultJsonParser::new;
    }


    public SDKV2 buildV2() {
        JsonParser om = Optional.ofNullable(jsonParser).orElseGet(defaultObjectMapper());
        return new SDKV2(host, om, sdkHttpClient);
    }
}
