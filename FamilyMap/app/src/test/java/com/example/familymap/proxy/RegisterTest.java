package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.RegisterResponse;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test a successful register with the loaded data. Test an unsuccessful register
 * with a username that is already in the database.
 */
public class RegisterTest {
    private String serverHost = "192.168.0.103";
    private String serverPort = "8080";

    @Test
    public void registerPass() throws Exception {
        // Register a new user to add to the database
        Random rand = new Random();
        String randomUseName = Integer.toString(rand.nextInt(100000000));
        RegisterRequest request = new RegisterRequest(randomUseName, "pass",
                "email", "Troy", "Whitewolf", "m");
        RegisterResponse response = Proxy.register(serverHost, serverPort, request);

        assertNotNull(response);
        assertTrue(response.getSuccess());
    }

    @Test
    public void registerUserAlreadyExistsFail() throws Exception {
        // Test that the user is already in the database

        // Register a random user, confirm that it worked
        Random rand = new Random();
        RegisterRequest request = new RegisterRequest("sheila", "parker",
                "email", "Sheila", "Parker", "f");
        RegisterResponse response = Proxy.register(serverHost, serverPort, request);

        assertNull(response);   // Indicates a bad request
    }
}