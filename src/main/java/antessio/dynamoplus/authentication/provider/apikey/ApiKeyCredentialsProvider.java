package antessio.dynamoplus.authentication.provider.apikey;

import antessio.dynamoplus.authentication.provider.CredentialsProvider;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.authentication.bean.ApiKeyCredentials;
import antessio.dynamoplus.authentication.bean.Credentials;

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
