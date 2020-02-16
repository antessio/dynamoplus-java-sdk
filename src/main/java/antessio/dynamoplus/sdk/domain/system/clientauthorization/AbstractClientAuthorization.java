package antessio.dynamoplus.sdk.domain.system.clientauthorization;

import java.util.List;

public abstract class AbstractClientAuthorization implements ClientAuthorization {
    protected final String clientId;
    protected final List<ClientScope> clientScopes;

    public AbstractClientAuthorization(String clientId, List<ClientScope> clientScopes) {
        this.clientId = clientId;
        this.clientScopes = clientScopes;
    }


    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public List<ClientScope> getClientScopes() {
        return clientScopes;
    }

}
