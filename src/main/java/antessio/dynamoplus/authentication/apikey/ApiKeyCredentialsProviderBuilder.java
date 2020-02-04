package antessio.dynamoplus.authentication.apikey;

public class ApiKeyCredentialsProviderBuilder {
    private String apiKey;

    public ApiKeyCredentialsProviderBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ApiKeyCredentialsProvider build() {
        return new ApiKeyCredentialsProvider(apiKey);
    }
}