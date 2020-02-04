package antessio.dynamoplus.authentication.httpsignature;

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

    public HttpSignatureCredentialsProvider createHttpSignatureCredentialsProvider() {
        return new HttpSignatureCredentialsProvider(keyId, privateKey);
    }
}