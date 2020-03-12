package antessio.dynamoplus.http;

import antessio.dynamoplus.authentication.provider.CredentialsProvider;

public abstract class AbstractSdkHttpClient implements SdkHttpClient {

    protected final HttpConfiguration httpConfiguration;
    protected final CredentialsProvider credentialsProvider;

    public AbstractSdkHttpClient(HttpConfiguration httpConfiguration, CredentialsProvider credentialsProvider) {
        this.httpConfiguration = httpConfiguration;
        this.credentialsProvider = credentialsProvider;
    }
}
