package antessio.dynamoplus.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SdkHttpRequest {

    public enum HttpMethod {
        GET,
        POST,
        DELETE,
        PATCH,
        PUT
    }

    private final String baseUrl;
    private final HttpMethod method;
    private final String path;
    private final List<String> headers;
    private final String body;
    private final Map<String, String> queryParameters;

    public SdkHttpRequest(String baseUrl, String path, HttpMethod method, List<String> headers, String body) {
        this.baseUrl = baseUrl;
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParameters = Collections.emptyMap();
    }

    public SdkHttpRequest(String baseUrl, String path, HttpMethod method, List<String> headers, String body, Map<String, String> queryParameters) {
        this.baseUrl = baseUrl;
        this.path = path;
        this.method = method;
        this.headers = headers;
        this.body = body;
        this.queryParameters = queryParameters;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }


}
