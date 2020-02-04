package antessio.dynamoplus.authentication;

import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.sdk.domain.authentication.Credentials;

public interface CredentialsProvider {
    Credentials getCredentials(SdkHttpRequest request);
}
