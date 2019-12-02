package antessio.dynamoplus.sdk.system.collection;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Collection {

    private String idKey;
    private String name;
    private List<Field> fields;

    public Collection(String idKey, String name) {
        this.idKey = idKey;
        this.name = name;
        this.fields = Collections.emptyList();
    }

    public Collection() {
    }

    public Collection(String idKey, String name, List<Field> fields) {
        this.idKey = idKey;
        this.name = name;
        this.fields = fields;
    }

    public String getIdKey() {
        return idKey;
    }

    public String getName() {
        return name;
    }

    public List<Field> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "idKey='" + idKey + '\'' +
                ", name='" + name + '\'' +
                ", fields=" + fields +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection that = (Collection) o;
        return Objects.equals(idKey, that.idKey) &&
                Objects.equals(name, that.name) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idKey, name, fields);
    }
}
