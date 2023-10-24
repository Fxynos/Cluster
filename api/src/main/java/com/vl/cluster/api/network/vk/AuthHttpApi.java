package com.vl.cluster.api.network.vk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthHttpApi {
    @GET("/token?grant_type=password&fa_supported=1&v=5.131&")
    Call<Auth> signIn(@Query("client_id") Integer clientID,
                      @Query("client_secret") String clientSecret,
                      @Query("username") String username,
                      @Query("password") String password);
    @GET("/token?grant_type=password&fa_supported=1&v=5.131&")
    Call<Auth> SignInWithCaptcha(@Query("client_id") Integer clientID,
                                 @Query("client_secret") String clientSecret,
                                 @Query("username") String username,
                                 @Query("password") String password,
                                 @Query("captcha_key") String captchaKey,
                                 @Query("captcha_sid") String captchaSid);
    @GET("/token?grant_type=password&fa_supported=1&v=5.131&")
    Call<Auth> SignInWithCode(@Query("client_id") Integer clientID,
                              @Query("client_secret") String clientSecret,
                              @Query("username") String username,
                              @Query("password") String password,
                              @Query("code") String code);
}
