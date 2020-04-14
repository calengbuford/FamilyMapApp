package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.request_.LoginRequest;
import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.RegisterResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class registerTest {
    private String serverHost = "10.0.2.2";
    private String serverPort = "8080";
    private String randomUseName;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {

    }

    @Test
    public void registerPass() throws Exception {
        // Register a new user to add to the database
        Random rand = new Random();
        randomUseName = Integer.toString(rand.nextInt(100000000));
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
        randomUseName = Integer.toString(rand.nextInt(100000000));
        RegisterRequest request = new RegisterRequest(randomUseName, "pass",
                "email", "Troy", "Whitewolf", "m");
        RegisterResponse response = Proxy.register(serverHost, serverPort, request);
        assertNotNull(response);
        assertTrue(response.getSuccess());

        // Try to register them again
        request = new RegisterRequest(randomUseName, "pass",
                "email", "Troy", "Whitewolf", "m");
        response = Proxy.register(serverHost, serverPort, request);

        assertNotNull(response);
        assertFalse(response.getSuccess());
    }

}
