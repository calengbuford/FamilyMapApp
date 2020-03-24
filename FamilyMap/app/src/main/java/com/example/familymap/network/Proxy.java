package com.example.familymap.network;

import com.example.shared.request_.LoginRequest;
import com.example.shared.request_.RegisterRequest;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.RegisterResponse;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;

public class Proxy {

    public static LoginResponse login(String serverHost, String serverPort, LoginRequest request) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);     // Indicate that request will contain an HTTP request body
            http.connect();             // Connect to the server and send the HTTP request

            // The JSON string to send in the HTTP request body
            String reqJson = gson.toJson(request);

            // Write the request JSON to the request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqJson, reqBody);

            reqBody.close();

            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Login successful.");

                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Call the login service and get the response
                LoginResponse response = gson.fromJson(respData, LoginResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RegisterResponse register(String serverHost, String serverPort, RegisterRequest request) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);     // Indicate that request will contain an HTTP request body
            http.connect();             // Connect to the server and send the HTTP request

            // The JSON string to send in the HTTP request body
            String reqJson = gson.toJson(request);

            // Write the request JSON to the request body
            OutputStream reqBody = http.getOutputStream();
            writeString(reqJson, reqBody);

            reqBody.close();

            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Register successful.");

                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Call the login service and get the response
                RegisterResponse response = gson.fromJson(respData, RegisterResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        The readString method shows how to read a String from an InputStream.
    */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}