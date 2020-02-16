package antessio.dynamoplus.authentication.bean;

import java.util.Arrays;
import java.util.List;

public class ApiKeyCredentials implements Credentials {

    private final static String AUTHORIZATION_PREFIX = "dynamoplus-api-key";
    private final static String CLIENT_ID_HEADER = "dynamoplus-client-id";
    private final String apiKey;
    private final String clientId;

    public ApiKeyCredentials(String clientId, String apiKey) {
        this.clientId = clientId;
        this.apiKey = apiKey;
    }

    @Override
    public List<String> getHeader() {
        return Arrays.asList(
                String.format("Authorization: %s %s", AUTHORIZATION_PREFIX, apiKey),
                String.format("%s: %s ", CLIENT_ID_HEADER, clientId)
        );
    }
}
