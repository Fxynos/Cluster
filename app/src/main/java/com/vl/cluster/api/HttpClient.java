package com.vl.cluster.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {
    private final Retrofit retrofit;

    public HttpClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T getApi(Class<T> apiClass) {
        return retrofit.create(apiClass);
    }
}
