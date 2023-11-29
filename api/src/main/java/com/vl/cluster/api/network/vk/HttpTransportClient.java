package com.vl.cluster.api.network.vk;

import com.vk.api.sdk.client.ClientResponse;
import com.vk.api.sdk.client.TransportClient;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

/**
 * Used instead of {@link com.vk.api.sdk.httpclient.HttpTransportClient} <br/>
 * Implementation fixes <code>java.lang.NoSuchFieldError: No static field INSTANCE of type Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; in class Lorg/apache/http/conn/ssl/AllowAllHostnameVerifier; or its superclasses (declaration of 'org.apache.http.conn.ssl.AllowAllHostnameVerifier' appears in /system/framework/framework.jar!classes2.dex)</code>
 * error on Android.
 */
public class HttpTransportClient implements TransportClient {
    @Override
    public ClientResponse post(String url, String body) throws IOException {
        return null;
    }

    @Override
    public ClientResponse post(String url, String fileName, File file) throws IOException {
        return null;
    }

    @Override
    public ClientResponse post(String url, String body, String contentType) throws IOException {
        return null;
    }

    @Override
    public ClientResponse post(String url) throws IOException {
        return null;
    }

    @Override
    public ClientResponse post(String url, String body, Header[] headers) throws IOException {
        return null;
    }

    @Override
    public ClientResponse get(String url) throws IOException {
        return null;
    }

    @Override
    public ClientResponse get(String url, String contentType) throws IOException {
        return null;
    }

    @Override
    public ClientResponse get(String url, Header[] headers) throws IOException {
        return null;
    }

    @Override
    public ClientResponse delete(String url) throws IOException {
        return null;
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
