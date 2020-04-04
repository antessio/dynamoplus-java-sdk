package antessio.dynamoplus.sdk.domain.system.collection;

import java.util.List;

public class Attribute {
    private String attributeName;
    private CollectionAttributeType attributeType;
    private List<CollectionAttributeConstraint> constraints;
    private List<Attribute> attributes;

    public Attribute() {
    }

    public Attribute(String attributeName, CollectionAttributeType attributeType, List<CollectionAttributeConstraint> constraints, List<Attribute> attributes) {
        this.attributeName = attributeName;
        this.attributeType = attributeType;
        this.constraints = constraints;
        this.attributes = attributes;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public CollectionAttributeType getAttributeType() {
        return attributeType;
    }

    public List<CollectionAttributeConstraint> getConstraints() {
        return constraints;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
}
