package antessio.dynamoplus.sdk.domain.conditions;

import java.util.List;

public class And implements Predicate {
    private List<Predicate> and;

    public And() {
    }

    public And(List<Predicate> and) {
        this.and = and;
    }

    public List<Predicate> getAnd() {
        return and;
    }


    @Override
    public String toString() {
        return "And{" +
                "and=" + and +
                '}';
    }

}
