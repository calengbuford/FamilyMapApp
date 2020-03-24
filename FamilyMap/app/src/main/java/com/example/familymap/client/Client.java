package com.example.familymap.client;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;
import com.example.shared.model_.User;

public class Client {
    private static Client instance;

    private User user;
    private Person person;
    private Event[] familyEvents;
    private Person[] usersFamily;

    private Client() {
        user = null;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
