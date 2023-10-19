package com.vl.cluster.api.network.vk;

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

    @NotNull
    @Override
    public VkSession signIn(@NotNull String login, @NotNull String password)
            throws WrongCredentialsException, ConnectionException {
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
        if (!auth.isSuccessful())
            throw new ConnectionException();
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
