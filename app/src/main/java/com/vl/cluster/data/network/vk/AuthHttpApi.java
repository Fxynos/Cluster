package com.vl.cluster.data.network.vk;

import com.vl.cluster.data.network.vk.dto.AuthSuccessResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthHttpApi {
    @GET("/token?grant_type=password&2fa_supported=1&v=5.131&")
    Call<AuthSuccessResponse> signIn(@Query("client_id") Integer clientID,
                                     @Query("client_secret") String clientSecret,
                                     @Query("username") String username,
                                     @Query("password") String password);
    @GET("/token?grant_type=password&2fa_supported=1&v=5.131&")
    Call<AuthSuccessResponse> signInWithCaptcha(@Query("client_id") Integer clientID,
                                                @Query("client_secret") String clientSecret,
                                                @Query("username") String username,
                                                @Query("password") String password,
                                                @Query("captcha_key") String captchaKey,
                                                @Query("captcha_sid") String captchaSid);
    @GET("/token?grant_type=password&2fa_supported=1&v=5.131&")
    Call<AuthSuccessResponse> signInWithCode(@Query("client_id") Integer clientID,
                                             @Query("client_secret") String clientSecret,
                                             @Query("username") String username,
                                             @Query("password") String password,
                                             @Query("code") String code);
}
