package com.vl.cluster.api.network.vk;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthVK {
    @GET("token")
    Call<Auth> getAccessToken(@Query("client_id") Integer clientID,
                                    @Query("client_secret") String clientSecret,
                                    @Query("grant_type") String grantType,
                                    @Query("username") String username,
                                    @Query("password") String password);
}
