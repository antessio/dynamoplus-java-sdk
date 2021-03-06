package antessio.dynamoplus.authentication.provider.httpsignature;

public class HttpSignatureCredentialsProviderBuilder {
    private String keyId;
    private String privateKey;

    public HttpSignatureCredentialsProviderBuilder withKeyId(String keyId) {
        this.keyId = keyId;
        return this;
    }

    public HttpSignatureCredentialsProviderBuilder withPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public HttpSignatureCredentialsProvider build() {
        return new HttpSignatureCredentialsProvider(keyId, privateKey);
    }
}