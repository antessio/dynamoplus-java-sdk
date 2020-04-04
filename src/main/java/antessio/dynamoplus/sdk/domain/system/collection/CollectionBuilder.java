package antessio.dynamoplus.sdk.domain.system.collection;

import java.util.List;

public class CollectionBuilder {
    private String idKey;
    private String name;
    private List<Attribute> attributes;

    public CollectionBuilder idKey(String idKey) {
        this.idKey = idKey;
        return this;
    }

    public CollectionBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CollectionBuilder fields(List<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Collection createCollection() {
        return new Collection(idKey, name, attributes);
    }
}