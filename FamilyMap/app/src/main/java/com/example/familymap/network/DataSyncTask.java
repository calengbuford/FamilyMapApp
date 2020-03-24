package com.example.familymap.network;

import android.os.AsyncTask;

import com.example.familymap.client.Client;
import com.example.shared.model_.Person;
import com.example.shared.model_.User;
import com.example.shared.response_.PersonIDResponse;
import com.example.shared.response_.PersonResponse;
import com.example.shared.response_.EventResponse;

public class DataSyncTask extends AsyncTask<String, Void, Void> {
    private String serverHost;
    private String serverPort;

//    public interface Listener {
//        void onError(Error e);
//        void dataSyncTaskComplete(PersonResponse personResponse);
//    }
//
//    private Listener listener;

    // Constructor for DataSyncTask
//    public DataSyncTask(Listener listener, String serverHost, String serverPort) {
//        this.listener = listener;
    public DataSyncTask(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected Void doInBackground(String... userInfo) {
        if (userInfo.length < 2) {
            return null;
        }

        String authToken = userInfo[0];
        String personID = userInfo[1];

        Client client = Client.getInstance();

        // Connect with server through proxy
        // Get user Person information
        PersonIDResponse personIDResponse = Proxy.getPersonID(serverHost, serverPort, personID, authToken);

        // Create new person
        if (personIDResponse != null && personIDResponse.getSuccess()) {
            Person person = new Person();
            person.setAssociatedUsername(personIDResponse.getAssociatedUsername());
            person.setPersonID(personIDResponse.getPersonID());
            person.setFirstName(personIDResponse.getFirstName());
            person.setLastName(personIDResponse.getLastName());
            person.setGender(personIDResponse.getGender());
            if (personIDResponse.getFatherID() != null) {
                person.setFatherID(personIDResponse.getFatherID());
            }
            if (personIDResponse.getMotherID() != null) {
                person.setFatherID(personIDResponse.getMotherID());
            }
            if (personIDResponse.getSpouseID() != null) {
                person.setFatherID(personIDResponse.getSpouseID());
            }
            client.setPerson(person);
            System.out.println("person added to database");

            // Create user if necessary (only on register)
            if (userInfo.length == 4) {
                String email = userInfo[2];
                String password = userInfo[3];

                // Create User information
                User user = new User(personIDResponse.getAssociatedUsername(), password, email,
                        personIDResponse.getFirstName(), personIDResponse.getLastName(),
                        personIDResponse.getGender(), personID);
                client.setUser(user);
                System.out.println("user added to database");

            }
        }

        // Get all family information
        PersonResponse personResponse = Proxy.dataSyncPersons(serverHost, serverPort, authToken);
        EventResponse eventResponse = Proxy.dataSyncEvents(serverHost, serverPort, authToken);





        return null;
    }
}
