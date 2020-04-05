package antessio.dynamoplus.http.unirest;

import antessio.dynamoplus.authentication.bean.Credentials;
import antessio.dynamoplus.authentication.provider.CredentialsProvider;
import antessio.dynamoplus.http.*;
import kong.unirest.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class UniRestSdkHttpClient extends AbstractSdkHttpClient {


    public UniRestSdkHttpClient(HttpConfiguration httpConfiguration, CredentialsProvider credentialsProvider) {
        super(httpConfiguration, credentialsProvider);
        Interceptor digestInterceptor = new Interceptor() {
            @Override
            public void onRequest(HttpRequest<?> request, Config config) {
                request.getBody()
                        .map(UniRestSdkHttpClient.this::bodyAsString)
                        .map(HttpUtils::getDigestHeaderFromRequestBody)
                        .map(HttpUtils::splitHeader)
                        .ifPresent(splitted -> request.header(splitted[0], splitted[1]));
            }
        };
        Interceptor authenticationInterceptor = new Interceptor() {
            @Override
            public void onRequest(HttpRequest<?> request, Config config) {
                Credentials credentials = credentialsProvider.getCredentials(fromUnirestRequest(request));
                credentials.getHeader()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .forEach(h -> request.header(h[0], h[1]));
            }

            private SdkHttpRequest fromUnirestRequest(HttpRequest<?> request) {
                try {
                    URL url = new URL(request.getUrl());
                    String baseUrl = url.getProtocol() + "://" + url.getHost();
                    List<String> headers = request.getHeaders()
                            .all()
                            .stream()
                            .map(h -> h.getName() + HttpUtils.HEADER_FIELD_SEPARATOR + h.getValue())
                            .collect(Collectors.toList());
                    String body = request.getBody().map(UniRestSdkHttpClient.this::bodyAsString).orElse(null);
                    return new SdkHttpRequest(baseUrl, url.getPath(), SdkHttpRequest.HttpMethod.valueOf(request.getHttpMethod().name()), headers, body);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(e);
                }

            }
        };
        Unirest.config()
                .connectTimeout(httpConfiguration.getConnectTimeoutInMilliseconds())
                .socketTimeout(httpConfiguration.getReadTimeoutInMilliseconds())
                .interceptor(digestInterceptor)
                .interceptor(authenticationInterceptor)
        ;
    }

    private String bodyAsString(Body body) {
        if (body.isEntityBody()) {
            if (!body.isMultiPart()) {
                Object obj = body.uniPart().getValue();
                return (String) obj;
            }
        }
        throw new UnsupportedOperationException("unable to convert request body");

    }

    private SdkHttpResponse get(SdkHttpRequest sdkHttpRequest) {
        GetRequest r = Unirest.get(String.format("%s/%s", sdkHttpRequest.getBaseUrl(), sdkHttpRequest.getPath()))
                .headers(sdkHttpRequest
                        .getHeaders()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .collect(Collectors.toMap(h -> h[0], h -> h[1]))
                );
        HttpResponse<String> response = r.asString();
        return new SdkHttpResponse(response.getStatus(), response.getBody());

    }

    private SdkHttpResponse post(SdkHttpRequest sdkHttpRequest) {
        RequestBodyEntity r = Unirest.post(String.format("%s/%s", sdkHttpRequest.getBaseUrl(), sdkHttpRequest.getPath()))
                .body(sdkHttpRequest.getBody())
                .headers(sdkHttpRequest
                        .getHeaders()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .collect(Collectors.toMap(h -> h[0], h -> h[1]))
                );
        HttpResponse<String> response = r.asString();
        return new SdkHttpResponse(response.getStatus(), response.getBody());
    }

    private SdkHttpResponse put(SdkHttpRequest sdkHttpRequest) {
        RequestBodyEntity r = Unirest.put(String.format("%s/%s", sdkHttpRequest.getBaseUrl(), sdkHttpRequest.getPath()))
                .body(sdkHttpRequest.getBody())
                .headers(sdkHttpRequest
                        .getHeaders()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .collect(Collectors.toMap(h -> h[0], h -> h[1]))
                );
        HttpResponse<String> response = r.asString();
        return new SdkHttpResponse(response.getStatus(), response.getBody());
    }

    private SdkHttpResponse patch(SdkHttpRequest sdkHttpRequest) {
        RequestBodyEntity r = Unirest.patch(String.format("%s/%s", sdkHttpRequest.getBaseUrl(), sdkHttpRequest.getPath()))
                .body(sdkHttpRequest.getBody())
                .headers(sdkHttpRequest
                        .getHeaders()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .collect(Collectors.toMap(h -> h[0], h -> h[1]))
                );
        HttpResponse<String> response = r.asString();
        return new SdkHttpResponse(response.getStatus(), response.getBody());
    }

    private SdkHttpResponse delete(SdkHttpRequest sdkHttpRequest) {
        RequestBodyEntity r = Unirest.delete(String.format("%s/%s", sdkHttpRequest.getBaseUrl(), sdkHttpRequest.getPath())).body(sdkHttpRequest.getBody())
                .headers(sdkHttpRequest
                        .getHeaders()
                        .stream()
                        .map(HttpUtils::splitHeader)
                        .collect(Collectors.toMap(h -> h[0], h -> h[1]))
                );
        HttpResponse<String> response = r.asString();
        return new SdkHttpResponse(response.getStatus(), response.getBody());
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
