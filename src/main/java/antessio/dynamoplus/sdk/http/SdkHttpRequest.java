package antessio.dynamoplus.sdk.http;

import java.util.List;

public class SdkHttpRequest {
    private final String baseUrl;
    private final String path;
    private final List<String> headers;
    private final String body;

    public SdkHttpRequest(String baseUrl, String path, List<String> headers, String body) {
        this.baseUrl = baseUrl;
        this.path = path;
        this.headers = headers;
        this.body = body;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPath() {
        return path;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }


}
