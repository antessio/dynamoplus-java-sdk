package antessio.dynamoplus.http;

import com.squareup.okhttp.*;

import java.util.concurrent.TimeUnit;


public class DefaultSdkHttpClient extends AbstractSdkHttpClient {


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    public DefaultSdkHttpClient(HttpConfiguration httpConfiguration) {
        super(httpConfiguration);
        this.client = new OkHttpClient();
        this.client.setConnectTimeout(httpConfiguration.getConnectTimeoutInMilliseconds(), TimeUnit.MILLISECONDS);
        this.client.setReadTimeout(httpConfiguration.getReadTimeoutInMilliseconds(), TimeUnit.MILLISECONDS);
        this.client.setWriteTimeout(httpConfiguration.getWriteTimeoutInMilliseconds(), TimeUnit.MILLISECONDS);

    }

    SdkHttpResponse get(SdkHttpRequest r) throws Exception {

        Request.Builder requestBuilder = buildHttpRequest(r);

        r.getHeaders().forEach(h -> {
            String[] splitted = h.split(":");
            requestBuilder.addHeader(splitted[0], splitted[1]);
        });

        Request request = requestBuilder
                .get()
                .build();
        Response response = client.newCall(request).execute();
        return new SdkHttpResponse(response.code(), response.body().string());

    }


    SdkHttpResponse post(SdkHttpRequest r) throws Exception {
        Request.Builder requestBuilder = buildHttpRequest(r);

        r.getHeaders().forEach(h -> {
            String[] splitted = h.split(":");
            requestBuilder.addHeader(splitted[0], splitted[1]);
        });

        Request request = requestBuilder
                .post(RequestBody.create(JSON, r.getBody()))
                .build();
        Response response = client.newCall(request).execute();
        return new SdkHttpResponse<>(response.code(), response.body().string());
    }

    SdkHttpResponse put(SdkHttpRequest r) throws Exception {
        Request.Builder requestBuilder = buildHttpRequest(r);

        r.getHeaders().forEach(h -> {
            String[] splitted = h.split(":");
            requestBuilder.addHeader(splitted[0], splitted[1]);
        });

        Request request = requestBuilder
                .put(RequestBody.create(JSON, r.getBody()))
                .build();
        Response response = client.newCall(request).execute();
        return new SdkHttpResponse<>(response.code(), response.body().string());
    }


    SdkHttpResponse patch(SdkHttpRequest r) throws Exception {
        Request.Builder requestBuilder = buildHttpRequest(r);

        r.getHeaders().forEach(h -> {
            String[] splitted = h.split(":");
            requestBuilder.addHeader(splitted[0], splitted[1]);
        });

        Request request = requestBuilder
                .patch(RequestBody.create(JSON, r.getBody()))
                .build();
        Response response = client.newCall(request).execute();
        return new SdkHttpResponse<>(response.code(), response.body().string());
    }

    SdkHttpResponse delete(SdkHttpRequest r) throws Exception {
        Request.Builder requestBuilder = buildHttpRequest(r);

        r.getHeaders().forEach(h -> {
            String[] splitted = h.split(":");
            requestBuilder.addHeader(splitted[0], splitted[1]);
        });

        Request request = requestBuilder
                .delete(RequestBody.create(JSON, r.getBody()))
                .build();
        Response response = client.newCall(request).execute();
        return new SdkHttpResponse<>(response.code(), response.body().string());
    }

    private Request.Builder buildHttpRequest(SdkHttpRequest r) {
        return new Request.Builder()
                .url(r.getBaseUrl() + "/" + r.getPath());
    }

    @Override
    public SdkHttpResponse execute(SdkHttpRequest sdkHttpRequest) throws Exception {


        switch (sdkHttpRequest.getMethod()) {
            case GET:
                return get(sdkHttpRequest);
            case PUT:
                return put(sdkHttpRequest);
            case POST:
                return post(sdkHttpRequest);
            case PATCH:
                return patch(sdkHttpRequest);
            case DELETE:
                return delete(sdkHttpRequest);
            default:
                throw new RuntimeException("not supported");
        }

    }
}
