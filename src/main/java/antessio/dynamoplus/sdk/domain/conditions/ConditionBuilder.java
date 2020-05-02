package antessio.dynamoplus.sdk.domain.conditions;

import java.util.List;

public class ConditionBuilder {

    private And and;
    private Eq eq;
    private Range range;

    public Predicate withAnd(List<Predicate> predicates) {
        return new And(predicates);
    }

    public Predicate withEq(String fieldName, String value) {
        return new Eq(fieldName, value);
    }

    public Predicate withRange(String fieldName, String from, String to) {
        return new Range(fieldName, from, to);
    }
}
