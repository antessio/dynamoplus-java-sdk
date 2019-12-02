package antessio.dynamoplus.sdk;

public class Query<T> {
    private String startFrom;
    private T matches;
    private Integer limit;

    public Query(String startFrom, T matches, Integer limit) {
        this.startFrom = startFrom;
        this.matches = matches;
        this.limit = limit;
    }

    public String getStartFrom() {
        return startFrom;
    }

    public T getMatches() {
        return matches;
    }

    public Integer getLimit() {
        return limit;
    }
}
