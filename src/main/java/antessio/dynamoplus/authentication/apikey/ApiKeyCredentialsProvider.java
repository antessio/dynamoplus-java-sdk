package antessio.dynamoplus.authentication.apikey;

import antessio.dynamoplus.authentication.CredentialsProvider;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.sdk.domain.authentication.ApiKeyCredentials;
import antessio.dynamoplus.sdk.domain.authentication.Credentials;

public class ApiKeyCredentialsProvider implements CredentialsProvider {

    private final String apiKey;

    public ApiKeyCredentialsProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Credentials getCredentials(SdkHttpRequest request) {
        return new ApiKeyCredentials(apiKey);
    }
}
