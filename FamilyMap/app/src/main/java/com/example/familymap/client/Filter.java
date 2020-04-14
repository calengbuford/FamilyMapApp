package com.example.familymap.client;

import com.example.familymap.activities.SettingsActivity;
import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Filter {
    private Client client = Client.getInstance();;
    private Settings settings;

    public List<Person> getPersonsChildren(Person[] family, String personID) {
        List<Person> children = new ArrayList<Person>();

        // Check every member of the family to find children
        for (Person person : family) {
            if (personID.equals(person.getMotherID()) || personID.equals(person.getFatherID())) {
                children.add(person);
            }
        }
        return children;
    }

    private void parentSideHelper(HashSet<String> set, String personID) {
        set.add(personID);  // Add person's ID to set

        // Add parents' IDs of current person
        Person person = client.getUserFamilyDict().get(personID);
        if (person != null && person.getFatherID() != null) {
            parentSideHelper(set, person.getFatherID());
        }
        if (person != null && person.getMotherID() != null) {
            parentSideHelper(set, person.getMotherID());
        }
    }

    public HashSet<String> filterFatherSidePersonIDSet() {
        HashSet<String> personIDSet = new HashSet<String>();

        if (client.getPerson() != null && client.getPerson().getFatherID() != null) {
            parentSideHelper(personIDSet, client.getPerson().getFatherID());
        }
        return personIDSet;
    }

    public HashSet<String> filterMotherSidePersonIDSet() {
        HashSet<String> personIDSet = new HashSet<String>();

        if (client.getPerson() != null && client.getPerson().getMotherID() != null) {
            parentSideHelper(personIDSet, client.getPerson().getMotherID());
        }
        return personIDSet;
    }

    public void filterEvents(SettingsActivity settingsActivity) {
        settings = Settings.getInstance();
        client = Client.getInstance();
        HashMap<String, Person> userFamilyDict = client.getUserFamilyDict();
        HashSet<String> fatherSidePersonIDSet = client.getFatherSidePersonIDSet();
        HashSet<String> motherSidePersonIDSet = client.getMotherSidePersonIDSet();
        Event[] familyEvents = client.getFamilyEvents();

        // Initialize the filtered set
        HashSet<Event> filteredFamilyEventsSet = new HashSet<Event>();

        // Add events to filteredFamilyEvents if respective switch is checked
        for (Event event : familyEvents) {
            boolean addToSet = true;

            if (!settings.getMaleEventSwitchStatus()) {
                if ("m".equals(userFamilyDict.get(event.getPersonID()).getGender())) {
                    addToSet = false;
                }
            }
            if (!settings.getFemaleEventSwitchStatus()) {
                if ("f".equals(userFamilyDict.get(event.getPersonID()).getGender())) {
                    addToSet = false;
                }
            }
            if (!settings.getFatherSideSwitchStatus()) {
                if (fatherSidePersonIDSet != null && fatherSidePersonIDSet.contains(event.getPersonID())) {
                    addToSet = false;
                }
            }
            if (!settings.getMotherSideSwitchStatus()) {
                if (motherSidePersonIDSet != null && motherSidePersonIDSet.contains(event.getPersonID())) {
                    addToSet = false;
                }
            }

            // Add to set only if the event passed all checks
            if (addToSet) {
                filteredFamilyEventsSet.add(event);
            }
        }

        // Set the filtered list of events in client
        client.setFilteredFamilyEvents(new ArrayList<Event>(filteredFamilyEventsSet));
        setFilteredEventDict(client.getFilteredFamilyEvents());
        System.out.println("LIST SIZE: " + filteredFamilyEventsSet.size());
    }

    private void setFilteredEventDict(List<Event> events) {
        HashMap<String, List<Event>> eventDict = new HashMap<String, List<Event>>();

        for (Event event : events) {
            String eventPersonID = event.getPersonID();
            List<Event> eventList = new ArrayList<Event>();

            // Add event to list of events for person ID
            if (eventDict.containsKey(eventPersonID)) {
                eventList = eventDict.get(eventPersonID);
            }
            eventList.add(event);
            eventDict.put(eventPersonID, eventList);
        }
        client.setFilteredEventDict(eventDict);
    }
}
