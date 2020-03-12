package antessio.dynamoplus.authentication.bean;


import java.util.Collections;
import java.util.List;

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
    public List<String> getHeader() {
        return Collections.singletonList(
                String.format("Authorization: Signature keyId=\"%s\",algorithm=\"%s\",headers=\"(request-target) %s\",signature=\"%s\"",
                        keyId,
                        algorithm,
                        headersString,
                        signature)
        );
    }
}
