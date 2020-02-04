package antessio.dynamoplus.authentication.basic;

public class BasicAuthCredentialsProviderBuilder {
    private String username;
    private String password;

    public BasicAuthCredentialsProviderBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public BasicAuthCredentialsProviderBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public BasicAuthCredentialsProvider createBasicAuthCredentialsProvider() {
        return new BasicAuthCredentialsProvider(username, password);
    }
}