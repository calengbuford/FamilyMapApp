package com.example.familymap.network;

import com.example.shared.request_.LoginRequest;
import com.example.shared.request_.RegisterRequest;
import com.example.shared.request_.PersonRequest;
import com.example.shared.response_.EventIDResponse;
import com.example.shared.response_.EventResponse;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.PersonIDResponse;
import com.example.shared.response_.RegisterResponse;
import com.example.shared.response_.PersonResponse;
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

    public static PersonIDResponse getPersonID(String serverHost, String serverPort, String personID, String authToken) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + personID);

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // Indicate that request will not contain an HTTP request body
            http.addRequestProperty("Authorization", authToken);   // Add an auth token to the request header
            http.connect();             // Connect to the server and send the HTTP request

            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Display the JSON data returned from the server
                System.out.println(respData);

                // Call the personID service and get the response
                PersonIDResponse response = gson.fromJson(respData, PersonIDResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static PersonResponse dataSyncPersons(String serverHost, String serverPort, String authToken) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // Indicate that request will not contain an HTTP request body
            http.addRequestProperty("Authorization", authToken);   // Add an auth token to the request header
            http.connect();             // Connect to the server and send the HTTP request


            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Display the JSON data returned from the server
                System.out.println(respData);

                // Call the persons service and get the response
                PersonResponse response = gson.fromJson(respData, PersonResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static EventIDResponse getEventID(String serverHost, String serverPort, String eventID, String authToken) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/" + eventID);

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // Indicate that request will not contain an HTTP request body
            http.addRequestProperty("Authorization", authToken);   // Add an auth token to the request header
            http.connect();             // Connect to the server and send the HTTP request

            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Display the JSON data returned from the server
                System.out.println(respData);

                // Call the eventID service and get the response
                EventIDResponse response = gson.fromJson(respData, EventIDResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    public static EventResponse dataSyncEvents(String serverHost, String serverPort, String authToken) {
        Gson gson = new Gson();

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            // Start constructing HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);    // Indicate that request will not contain an HTTP request body
            http.addRequestProperty("Authorization", authToken);   // Add an auth token to the request header
            http.connect();             // Connect to the server and send the HTTP request


            // Check that the HTTP response from the server contains a 200 status code
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read JSON string from the input stream
                InputStream respJson = http.getInputStream();
                String respData = readString(respJson);

                // Display the JSON data returned from the server
                System.out.println(respData);

                // Call the events service and get the response
                EventResponse response = gson.fromJson(respData, EventResponse.class);
                return response;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
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