package com.example.familymap.network;

import android.os.AsyncTask;

import com.example.familymap.model.Client;
import com.example.familymap.model.Filter;
import com.example.shared.model_.Event;
import com.example.shared.model_.Person;
import com.example.shared.response_.PersonIDResponse;
import com.example.shared.response_.PersonResponse;
import com.example.shared.response_.EventResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataSyncTask extends AsyncTask<String, Void, Boolean> {
    private String serverHost;
    private String serverPort;
    private Client client;
    private Filter filter;

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

            client = Client.getInstance();
            filter = new Filter();

            // Connect with server through proxy
            // Get user Person information
            PersonIDResponse personIDResponse = Proxy.getPersonID(serverHost, serverPort, personID, authToken);

            createPerson(personIDResponse);         // Create new person for login session

            // Get all family information
            PersonResponse personResponse = Proxy.dataSyncPersons(serverHost, serverPort, authToken);
            EventResponse eventResponse = Proxy.dataSyncEvents(serverHost, serverPort, authToken);

            setFamilyPersonsInfo(personResponse);   // Set family persons info
            setFamilyEventsInfo(eventResponse);     // Set family events info

            // Data synced successfully
            return true;
        }
        catch (Exception e){
            // Data did not sync correctly
            e.printStackTrace();
            return false;
        }
    }

    // Create new person
    private void createPerson(PersonIDResponse personIDResponse) throws Exception {
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
                person.setMotherID(personIDResponse.getMotherID());
            }
            if (personIDResponse.getSpouseID() != null) {
                person.setSpouseID(personIDResponse.getSpouseID());
            }
            client.setPerson(person);
            System.out.println("Person set in client instance");
        }
        else {
            throw new Exception("PersonIDResponse failed");
        }
    }

    // Set family persons info
    private void setFamilyPersonsInfo(PersonResponse personResponse) throws Exception {
        if (personResponse != null && personResponse.getSuccess()) {
            Person[] family = personResponse.getData();
            client.setUsersFamily(family);      // Store the list of family members

            // Store a map from each person's ID to their Person object
            HashMap<String, Person> familyDict = new HashMap<String, Person>();
            for (Person person : family) {
                familyDict.put(person.getPersonID(), person);
            }
            client.setUserFamilyDict(familyDict);
            client.setFatherSidePersonIDSet(filter.filterFatherSidePersonIDSet());  // For filtering later
            client.setMotherSidePersonIDSet(filter.filterMotherSidePersonIDSet());  // For filtering later
            System.out.println("Family persons synced");
        }
        else {
            throw new Exception("PersonResponse failed");
        }
    }

    // Set family events info
    private void setFamilyEventsInfo(EventResponse eventResponse) throws Exception {
        if (eventResponse != null && eventResponse.getSuccess()) {
            Event[] events = eventResponse.getData();
            client.setFamilyEvents(events);                         // Store the list of family events

            // Store a map from each person's ID to a list of their Event objects
            String[] colors = Client.getColors();
            HashMap<String, List<Event>> eventDict = new HashMap<String, List<Event>>();
            List<Event> allEventsArray = new ArrayList<Event>();        // For filtering later
            int counter = 0;

            for (Event event : events) {
                String eventPersonID = event.getPersonID();
                List<Event> eventList = new ArrayList<Event>();

                // Add event to list of events for person ID
                if (eventDict.containsKey(eventPersonID)) {
                    eventList = eventDict.get(eventPersonID);
                }
                eventList.add(event);
                eventDict.put(eventPersonID, eventList);
                allEventsArray.add(event);                         // For filtering later

                // Add event type to color dictionary if necessary
                if (!client.getEventColors().containsKey(event.getEventType().toLowerCase())) {
                    client.getEventColors().put(event.getEventType().toLowerCase(), Float.valueOf(colors[counter % colors.length]));
                }
                counter++;
            }
            client.setFilteredFamilyEvents(allEventsArray);        // For filtering later
            client.setFilteredEventDict(eventDict);                // For filtering later
            client.setUserEventDict(eventDict);
            System.out.println("Family events synced");
        }
        else {
            throw new Exception("EventResponse failed");
        }
    }

    @Override
    protected void onPostExecute(Boolean syncSuccess) {
        listener.dataSyncTaskComplete(syncSuccess);
    }
}
