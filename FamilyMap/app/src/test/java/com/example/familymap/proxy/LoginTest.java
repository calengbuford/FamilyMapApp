package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.request_.LoginRequest;
import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.RegisterResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private String serverHost = "10.0.2.2";
    private String serverPort = "8080";

    @BeforeEach
    public void setUp() {
        // Register a user. If they already exist in database, that is fine.
        RegisterRequest registerRequest = new RegisterRequest("user", "pass",
                "email", "Greg", "Amazon", "m");
        RegisterResponse registerResponse = Proxy.register(serverHost, serverPort, registerRequest);
        assertNotNull(registerRequest);
    }

    @Test
    public void loginPass() {
        // Test that the person can log in
        LoginRequest request = new LoginRequest("user", "pass");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNotNull(response);
        assertTrue(response.getSuccess());
    }

    @Test
    public void loginBadUsernameFail() {
        // Test that the username entered is wrong
        LoginRequest request = new LoginRequest("badUsername", "pass");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNotNull(response);
        assertFalse(response.getSuccess());
    }

    @Test
    public void loginBadPasswordFail() {
        // Test the password entered is wrong
        LoginRequest request = new LoginRequest("user", "badPass");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNotNull(response);
        assertFalse(response.getSuccess());
    }

}
