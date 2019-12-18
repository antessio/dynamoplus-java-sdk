package antessio.dynamoplus.sdk.http;

import com.squareup.okhttp.*;


public class DefaultSdkHttpClient implements SdkHttpClient {


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    public DefaultSdkHttpClient() {
        this.client = new OkHttpClient();

    }

    @Override
    public SdkHttpResponse get(SdkHttpRequest r) throws Exception {

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


    @Override
    public SdkHttpResponse post(SdkHttpRequest r) throws Exception {
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

    @Override
    public SdkHttpResponse put(SdkHttpRequest r) throws Exception {
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

    @Override
    public SdkHttpResponse patch(SdkHttpRequest r) throws Exception {
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

    @Override
    public SdkHttpResponse delete(SdkHttpRequest r) throws Exception {
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
}
