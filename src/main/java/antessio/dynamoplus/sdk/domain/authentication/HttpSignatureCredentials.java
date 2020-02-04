package antessio.dynamoplus.sdk.domain.authentication;


public class HttpSignatureCredentials implements Credentials {

    private final String signature;
    private final String keyId;
    private final String algorithm;
    private final String headersString;

    public HttpSignatureCredentials(String keyId, String algorithm, String headersString, String signature) {
        this.keyId = keyId;
        this.algorithm = algorithm;
        this.headersString = headersString;
        this.signature = signature;
    }

    @Override
    public String getHeader() {
        return String.format("Authorization: Signature keyId=\"%s\",algorithm=\"%s\",headers=\"%s\",signature=\"%s\"",
                keyId,
                algorithm,
                headersString,
                signature);
    }
}
