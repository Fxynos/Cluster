package com.vl.cluster.api.network.vk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("user_id")
    Integer userID;
    @SerializedName("expires_in")
    Integer expiresIn;

    public Auth(String accessToken, Integer userID, Integer expiresIn){
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.userID = userID;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "access_token='" + accessToken + '\'' +
                ", user_id='" + userID + '\'' +
                ", expires_in='" + expiresIn + '\'' +
                '}';
    }
}
