package antessio.dynamoplus.authentication.provider.apikey;

public class ApiKeyCredentialsProviderBuilder {
    private String apiKey;
    private String clientId;

    public ApiKeyCredentialsProviderBuilder withApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ApiKeyCredentialsProviderBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ApiKeyCredentialsProvider build() {
        return new ApiKeyCredentialsProvider(clientId, apiKey);
    }
}