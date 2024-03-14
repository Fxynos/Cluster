package com.vl.cluster.api.network.vk.dto;

import com.google.gson.annotations.SerializedName;

public class AuthErrorResponse {
    @SerializedName("error")
    private String errorName;
    @SerializedName("error_description")
    private String errorDescription;
    @SerializedName("validation_type")
    private String validationType;

    @SerializedName("phone_mask")
    private String phoneMask;

    @SerializedName("captcha_sid")
    private String captchaSid;

    @SerializedName("captcha_img")
    private String captchaImg;

    public String getErrorName() {
        return errorName;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getValidationType() {
        return validationType;
    }

    public String getPhoneMask() {
        return phoneMask;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + errorName + '\'' +
                ", error_description='" + errorDescription + '\'' +
                ", validation_type='" + validationType + '\'' +
                '}';
    }

    public String getCaptchaSid() {
        return captchaSid;
    }

    public String getCaptchaImg() {
        return captchaImg;
    }
}
