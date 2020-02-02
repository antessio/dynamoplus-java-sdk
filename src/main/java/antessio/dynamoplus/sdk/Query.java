package antessio.dynamoplus.sdk;

import java.io.Serializable;

public class Query<T> implements Serializable {
    private String startFrom;
    private T matches;
    private Integer limit;

    public Query(T matches, Integer limit, String startFrom) {
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

    @Override
    public String toString() {
        return "Query{" +
                "startFrom='" + startFrom + '\'' +
                ", matches=" + matches +
                ", limit=" + limit +
                '}';
    }
}
