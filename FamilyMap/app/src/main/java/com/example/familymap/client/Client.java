package com.example.familymap.client;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.HashMap;
import java.util.List;

public class Client {
    private static Client instance;

    private Person person;
    private Event[] familyEvents;
    private Person[] usersFamily;
    private HashMap<String, Person> userFamilyDict;
    private HashMap<String, List<Event>> userEventDict;
    private HashMap<String, Float> eventColors = new HashMap<String, Float>();
    static final private String[] colors = { "210.0", "240.0", "180.0", "120.0", "30.0", "0.0", "330.0", "60.0" };

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

    public HashMap<String, Float> getEventColors() {
        return eventColors;
    }

    public void setEventColors(HashMap<String, Float> eventColors) {
        this.eventColors = eventColors;
    }

    public static String[] getColors() {
        return colors;
    }
}
