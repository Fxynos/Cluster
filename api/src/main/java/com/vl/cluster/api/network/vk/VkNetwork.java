package com.vl.cluster.api.network.vk;

import com.google.gson.Gson;
import com.vk.api.sdk.client.VkApiClient;
import com.vl.cluster.api.ApiCredentialsKt;
import com.vl.cluster.api.HttpClient;
import com.vl.cluster.api.definition.exception.ApiCustomException;
import com.vl.cluster.api.definition.exception.CaptchaException;
import com.vl.cluster.api.definition.exception.ConnectionException;
import com.vl.cluster.api.definition.Network;
import com.vl.cluster.api.definition.Session;
import com.vl.cluster.api.definition.exception.TwoFaException;
import com.vl.cluster.api.definition.exception.UnsupportedLoginMethodException;
import com.vl.cluster.api.definition.features.NetworkAuth;
import com.vl.cluster.api.network.vk.dto.AuthErrorResponse;
import com.vl.cluster.api.network.vk.dto.AuthSuccessResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class VkNetwork implements
        Network<VkNetwork.VkSession>,
        NetworkAuth.Password<VkNetwork.VkSession>
{
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

    @NotNull
    @Override
    public VkSession signIn(@NotNull String login, @NotNull String password) throws
            WrongCredentialsException,
            ConnectionException,
            TwoFaException,
            CaptchaException,
            UnsupportedLoginMethodException
    {
        Response<AuthSuccessResponse> auth;
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
        /* Handling errors */
        if (!auth.isSuccessful()) try (ResponseBody response = Objects.requireNonNull(auth.errorBody())) {
            final AuthErrorResponse error = new Gson().fromJson(
                    response.charStream(),
                    AuthErrorResponse.class
            );
            switch (error.getErrorName()) {
                case "invalid_client" -> throw new WrongCredentialsException();
                case "need_captcha" -> throw new CaptchaException(
                        error.getCaptchaSid(),
                        error.getCaptchaImg(),
                        captcha -> { // on captcha confirmation
                            Response<AuthSuccessResponse> capAuth;
                            try {
                                capAuth = authClient.getApi(AuthHttpApi.class).signInWithCaptcha(
                                        Integer.parseInt(ApiCredentialsKt.CLIENT_ID_VK),
                                        ApiCredentialsKt.CLIENT_SECRET_VK,
                                        login,
                                        password,
                                        captcha,
                                        error.getCaptchaSid()
                                ).execute();
                            } catch (IOException exception) {
                                exception.printStackTrace();
                                throw new ConnectionException();
                            }
                            if (!capAuth.isSuccessful()) try (ResponseBody capResponse = Objects.requireNonNull(capAuth.errorBody())) {
                                final AuthErrorResponse capError = new Gson().fromJson(
                                        capResponse.charStream(),
                                        AuthErrorResponse.class
                                );
                                throw new ApiCustomException(
                                        capError.getErrorName(),
                                        capError.getErrorDescription()
                                ); // TODO check if password or captcha are wrong
                            }
                            AuthSuccessResponse capResponse =
                                    Objects.requireNonNull(capAuth.body());
                            return new VkSession(capResponse.getUserId(), capResponse.getAccessToken());
                        }
                    );
                case "need_validation" -> {
                    switch (error.getValidationType()) {
                        case "2fa_sms", "2fa_app" -> throw new TwoFaException(
                                error.getValidationType().equals("2fa_sms") ?
                                        TwoFaException.CodeSource.SMS :
                                        TwoFaException.CodeSource.APP,
                                code -> {
                                    Response<AuthSuccessResponse> codeAuth;
                                    try {
                                        codeAuth = authClient.getApi(AuthHttpApi.class)
                                            .signInWithCode(
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
                                    if (!codeAuth.isSuccessful())
                                        throw new WrongCredentialsException();
                                    AuthSuccessResponse codeResponse =
                                            Objects.requireNonNull(codeAuth.body());
                                    return new VkSession(
                                            codeResponse.getUserId(),
                                            codeResponse.getAccessToken()
                                    );
                                }
                        );
                        default -> throw new UnsupportedLoginMethodException(
                                error.getValidationType()
                        );
                    }
                }
                default -> throw new ApiCustomException(
                        error.getErrorName(),
                        error.getErrorDescription()
                );
            }
        }
        /* Success */
        AuthSuccessResponse response = Objects.requireNonNull(auth.body());
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
