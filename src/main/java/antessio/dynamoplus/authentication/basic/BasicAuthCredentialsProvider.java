package antessio.dynamoplus.authentication.basic;

import antessio.dynamoplus.authentication.CredentialsProvider;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.sdk.domain.authentication.BaseAuthCredentials;
import antessio.dynamoplus.sdk.domain.authentication.Credentials;

public class BasicAuthCredentialsProvider implements CredentialsProvider {

    private final String username;
    private final String password;

    public BasicAuthCredentialsProvider(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Credentials getCredentials(SdkHttpRequest request) {
        return new BaseAuthCredentials(username, password);
    }
}
