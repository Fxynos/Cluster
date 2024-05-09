package com.vl.cluster.data.network.vk.dto;

import com.google.gson.annotations.SerializedName;

public class AuthSuccessResponse {
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("expires_in")
    private int expiresIn;

    public AuthSuccessResponse(String accessToken, int userId, int expiresIn) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getUserId() {
        return userId;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "access_token='" + accessToken + '\'' +
                ", user_id='" + userId + '\'' +
                ", expires_in='" + expiresIn + '\'' +
                '}';
    }
}
