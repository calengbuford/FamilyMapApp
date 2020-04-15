package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.request_.LoginRequest;
import com.example.shared.response_.LoginResponse;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test a successful login with the loaded data. Test unsuccessful logins with
 * a bad username and then with a bad password.
 */
public class LoginTest {
    private String serverHost = "192.168.0.103";
    private String serverPort = "8080";

    @Test
    public void loginPass() {
        // Test that the person can log in
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNotNull(response);
        assertTrue(response.getSuccess());
    }

    @Test
    public void loginBadUsernameFail() {
        // Test that the username entered is wrong
        LoginRequest request = new LoginRequest("badUsername", "parker");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNull(response);   // Indicates a bad request
    }

    @Test
    public void loginBadPasswordFail() {
        // Test the password entered is wrong
        LoginRequest request = new LoginRequest("sheila", "badPass");
        LoginResponse response = Proxy.login(serverHost, serverPort, request);

        assertNull(response);   // Indicates a bad request
    }
}