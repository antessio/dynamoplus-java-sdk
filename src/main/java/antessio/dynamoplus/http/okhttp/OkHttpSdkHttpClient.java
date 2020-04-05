package antessio.dynamoplus.http.okhttp;


import antessio.dynamoplus.authentication.bean.Credentials;
import antessio.dynamoplus.authentication.provider.CredentialsProvider;
import antessio.dynamoplus.http.*;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class OkHttpSdkHttpClient extends AbstractSdkHttpClient {


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;

    private class DigestHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            Optional<Request> maybeNewRequest = bodyToString(request)
                    .map(HttpUtils::getDigestHeaderFromRequestBody)
                    .map(Collections::singletonList)
                    .map(headers -> addHeaders(request, headers));
            return chain.proceed(maybeNewRequest.orElse(request));
        }

    }

    private class CredentialsProviderNetworkInterceptor implements Interceptor {
        private final CredentialsProvider credentialsProvider;

        CredentialsProviderNetworkInterceptor(CredentialsProvider credentialsProvider) {
            this.credentialsProvider = credentialsProvider;
        }


        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Credentials credentials = credentialsProvider.getCredentials(fromOkHttpClientRequest(request));
            return chain.proceed(addCredentialsHeaders(request, credentials));
        }

        private Request addCredentialsHeaders(Request request, Credentials credentials) {
            return addHeaders(request, credentials.getHeader());
        }

        private SdkHttpRequest fromOkHttpClientRequest(Request request) {
            String baseUrl = request.url().url().getProtocol() + "://" + request.url().url().getHost();
            List<String> headers = request.headers()
                    .toMultimap()
                    .entrySet()
                    .stream()
                    .map(e -> e.getKey() + ":" + String.join(" ", e.getValue()))
                    .collect(Collectors.toList());
            return new SdkHttpRequest(
                    baseUrl,
                    request.url().encodedPath(),
                    SdkHttpRequest.HttpMethod.valueOf(request.method().toUpperCase()),
                    headers,
                    bodyToString(request).orElse(null)
            );
        }
    }

    private static Optional<String> bodyToString(final Request request) {

        return Optional.ofNullable(request)
                .map(Request::newBuilder)
                .map(Request.Builder::build)
                .map(Request::body)
                .map(body -> {
                    Buffer buffer = new Buffer();
                    try {
                        body.writeTo(buffer);
                        return buffer.readUtf8();
                    } catch (IOException e) {
                        return null;
                    }
                });
    }

    private static Request addHeaders(Request request, List<String> headers) {
        Headers.Builder headersBuilder = request.headers().newBuilder();
        for (String h : headers) {
            String[] split = h.split(":");
            headersBuilder.add(split[0], split[1]);
        }
        return new Request.Builder()
                .url(request.url())
                .method(request.method(), request.body())
                .tag(request.tag())
                .headers(headersBuilder.build())
                .build();
    }

    public OkHttpSdkHttpClient(HttpConfiguration httpConfiguration, CredentialsProvider credentialsProvider) {
        super(httpConfiguration, credentialsProvider);
        this.client = new OkHttpClient.Builder()
                .connectTimeout(httpConfiguration.getReadTimeoutInMilliseconds(), TimeUnit.MILLISECONDS)
                .readTimeout(httpConfiguration.getReadTimeoutInMilliseconds(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpConfiguration.getWriteTimeoutInMilliseconds(), TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new DigestHeaderInterceptor())
                .addNetworkInterceptor(new CredentialsProviderNetworkInterceptor(credentialsProvider))
                .build();

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
