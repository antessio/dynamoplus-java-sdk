package antessio.dynamoplus.sdk.domain.conditions;

public class Eq implements Predicate {
    private String fieldName;
    private String value;

    public Eq() {
    }

    public Eq(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "Eq{" +
                "fieldName='" + fieldName + '\'' +
                ", fieldValue='" + value + '\'' +
                '}';
    }
}
