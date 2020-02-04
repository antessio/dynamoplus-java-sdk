package antessio.dynamoplus.authentication.httpsignature;

import antessio.dynamoplus.authentication.CredentialsProvider;
import antessio.dynamoplus.http.SdkHttpRequest;
import antessio.dynamoplus.sdk.domain.authentication.Credentials;
import antessio.dynamoplus.sdk.domain.authentication.HttpSignatureCredentials;

import java.security.KeyFactory;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


public class HttpSignatureCredentialsProvider implements CredentialsProvider {
    public static final String HEADER_FIELD_SEPARATOR = ": ";
    public static final String REQUEST_TARGET = "(request-target)";
    public static final String ALGORITHM = "rsa-sha256";

    private final String keyId;
    private final String privateKey;

    public HttpSignatureCredentialsProvider(String keyId, String privateKey) {
        this.keyId = keyId;
        this.privateKey = privateKey;
    }

    @Override
    public Credentials getCredentials(SdkHttpRequest request) {
        return new HttpSignatureCredentials(
                keyId,
                ALGORITHM,
                concatHeadersFields(request.getHeaders()),
                base64RSASha256(getSigninString(request.getMethod().name().toLowerCase(), request.getPath(), request.getHeaders().toArray(new String[0]))));
    }

    private String concatHeadersFields(List<String> headers) {
        return headers.isEmpty() ? "" : " " + headers.stream().map(String::toLowerCase).collect(Collectors.joining(" "));
    }

    private byte[] signMessage(byte[] data) {
        try {
            // Remove markers and new line characters in private key
            String realPK = privateKey.replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("\n", "");
            byte[] b1 = Base64.getDecoder().decode(realPK);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(kf.generatePrivate(spec));
            privateSignature.update(data);
            return privateSignature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getSigninString(String method, String path, String... headers) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(REQUEST_TARGET + ": ").append(method.toLowerCase()).append(" ").append(path.toLowerCase());
        for (String header : headers) {
            String headerField = header.split(HEADER_FIELD_SEPARATOR)[0];
            String headerValue = header.split(HEADER_FIELD_SEPARATOR)[1];
            stringBuilder.append("\n").append(headerField.toLowerCase()).append(HEADER_FIELD_SEPARATOR).append(headerValue);
        }
        return stringBuilder.toString();
    }

    private String base64RSASha256(String signinSignature) {
        return new String(Base64.getEncoder().encode(signMessage(signinSignature.getBytes())));
    }
}
