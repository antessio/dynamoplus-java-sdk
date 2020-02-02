package antessio.dynamoplus.http;


public interface SdkHttpClient {

    SdkHttpResponse get(SdkHttpRequest sdkHttpRequest) throws Exception;

    SdkHttpResponse post(SdkHttpRequest sdkHttpRequest) throws Exception;

    SdkHttpResponse put(SdkHttpRequest sdkHttpRequest) throws Exception;

    SdkHttpResponse patch(SdkHttpRequest sdkHttpRequest) throws Exception;

    SdkHttpResponse delete(SdkHttpRequest sdkHttpRequest) throws Exception;


}
