package antessio.dynamoplus.sdk.domain.system.collection;

public enum CollectionAttributeConstraint {

    NULLABLE(0),
    NOT_NULL(1);

    private final int typeCode;

    CollectionAttributeConstraint(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
