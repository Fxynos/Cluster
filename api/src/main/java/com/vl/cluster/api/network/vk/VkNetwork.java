package com.vl.cluster.api.network.vk;

import com.google.gson.Gson;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vl.cluster.api.ApiCredentialsKt;
import com.vl.cluster.api.HttpClient;
import com.vl.cluster.api.definition.ConnectionException;
import com.vl.cluster.api.definition.Network;
import com.vl.cluster.api.definition.Session;
import com.vl.cluster.api.definition.features.NetworkAuth;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import retrofit2.Response;


public class VkNetwork implements
        Network<VkNetwork.VkSession>,
        NetworkAuth.Password<VkNetwork.VkSession> {
    private static final String NAME = "ВКонтакте";
    private final HttpClient authClient = new HttpClient("https://oauth.vk.com");

    @NotNull
    @Override
    public VkNetwork getAuthentication() {
        return this;
    }

    @NotNull
    @Override
    public Set<LoginType> getLoginVariants() {
        return Set.of(LoginType.PHONE);
    }

    public static class Fa2Exception extends Exception {
        String type2Fa;
        String phoneMask;


        public Fa2Exception(String type2Fa, String phoneMask) {
            this.type2Fa = type2Fa;
            this.phoneMask = phoneMask;
        }
    }

    public static class NeedCaptchaException extends Exception {
            String captchaSid;
            String captchaImg;

        public NeedCaptchaException(String captchaSid, String captchaImg) {
            this.captchaSid = captchaSid;
            this.captchaImg = captchaImg;
        }
    }

    public static class NeedFa2CallReset extends Exception {

    }

    @NotNull
    @Override
    public VkSession signIn(@NotNull String login, @NotNull String password)
            throws WrongCredentialsException, ConnectionException, Fa2Exception, NeedCaptchaException, NeedFa2CallReset {
        Response<Auth> auth;
        try {
            auth = authClient.getApi(AuthHttpApi.class).signIn(
                    Integer.parseInt(ApiCredentialsKt.CLIENT_ID_VK),
                    ApiCredentialsKt.CLIENT_SECRET_VK,
                    login,
                    password
            ).execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new ConnectionException();
        }
        checkAuthErrors(auth);
        Auth response = Objects.requireNonNull(auth.body());
        return new VkSession(response.getUserId(), response.getAccessToken());
    }

    private void checkAuthErrors(@NotNull Response<Auth> auth) throws WrongCredentialsException, Fa2Exception, NeedCaptchaException, NeedFa2CallReset{
        ErrorResponse error;
        Gson gson = new Gson();
        if (!auth.isSuccessful()) {
            error = gson.fromJson(auth.errorBody().charStream(), ErrorResponse.class);
            if (error.getError().equals("invalid_client")) {
                throw new WrongCredentialsException();
            }
            if (error.getError().equals("need_validation")) {
                if (error.getValidationType().equals("2fa_callreset")) {
                    throw new NeedFa2CallReset();
                } else {
                    System.out.println(error.getValidationType());
                    throw new Fa2Exception(error.getValidationType(), error.getPhoneMask());
                }
            }
            if (error.getError().equals("need_captcha")) {
                throw new NeedCaptchaException(error.getCaptchaSid(), error.getCaptchaImg());
            }
        }
    }

    public VkSession signInWithCaptcha(@NotNull String login, @NotNull String password, @NotNull String captchaKey, @NotNull String captchaSid) throws ConnectionException, WrongCredentialsException, NeedCaptchaException, Fa2Exception, NeedFa2CallReset {
        Response<Auth> auth;
        try {
            auth = authClient.getApi(AuthHttpApi.class).SignInWithCaptcha(
                    Integer.parseInt(ApiCredentialsKt.CLIENT_ID_VK),
                    ApiCredentialsKt.CLIENT_SECRET_VK,
                    login,
                    password,
                    captchaKey,
                    captchaSid
            ).execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new ConnectionException();
        }
        checkAuthErrors(auth);
        Auth response = Objects.requireNonNull(auth.body());
        return new VkSession(response.getUserId(), response.getAccessToken());
    }

    public VkSession signInWithCode(@NotNull String login, @NotNull String password, @NotNull String code) throws ConnectionException, NeedCaptchaException, Fa2Exception, WrongCredentialsException, NeedFa2CallReset {
        Response<Auth> auth;
        try {
            auth = authClient.getApi(AuthHttpApi.class).SignInWithCode(
                    Integer.parseInt(ApiCredentialsKt.CLIENT_ID_VK),
                    ApiCredentialsKt.CLIENT_SECRET_VK,
                    login,
                    password,
                    code
            ).execute();
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new ConnectionException();
        }
        checkAuthErrors(auth);
        Auth response = Objects.requireNonNull(auth.body());
        return new VkSession(response.getUserId(), response.getAccessToken());
    }



    @NotNull
    @Override
    public String getNetworkName() {
        return NAME;
    }

    @Override
    public int getNetworkId() {
        return Network.DefaultImpls.getNetworkId(this);
    }

    public class VkSession extends Session {
        private final int userId;
        private final String token;
        private final VkApiClient client = new VkApiClient(new HttpTransportClient());

        private VkSession(int userId, String token) {
            super(VkNetwork.this);
            this.userId = userId;
            this.token = token;
        }

        @Override
        public int getSessionId() {
            return userId;
        }

        @NotNull
        @Override
        public String getSessionName() {
            return "unknown"; // TODO implement
        }
    }
}
