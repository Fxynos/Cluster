package com.vl.cluster.data.network.vk;

import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.httpclient.ConnectionsSupervisor;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Used instead of {@link com.vk.api.sdk.httpclient.HttpTransportClient} <br/>
 * Implementation fixes <code>java.lang.NoSuchFieldError: No static field INSTANCE of type Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; in class Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; or its superclasses (declaration of 'org.apache.http.conn.ssl.AllowAllHostnameVerifier' appears in /system/framework/framework.jar!classes2.dex)</code>
 * error on Android.
 */
public class HttpTransportClient implements TransportClient {
    OkHttpClient httpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
    protected static final String CONTENT_TYPE_HEADER = "Content-Type";
    protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";


    protected static Map<String, String> getHeaders(Headers headers) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0, size = headers.size(); i < size; i++) {
            result.put(headers.name(i), headers.value(i));
        }
        return result;
    }

    private String validateHeaderName(String name) {
        return Stream.of(name.split("-"))
                .map((s) -> s.substring(0, 1).toUpperCase().concat(s.substring(1).toLowerCase()))
                .reduce((s1, s2) -> String.format("%s-%s", s1, s2))
                .orElseGet(() -> name.substring(0, 1).toUpperCase().concat(name.substring(1).toLowerCase()));
    }

    @Override
    public ClientResponse post(String url, String body) throws IOException {
        return post(url, body, FORM_CONTENT_TYPE);
    }

    @Override
    public ClientResponse post(String url, String fileName, File file) throws IOException {
        return null;
    }

    @Override
    public ClientResponse post(String url, String body, String contentType) throws IOException {
        MediaType JSON = MediaType.get("application/x-www-form-urlencoded; charset=UTF-8");
        RequestBody requestBody = RequestBody.create(body, JSON);
        Request request = new Request.Builder()
                .url(url).
                post(requestBody).
                header(CONTENT_TYPE_HEADER, contentType).
                build();
        return call(request);
    }

    @Override
    public ClientResponse post(String url) throws IOException {
        return post(url, null);
    }

    @Override
    public ClientResponse post(String url, String body, Header[] headers) throws IOException {
        RequestBody requestBody = RequestBody.create(body, JSON);
        Request.Builder requestBuilder = new Request.Builder();
        for (Header header : headers) {
            requestBuilder.header(header.getName(), header.getValue());
        }
        Request request = requestBuilder.url(url).post(requestBody).build();
        return call(request);
    }

    @Override
    public ClientResponse get(String url) throws IOException {
        return get(url, FORM_CONTENT_TYPE);
    }


    protected ClientResponse call(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            Map<String, String> responseHeaders = getHeaders(response.headers()).entrySet().stream().map((entry) -> Map.entry(validateHeaderName(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            responseHeaders.forEach((key, value) -> System.out.printf("%s:%s\n", key, value));
            if (response.body() != null) {
                String responseBody = response.body().string();
                return new ClientResponse(response.code(), responseBody, responseHeaders);
            }
        }
        return null;
    }

    @Override
    public ClientResponse get(String url, String contentType) throws IOException {
        Request request = new Request.Builder()
                .url(url).
                addHeader(CONTENT_TYPE_HEADER, contentType).
                build();
        return call(request);
    }


    @Override
    public ClientResponse get(String url, Header[] headers) throws IOException {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader(CONTENT_TYPE_HEADER, FORM_CONTENT_TYPE);
        for (Header header : headers) {
            requestBuilder.addHeader(header.getName(), header.getValue());
        }
        Request request = requestBuilder.url(url).build();
        return call(request);
    }

    @Override
    public ClientResponse delete(String url) throws IOException {
        return get(url);
    }

    @Override
    public ClientResponse delete(String url, String body) throws IOException {
        return null;
    }

    @Override
    public ClientResponse delete(String url, String body, String contentType) throws IOException {
        return null;
    }

    @Override
    public ClientResponse delete(String url, String body, Header[] headers) throws IOException {
        return null;
    }
}
