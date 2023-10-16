package com.vl.cluster.api.network.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vl.cluster.api.definition.Network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


public class VkNetwork implements Network {
    private static final String NAME = "ВКонтакте";

    private final VkApiClient vk = new VkApiClient(new HttpTransportClient());

    @NotNull
    @Override
    public Set<LoginType> getLoginVariants() {
        return Set.of(LoginType.PHONE);
    }

    @NotNull
    @Override
    public AuthType getAuthType() {
        return AuthType.PASSWORD;
    }

    @Nullable
    @Override
    public String signIn(@NotNull String login, @NotNull String password) {
        return null; // TODO implement
    }

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getId() {
        return Network.DefaultImpls.getId(this);
    }
}
