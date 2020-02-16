package antessio.dynamoplus.sdk.domain.system.clientauthorization;

import java.util.List;

public class ClientAuthorizationApiKey extends AbstractClientAuthorization {
    private final String apiKey;
    private final List<String> whitelistHosts;
    private final ClientAuthorizationType type;

    public ClientAuthorizationApiKey(String clientId, List<ClientScope> clientScopes, String apiKey, List<String> whitelistHosts) {
        super(clientId, clientScopes);
        this.apiKey = apiKey;
        this.whitelistHosts = whitelistHosts;
        this.type = ClientAuthorizationType.api_key;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<String> getWhitelistHosts() {
        return whitelistHosts;
    }

    @Override
    public ClientAuthorizationType getType() {
        return type;
    }
}
