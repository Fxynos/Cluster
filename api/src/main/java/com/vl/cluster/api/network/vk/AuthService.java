package com.vl.cluster.api.network.vk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {
    private static AuthService mInstance;
    private static final String BASE_URL = "https://oauth.vk.com";
    private final Retrofit retrofit;

    private AuthService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static AuthService getInstance() {
        if (mInstance == null) {
            mInstance = new AuthService();
        }
        return mInstance;
    }

    public AuthVK getAccessToken() {
        return retrofit.create(AuthVK.class);
    }
}
