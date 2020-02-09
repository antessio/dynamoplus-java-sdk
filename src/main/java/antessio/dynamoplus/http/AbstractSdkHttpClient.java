package antessio.dynamoplus.http;

public abstract class AbstractSdkHttpClient implements SdkHttpClient {

    private final HttpConfiguration httpConfiguration;

    public AbstractSdkHttpClient(HttpConfiguration httpConfiguration) {
        this.httpConfiguration = httpConfiguration;
    }
}
