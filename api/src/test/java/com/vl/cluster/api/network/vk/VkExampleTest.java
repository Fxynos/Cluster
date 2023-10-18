package com.vl.cluster.api.network.vk;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.vl.cluster.api.definition.features.NetworkAuth;

import org.junit.jupiter.api.Test;

public class VkExampleTest {
    private final VkNetwork network = new VkNetwork();

    @Test
    public void testAuthWithIncorrectCredentials() {
         assertThrows(
                 NetworkAuth.Password.WrongCredentialsException.class,
                 () -> network.signIn("login", "password")
         );
    }
}
