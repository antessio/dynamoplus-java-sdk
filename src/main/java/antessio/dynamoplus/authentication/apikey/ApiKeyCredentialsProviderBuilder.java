package antessio.dynamoplus.authentication.apikey;

public class ApiKeyCredentialsProviderBuilder {
    private String apiKey;

    public ApiKeyCredentialsProviderBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ApiKeyCredentialsProvider createApiKeyCredentialsProvider() {
        return new ApiKeyCredentialsProvider(apiKey);
    }
}