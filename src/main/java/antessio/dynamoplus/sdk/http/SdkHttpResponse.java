package antessio.dynamoplus.sdk.http;

public class SdkHttpResponse<T> {
    private final String responseBody;
    private final int httpStatusCode;

    public SdkHttpResponse(int httpStatusCode, String responseBody) {
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
