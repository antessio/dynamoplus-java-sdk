package antessio.dynamoplus.http;


public interface SdkHttpClient {

    SdkHttpResponse execute(SdkHttpRequest sdkHttpRequest) throws Exception;

}
