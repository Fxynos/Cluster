package com.vl.cluster.api.network.vk;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.vl.cluster.api.ApiCredentialsKt;

import org.junit.jupiter.api.Test;

public class VkExampleTest {
    private final VkNetwork network = new VkNetwork();

    @Test
    public void testAuthWithIncorrectCredentials() {
         assertNull(network.signIn("login", "password"));
    }
    @Test
    public void testNewsFeedIsWorking(){
        assertNull(network.getNewsFeed());
    }
}
