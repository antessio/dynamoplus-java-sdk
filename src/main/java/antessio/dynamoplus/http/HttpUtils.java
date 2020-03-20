package antessio.dynamoplus.http;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HttpUtils {
    public static final String HEADER_FIELD_SEPARATOR = ":";

    private HttpUtils() {
    }

    public static String[] splitHeader(String header) {
        return header.split(HEADER_FIELD_SEPARATOR, 2);
    }


    public static String getDigestHeaderFromRequestBody(String requestBody) {
        return String.format("Digest%s SHA-256=%s", HEADER_FIELD_SEPARATOR, hashWith256(requestBody));
    }

    private static String hashWith256(String textToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            String encoded = Base64.getEncoder().encodeToString(hashedByetArray);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
