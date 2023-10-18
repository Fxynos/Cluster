package com.vl.cluster.api.network.vk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthHttpApi {
    @GET("/token?grant_type=password&")
    Call<Auth> signIn(@Query("client_id") Integer clientID,
                      @Query("client_secret") String clientSecret,
                      @Query("username") String username,
                      @Query("password") String password);
}
