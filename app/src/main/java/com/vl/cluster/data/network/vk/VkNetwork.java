package com.vl.cluster.data.network.vk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vl.cluster.ApiCredentialsKt;
import com.vl.cluster.R;
import com.vl.cluster.data.HttpClient;
import com.vl.cluster.data.network.vk.dto.AuthErrorResponse;
import com.vl.cluster.domain.boundary.SessionStore;
import com.vl.cluster.domain.entity.ChatDialog;
import com.vl.cluster.domain.entity.ChatMessage;
import com.vl.cluster.domain.entity.Comment;
import com.vl.cluster.domain.entity.Dialog;
import com.vl.cluster.domain.entity.LoginType;
import com.vl.cluster.domain.entity.Page;
import com.vl.cluster.domain.entity.PrivateDialog;
import com.vl.cluster.domain.entity.PrivateMessage;
import com.vl.cluster.domain.exception.ApiCustomException;
import com.vl.cluster.domain.exception.CaptchaException;
import com.vl.cluster.domain.exception.ConnectionException;
import com.vl.cluster.domain.boundary.Network;
import com.vl.cluster.domain.boundary.Session;
import com.vl.cluster.domain.exception.TwoFaException;
import com.vl.cluster.domain.exception.UnsupportedLoginMethodException;
import com.vl.cluster.domain.exception.WrongCredentialsException;
import com.vl.cluster.domain.boundary.Messenger;
import com.vl.cluster.domain.boundary.NetworkAuth;
import com.vl.cluster.domain.boundary.Newsfeed;
import com.vl.cluster.domain.entity.Post;
import com.vl.cluster.domain.entity.Profile;
import com.vl.cluster.data.network.vk.dto.AuthSuccessResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class VkNetwork implements Network, NetworkAuth.PasswordAuth {
    private static final String
            NAME = "ВКонтакте",
            ERROR_INVALID_CLIENT = "invalid_client",
            ERROR_NEED_CAPTCHA = "need_captcha",
            ERROR_NEED_VALIDATION = "need_validation",
            TFA_SMS = "2fa_sms",
            TFA_APP = "2fa_app";

    private final HttpClient authClient = new HttpClient("https://oauth.vk.com");
    private final VkSessionStore sessionStore;

    public VkNetwork(Context context) {
        sessionStore = new VkSessionStore(context, this);
    }

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
    public Session signIn(@NotNull String login, @NotNull String password) throws
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
                case ERROR_INVALID_CLIENT -> throw new WrongCredentialsException();
                case ERROR_NEED_CAPTCHA -> throw new CaptchaException( // TODO [tva] refactor
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
                                if (capError.getErrorName().equals(ERROR_INVALID_CLIENT))
                                    throw new WrongCredentialsException();
                                else
                                    throw new ApiCustomException(
                                            capError.getErrorName(),
                                            capError.getErrorDescription() != null ?
                                                    capError.getErrorDescription() :
                                                    ""
                                    );
                            }
                            AuthSuccessResponse capResponse =
                                    Objects.requireNonNull(capAuth.body());
                            return new VkSession(capResponse.getUserId(), capResponse.getAccessToken());
                        }
                    );
                case ERROR_NEED_VALIDATION -> {
                    switch (error.getValidationType()) {
                        case TFA_SMS, TFA_APP -> throw new TwoFaException(
                                error.getValidationType().equals(TFA_SMS) ?
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
        var sessions = new HashSet<>(sessionStore.getSessions());
        var session = new VkSession(response.getUserId(), response.getAccessToken());
        sessions.add(session);
        sessionStore.updateSessions(sessions);
        return session;
    }

    @NotNull
    @Override
    public String getNetworkName() {
        return NAME;
    }

    @Override
    public int getNetworkId() {
        return getNetworkName().hashCode();
    }

    @Override
    public int getIcon() {
        return R.drawable.vk;
    }

    @NonNull
    @Override
    public SessionStore getSessionStore() {
        return sessionStore;
    }

    public class VkSession extends Session {
        private final VkApiClient client = new VkApiClient(new HttpTransportClient());
        private final UserActor user;
        private final VkNewsfeed newsfeed;

        public VkSession(int userId, String token) {
            user = new UserActor(userId, token);
            newsfeed = new VkNewsfeed(this);
        }

        public VkApiClient getClient() {
            return client;
        }

        public UserActor getUser() {
            return user;
        }

        /* Session */

        @Override
        public int getSessionId() {
            return user.getId();
        }

        @NonNull
        @Override
        public String getSessionName() {
            return user.getPhone();
        }

        @NonNull
        @Override
        public Network getNetwork() {
            return VkNetwork.this;
        }

        @NonNull
        @Override
        public Newsfeed getNewsfeed() {
            return newsfeed;
        }

        @NonNull
        @Override
        public Messenger getMessenger() {
            return null;
        }
    }
}
