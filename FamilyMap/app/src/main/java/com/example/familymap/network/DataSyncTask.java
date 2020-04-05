package com.example.familymap.network;

import android.os.AsyncTask;

import com.example.familymap.client.Client;
import com.example.shared.model_.Person;
import com.example.shared.model_.User;
import com.example.shared.response_.PersonIDResponse;
import com.example.shared.response_.PersonResponse;
import com.example.shared.response_.EventResponse;
import com.example.shared.response_.RegisterResponse;

public class DataSyncTask extends AsyncTask<String, Void, Boolean> {
    private String serverHost;
    private String serverPort;

    public interface Listener {
        void onError(Error e);
        void dataSyncTaskComplete(Boolean syncSuccess);
    }

    private Listener listener;

    // Constructor for DataSyncTask
    public DataSyncTask(Listener listener, String serverHost, String serverPort) {
        this.listener = listener;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    protected Boolean doInBackground(String... userInfo) {
        try {
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
                System.out.println("Person set in client instance");
            }
            else {
                throw new Exception("PersonIDResponse failed");
            }

            // Get all family information
            PersonResponse personResponse = Proxy.dataSyncPersons(serverHost, serverPort, authToken);
            EventResponse eventResponse = Proxy.dataSyncEvents(serverHost, serverPort, authToken);

            // Set family persons info
            if (personResponse != null && personResponse.getSuccess()) {
                client.setUsersFamily(personResponse.getData());
                System.out.println("Family persons synced");
            }
            else {
                throw new Exception("PersonResponse failed");
            }

            // Set family events info
            if (eventResponse != null && eventResponse.getSuccess()) {
                client.setFamilyEvents(eventResponse.getData());
                System.out.println("Family events synced");
            }
            else {
                throw new Exception("EventResponse failed");
            }

            // Data synced successfully
            return true;
        }
        catch (Exception e){
            // Data did not sync correctly
            System.out.println("ERROR: " + e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean syncSuccess) {
        listener.dataSyncTaskComplete(syncSuccess);
    }
}
