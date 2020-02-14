package antessio.dynamoplus.authentication.provider.basic;

import antessio.dynamoplus.authentication.provider.CredentialsProvider;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.authentication.bean.BaseAuthCredentials;
import antessio.dynamoplus.authentication.bean.Credentials;

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
