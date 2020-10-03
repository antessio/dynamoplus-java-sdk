package antessio.dynamoplus.sdk.domain.document.query;

import antessio.dynamoplus.sdk.domain.conditions.Predicate;

public class Query {
    private Predicate matches;

    public Query() {
    }

    public Query(Predicate matches) {
        this.matches = matches;
    }


    public Predicate getMatches() {
        return matches;
    }
}
