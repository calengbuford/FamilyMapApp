package com.example.familymap.network;

import android.content.Context;
import android.os.AsyncTask;

import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.RegisterResponse;

public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {
    private Proxy proxy;
    private Context context;

    public RegisterTask(String serverHost, String serverPort) {
        proxy = new Proxy(serverHost, serverPort);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected RegisterResponse doInBackground(RegisterRequest... registerRequests) {



        return null;
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {
        // call registerComplete(registerResponse);
    }
}
