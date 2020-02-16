package antessio.dynamoplus.sdk.domain.system.clientauthorization;

import java.util.List;

public interface ClientAuthorization {


    enum ClientAuthorizationType {
        http_signature,
        api_key
    }

    String getClientId();

    ClientAuthorizationType getType();

    List<ClientScope> getClientScopes();

}
