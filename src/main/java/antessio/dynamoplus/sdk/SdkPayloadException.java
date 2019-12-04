package antessio.dynamoplus.sdk;

public class SdkPayloadException extends SdkException {


    public SdkPayloadException(String message) {
        super(message);
    }

    public SdkPayloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public SdkPayloadException(Throwable cause) {
        super(cause);
    }
}
