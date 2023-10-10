package com.vl.cluster.api.network.vk;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class VkExampleTest {
    private final VkNetwork network = new VkNetwork();

    @Test
    public void testAuthWithIncorrectCredentials() {
        assertNull(network.signIn("890000000000", "password"));
    }
}
