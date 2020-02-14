package antessio.dynamoplus.authentication.provider;

import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.authentication.bean.Credentials;

public interface CredentialsProvider {
    Credentials getCredentials(SdkHttpRequest request);
}
