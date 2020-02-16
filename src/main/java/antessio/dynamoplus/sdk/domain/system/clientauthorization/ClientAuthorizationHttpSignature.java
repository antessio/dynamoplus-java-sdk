package antessio.dynamoplus.sdk.domain.system.clientauthorization;

import java.util.List;

public class ClientAuthorizationHttpSignature extends AbstractClientAuthorization {
    private final String publicKey;
    private final ClientAuthorizationType type;

    public ClientAuthorizationHttpSignature(String clientId, List<ClientScope> clientScopes, String publicKey) {
        super(clientId, clientScopes);
        this.publicKey = publicKey;
        this.type = ClientAuthorizationType.http_signature;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public ClientAuthorizationType getType() {

        return type;
    }
}
