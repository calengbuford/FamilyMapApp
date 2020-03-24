package com.example.familymap.network;

import android.os.AsyncTask;

import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.RegisterResponse;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {
    private String serverHost;
    private String serverPort;
    private String email;
    private String password;

    public interface Listener {
        void onError(Error e);
        void registerComplete(RegisterResponse registerResponse);
    }

    private Listener listener;

    // Constructor for RegisterTask
    public RegisterTask(Listener listener, String serverHost, String serverPort) {
        this.listener = listener;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {
        if (registerRequests.length == 0) {
            return null;
        }
        // Connect with server through proxy
        email = registerRequests[0].getEmail();
        password = registerRequests[0].getPassword();
        RegisterResponse response = Proxy.register(serverHost, serverPort, registerRequests[0]);
        return response;
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {
        if (registerResponse != null) {
            DataSyncTask dataSyncTask = new DataSyncTask(serverHost, serverPort);
            dataSyncTask.execute(registerResponse.getAuthToken(), registerResponse.getPersonID(),
                    email, password);
        }
        listener.registerComplete(registerResponse);
    }
}
