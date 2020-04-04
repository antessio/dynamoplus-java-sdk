package antessio.dynamoplus.sdk.domain.system.collection;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Collection {

    private String idKey;
    private String name;
    private List<Attribute> attributes;

    public Collection() {
    }

    public Collection(String idKey, String name, List<Attribute> attributes) {
        this.idKey = idKey;
        this.name = name;
        this.attributes = attributes;
    }

    public String getIdKey() {
        return idKey;
    }

    public String getName() {
        return name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "idKey='" + idKey + '\'' +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(idKey, that.idKey) &&
                Objects.equals(name, that.name) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKey, name, attributes);
    }
}
