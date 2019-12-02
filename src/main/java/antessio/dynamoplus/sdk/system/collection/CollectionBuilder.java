package antessio.dynamoplus.sdk.system.collection;

import java.util.List;

public class CollectionBuilder {
    private String idKey;
    private String name;
    private List<Field> fields;

    public CollectionBuilder idKey(String idKey) {
        this.idKey = idKey;
        return this;
    }

    public CollectionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CollectionBuilder fields(List<Field> fields) {
        this.fields = fields;
        return this;
    }

    public Collection createCollection() {
        return new Collection(idKey, name);
    }
}