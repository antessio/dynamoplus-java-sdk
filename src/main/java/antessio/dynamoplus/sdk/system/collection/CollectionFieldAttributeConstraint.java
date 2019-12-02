package antessio.dynamoplus.sdk.system.collection;

public enum CollectionFieldAttributeConstraint {

    NULLABLE(0),
    NOT_NULL(1);

    private final int typeCode;

    CollectionFieldAttributeConstraint(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
