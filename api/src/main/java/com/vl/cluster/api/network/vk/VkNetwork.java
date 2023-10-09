package com.vl.cluster.api.network.vk;

import com.vl.cluster.api.definition.Network;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class VkNetwork implements Network {
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
        return null;
    }
}
