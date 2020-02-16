package antessio.dynamoplus.sdk.domain.system.clientauthorization;

import java.util.Arrays;
import java.util.List;

public class ClientScope {

    public enum ClientScopeType {
        QUERY,
        CREATE,
        GET,
        UPDATE,
        DELETE;
    }

    public static final List<ClientScopeType> READ = Arrays.asList(ClientScopeType.QUERY, ClientScopeType.GET);
    public static final List<ClientScopeType> READ_WRITE = Arrays.asList(ClientScopeType.QUERY, ClientScopeType.GET, ClientScopeType.CREATE, ClientScopeType.UPDATE, ClientScopeType.DELETE);

    private final String collectionName;
    private final ClientScopeType scopeType;

    public ClientScope(String collectionName, ClientScopeType scopeType) {
        this.collectionName = collectionName;
        this.scopeType = scopeType;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public ClientScopeType getScopeType() {
        return scopeType;
    }
}
