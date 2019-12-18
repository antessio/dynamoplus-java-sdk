package antessio.dynamoplus.sdk;

import java.io.Serializable;
import java.util.List;

public class PaginatedResult<T> implements Serializable {
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
