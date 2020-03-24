package com.example.familymap.network;

import android.os.AsyncTask;

import com.example.shared.request_.LoginRequest;
import com.example.shared.response_.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
    private String serverHost;
    private String serverPort;

    public interface Listener {
        void onError(Error e);
        void loginComplete(LoginResponse loginResponse);
    }

    private Listener listener;

    // Constructor for LoginTask
    public LoginTask(Listener listener, String serverHost, String serverPort) {
        this.listener = listener;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... loginRequests) {
        if (loginRequests.length == 0) {
            return null;
        }
        // Connect with server through proxy
        LoginResponse response = Proxy.login(serverHost, serverPort, loginRequests[0]);
        return response;
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse) {
        listener.loginComplete(loginResponse);
    }
}
