package com.example.familymap.network;

import android.content.Context;
import android.os.AsyncTask;

import com.example.shared.request_.LoginRequest;
import com.example.shared.response_.LoginResponse;

public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
    private Proxy proxy;
    private Context context;

    public LoginTask(String serverHost, String serverPort) {
        proxy = new Proxy(serverHost, serverPort);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected LoginResponse doInBackground(LoginRequest... loginRequests) {



        return null;
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse) {
        // call loginComplete(loginResponse);
    }
}
