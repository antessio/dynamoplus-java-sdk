package antessio.dynamoplus.http;

import java.util.List;
import java.util.Map;

public final class SdkHttpRequestBuilder {
    private String baseUrl;
    private SdkHttpRequest.HttpMethod method;
    private String path;
    private List<String> headers;
    private String body;
    private Map<String, String> queryParameters;

    private SdkHttpRequestBuilder() {
    }

    public static SdkHttpRequestBuilder aSdkHttpRequest() {
        return new SdkHttpRequestBuilder();
    }

    public SdkHttpRequestBuilder withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public SdkHttpRequestBuilder withMethod(SdkHttpRequest.HttpMethod method) {
        this.method = method;
        return this;
    }

    public SdkHttpRequestBuilder withPath(String path) {
        this.path = path;
        return this;
    }

    public SdkHttpRequestBuilder withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    public SdkHttpRequestBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public SdkHttpRequestBuilder withQueryParameters(Map<String, String> queryParameters) {
        this.queryParameters = queryParameters;
        return this;
    }

    public SdkHttpRequest build() {
        return new SdkHttpRequest(baseUrl, path, method, headers, body, queryParameters);
    }
}
