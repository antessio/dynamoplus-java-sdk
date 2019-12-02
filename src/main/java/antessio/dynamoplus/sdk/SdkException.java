package antessio.dynamoplus.sdk;

import java.io.IOException;

public class SdkException extends RuntimeException {

    private int httpCode;
    private String responseBody;

    public SdkException(int httpCode, String responseBody) {
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }

    public SdkException(Throwable cause) {
        super(cause);
    }

    public SdkException(String message, int httpCode, String responseBody) {
        super(message);
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }

    public SdkException(String message, Throwable cause, int httpCode, String responseBody) {
        super(message, cause);
        this.httpCode = httpCode;
        this.responseBody = responseBody;
    }

    public SdkException(Throwable cause, int httpCode, String responseBody) {
        super(cause);
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
