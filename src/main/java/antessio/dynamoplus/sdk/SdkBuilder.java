package antessio.dynamoplus.sdk;

import antessio.dynamoplus.authentication.provider.CredentialsProvider;
import antessio.dynamoplus.authentication.provider.apikey.ApiKeyCredentialsProviderBuilder;
import antessio.dynamoplus.authentication.provider.basic.BasicAuthCredentialsProviderBuilder;
import antessio.dynamoplus.authentication.provider.httpsignature.HttpSignatureCredentialsProviderBuilder;
import antessio.dynamoplus.http.DefaultSdkHttpClient;
import antessio.dynamoplus.http.HttpConfiguration;
import antessio.dynamoplus.http.SdkHttpClient;
import antessio.dynamoplus.json.DefaultJsonParser;
import antessio.dynamoplus.json.JsonParser;

import java.util.Optional;
import java.util.function.Supplier;

public class SdkBuilder {
    private final String host;
    private JsonParser jsonParser;
    private SdkHttpClient sdkHttpClient;
    private HttpConfiguration httpConfiguration = new HttpConfiguration();
    private CredentialsProvider credentialsProvider;


    public static class CredentialsProviderBuilder {

        public CredentialsProviderBuilder() {
        }

        public HttpSignatureCredentialsProviderBuilder withHttpSignatureCredentialsProviderBuilder() {
            return new HttpSignatureCredentialsProviderBuilder();
        }

        public BasicAuthCredentialsProviderBuilder withBasicAuthCredentialsProviderBuilder() {
            return new BasicAuthCredentialsProviderBuilder();
        }

        public ApiKeyCredentialsProviderBuilder withApiKeyCredentialsProviderBuilder() {
            return new ApiKeyCredentialsProviderBuilder();
        }

    }

    public SdkBuilder(String host) {
        this.host = host;
    }

    public SdkBuilder withJsonParser(JsonParser objectMapper) {
        this.jsonParser = objectMapper;
        return this;
    }

    public SdkBuilder withCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public SdkBuilder withHttpConfiguration(HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
        return this;
    }


    public SdkBuilder withSdkHttpClient(SdkHttpClient sdkHttpClient) {
        this.sdkHttpClient = sdkHttpClient;
        return this;
    }


    public SDK build() {
        JsonParser om = Optional.ofNullable(jsonParser).orElseGet(defaultObjectMapper());
        SdkHttpClient sdkHttpClient = Optional.ofNullable(this.sdkHttpClient)
                .orElseGet(() -> new DefaultSdkHttpClient(httpConfiguration));
        return new SDK(host, om, sdkHttpClient, credentialsProvider);
    }


    private Supplier<JsonParser> defaultObjectMapper() {
        return DefaultJsonParser::new;
    }


}
