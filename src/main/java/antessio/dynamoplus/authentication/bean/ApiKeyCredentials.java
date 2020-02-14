package antessio.dynamoplus.authentication.bean;

public class ApiKeyCredentials implements Credentials {

    private final static String AUTHORIZATION_PREFIX = "dynamoplus-api-key";

    private final String apiKey;

    public ApiKeyCredentials(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getHeader() {
        return String.format("Authorization: %s %s", AUTHORIZATION_PREFIX, apiKey);
    }
}
