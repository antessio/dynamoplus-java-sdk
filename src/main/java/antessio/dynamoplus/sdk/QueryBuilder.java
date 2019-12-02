package antessio.dynamoplus.sdk;

public class QueryBuilder<T> {
    private String startFrom;
    private T matches;
    private Integer limit;

    public QueryBuilder startFrom(String startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    public QueryBuilder matches(T matches) {
        this.matches = matches;
        return this;
    }

    public QueryBuilder limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Query build() {
        return new Query(startFrom, matches, limit);
    }
}