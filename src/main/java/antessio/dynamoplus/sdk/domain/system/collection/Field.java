package antessio.dynamoplus.sdk.domain.system.collection;

import java.util.Collections;
import java.util.List;

public class Field {
    private final String attributeName;
    private final CollectionAttributeType attributeType;
    private final List<CollectionFieldAttributeConstraint> constraints;

    public Field(String attributeName, CollectionAttributeType attributeType) {
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.constraints = Collections.emptyList();

    }

    public Field(String attributeName, CollectionAttributeType attributeType, List<CollectionFieldAttributeConstraint> constraints) {
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.constraints = constraints;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public CollectionAttributeType getAttributeType() {
        return attributeType;
    }

    public List<CollectionFieldAttributeConstraint> getConstraints() {
        return constraints;
    }
}
