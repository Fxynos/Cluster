package com.vl.cluster.api.network.vk;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("error")
    private String error;
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

    public String getError() {
        return error;
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
                "error='" + error + '\'' +
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
