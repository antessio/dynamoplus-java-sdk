package antessio.dynamoplus.sdk;

import java.util.List;

public class PaginatedResult<T> {
    private List<T> data;
    private String lastKey;

    public PaginatedResult() {
    }

    public PaginatedResult(List<T> data, String lastKey) {
        this.data = data;
        this.lastKey = lastKey;
    }

    public List<T> getData() {
        return data;
    }

    public String getLastKey() {
        return lastKey;
    }
}
