package antessio.dynamoplus.sdk;


public class SdkHttpException extends SdkException {

    private int httpCode;
    private String responseBody;

    public SdkHttpException(int httpCode, String responseBody) {
        super(responseBody);
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
