package com.example.familymap.client;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Client {
    private static Client instance;
    private Person person;

    private Event[] familyEvents;
    private Person[] usersFamily;
    private HashMap<String, Person> userFamilyDict;
    private HashMap<String, List<Event>> userEventDict;

    private HashSet<String> fatherSidePersonIDSet;
    private HashSet<String> motherSidePersonIDSet;
    private List<Event> filteredFamilyEvents;
    private HashMap<String, List<Event>> filteredEventDict;

    private HashMap<String, Float> eventColors = new HashMap<String, Float>();
    static final private String[] colors = { "210.0", "240.0", "180.0", "120.0", "30.0", "0.0", "330.0", "60.0" };
    private Event curEventViewed;

    private Client() {
        person = null;
        familyEvents = null;
        usersFamily = null;
    }

    // Constructor and getter of instance
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Event[] getFamilyEvents() {
        return familyEvents;
    }

    public void setFamilyEvents(Event[] familyEvents) {
        this.familyEvents = familyEvents;
    }

    public Person[] getUsersFamily() {
        return usersFamily;
    }

    public void setUsersFamily(Person[] usersFamily) {
        this.usersFamily = usersFamily;
    }

    /**
     *  Store a map from each person's ID to their Person object
     */
    public HashMap<String, Person> getUserFamilyDict() {
        return userFamilyDict;
    }

    public void setUserFamilyDict(HashMap<String, Person> userFamilyDict) {
        this.userFamilyDict = userFamilyDict;
    }

    /**
     * Store a map from each person's ID to a list of their Event objects
     */
    public HashMap<String, List<Event>> getUserEventDict() {
        return userEventDict;
    }

    public void setUserEventDict(HashMap<String, List<Event>> userEventDict) {
        this.userEventDict = userEventDict;
    }

    public HashSet<String> getFatherSidePersonIDSet() {
        return fatherSidePersonIDSet;
    }

    public void setFatherSidePersonIDSet(HashSet<String> fatherSidePersonIDSet) {
        this.fatherSidePersonIDSet = fatherSidePersonIDSet;
    }

    public HashSet<String> getMotherSidePersonIDSet() {
        return motherSidePersonIDSet;
    }

    public void setMotherSidePersonIDSet(HashSet<String> motherSidePersonIDSet) {
        this.motherSidePersonIDSet = motherSidePersonIDSet;
    }

    public List<Event> getFilteredFamilyEvents() {
        return filteredFamilyEvents;
    }

    public void setFilteredFamilyEvents(List<Event> filteredFamilyEvents) {
        this.filteredFamilyEvents = filteredFamilyEvents;
    }

    public HashMap<String, List<Event>> getFilteredEventDict() {
        return filteredEventDict;
    }

    public void setFilteredEventDict(HashMap<String, List<Event>> filteredEventDict) {
        this.filteredEventDict = filteredEventDict;
    }

    public HashMap<String, Float> getEventColors() {
        return eventColors;
    }

    public void setEventColors(HashMap<String, Float> eventColors) {
        this.eventColors = eventColors;
    }

    public static String[] getColors() {
        return colors;
    }

    public Event getCurEventViewed() {
        return curEventViewed;
    }

    public void setCurEventViewed(Event curEventViewed) {
        this.curEventViewed = curEventViewed;
    }

    public List<Event> getLifeEvents(String personID) {
        List<Event> events = getFilteredEventDict().get(personID);   // Get events for person

        if (events == null) {
            return new ArrayList<Event>();
        }

        // Sort events by birth, death, year, and alphanumerics
        if (events.size() > 0) {
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    String eType1 = e1.getEventType().toLowerCase();
                    String eType2 = e2.getEventType().toLowerCase();
                    Integer eYear1 = e1.getYear();
                    Integer eYear2 = e2.getYear();
                    String birthStr = "birth";
                    String deathStr = "death";

                    // Set the birth event to be first
                    if (birthStr.equals(eType1)) {
                        if (birthStr.equals(eType2)) {
                            return 0;
                        }
                        return -1;
                    }
                    if (birthStr.equals(eType2)) {
                        return 1;
                    }

                    // Set the death event to be first
                    if (deathStr.equals(eType1)) {
                        if (deathStr.equals(eType2)) {
                            return 0;
                        }
                        return 1;
                    }
                    if (deathStr.equals(eType2)) {
                        return -1;
                    }

                    // Compare the years of the event
                    if (eYear1 < eYear2) {
                        return -1;
                    } else if (eYear1 == eYear2) {
                        if (eType1.compareTo(eType1) < 0) {
                            return -1;
                        }
                        return 0;
                    } else if (eYear1 > eYear2) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return events;
    }

    public void reset() {
        // Call on logout
        instance = null;
    }
}
