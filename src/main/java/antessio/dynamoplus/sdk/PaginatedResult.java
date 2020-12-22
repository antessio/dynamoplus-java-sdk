package antessio.dynamoplus.sdk;

import java.io.Serializable;
import java.util.List;

public class PaginatedResult<T> implements Serializable {
    private List<T> data;
    private Boolean hasMore;

    public PaginatedResult() {
    }

    public PaginatedResult(List<T> data, Boolean hasMore) {
        this.data = data;
        this.hasMore = hasMore;
    }

    public List<T> getData() {
        return data;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    @Override
    public String toString() {
        return "PaginatedResult{" +
                "data=" + data +
                ", hasMore=" + hasMore +
                '}';
    }
}
