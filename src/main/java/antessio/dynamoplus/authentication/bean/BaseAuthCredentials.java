package antessio.dynamoplus.authentication.bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class BaseAuthCredentials implements Credentials {

    private final String username;
    private final String password;

    public BaseAuthCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public List<String> getHeader() {
        return Collections.singletonList("Authorization: Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8)));
    }
}
