package com.vl.cluster.api.network.vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.newsfeed.responses.GetResponse;
import com.vk.api.sdk.objects.wall.GetFilter;
import com.vl.cluster.api.ApiCredentialsKt;
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

    public com.vk.api.sdk.objects.newsfeed.responses.GetResponse getNewsFeed() {
        UserActor actor = new UserActor(ApiCredentialsKt.USER_ID, ApiCredentialsKt.ACCESS_TOKEN);
        GetResponse getResponse = null;
        try {
            getResponse = vk.newsfeed().get(actor).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        return getResponse;
    }
}
