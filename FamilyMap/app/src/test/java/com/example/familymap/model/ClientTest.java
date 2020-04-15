package com.example.familymap.model;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    @Test
    public void chronologicalLifeEventsNonEmptyPass() {
        // Create related people to plug into functions
        Person person1 = new Person("username", "jimmy", "lastName",
                "m", "fatherID", "motherID", "spouseID");

        // Create person events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "death", 2020);
        // Create family events
        Event event2 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "AAA", 1740);
        // Create family events
        Event event3 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "noType", 1900);
        Event event4 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "death", 2035);
        Event event5 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "birth", 1492);
        Event event6 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "BBB", 1740);

        // Create family events
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        HashMap<String, List<Event>> userEventDict = new HashMap<String, List<Event>>();
        userEventDict.put(person1.getPersonID(), events);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setFilteredEventDict(userEventDict);

        // Sort events
        List<Event> personEvents = client.getLifeEvents(person1.getPersonID());

        // Verify sorting
        assertNotNull(personEvents);
        assertEquals(6, personEvents.size());
        assertEquals("birth", personEvents.get(0).getEventType());
        assertEquals("AAA", personEvents.get(1).getEventType());
        assertEquals("BBB", personEvents.get(2).getEventType());
        assertEquals("noType", personEvents.get(3).getEventType());
        assertEquals("death", personEvents.get(4).getEventType());
        assertEquals("death", personEvents.get(5).getEventType());
        assertEquals(2020, personEvents.get(4).getYear());
        assertEquals(2035, personEvents.get(5).getYear());
        client.reset();
    }

    @Test
    public void chronologicalLifeEventsEmptyPass() {
        // Create related people to plug into functions
        Person person1 = new Person("username", "jimmy", "lastName",
                "m", "fatherID", "motherID", "spouseID");

        // Create family events
        HashMap<String, List<Event>> userEventDict = new HashMap<String, List<Event>>();
        userEventDict.put(person1.getPersonID(), new ArrayList<Event>());

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setFilteredEventDict(userEventDict);

        // Sort events
        List<Event> personEvents = client.getLifeEvents(person1.getPersonID());

        // Verify sorting
        assertNotNull(personEvents);
        assertEquals(0, personEvents.size());
        client.reset();
    }

    @Test
    public void chronologicalLifeEventsBadIDPass() {
        // Create related people to plug into functions
        Person person1 = new Person("username", "jimmy", "lastName",
                "m", "fatherID", "motherID", "spouseID");

        // Create family events
        HashMap<String, List<Event>> userEventDict = new HashMap<String, List<Event>>();
        userEventDict.put(person1.getPersonID(), new ArrayList<Event>());

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setFilteredEventDict(userEventDict);

        // Sort events
        List<Event> personEvents = client.getLifeEvents("badPersonID");

        // Verify sorting
        assertNotNull(personEvents);
        assertEquals(0, personEvents.size());
        client.reset();
    }

    @Test
    public void searchForPersonByIDPass() {
        // Create user family dict
        Person person1 = new Person("username", "greg", "lastName",
                "m", "fatherID", "motherID", "spouseID");
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Retrieve person from client
        Person person = client.getUserFamilyDict().get(person1.getPersonID());

        assertNotNull(person);
        assertEquals(person.getPersonID(), person1.getPersonID());
        client.reset();
    }

    @Test
    public void searchForPersonByIDFail() {
        // Create user family dict
        Person person1 = new Person("username", "greg", "lastName",
                "m", "fatherID", "motherID", "spouseID");
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Retrieve person from client
        Person person = client.getUserFamilyDict().get("badPersonID");

        assertNull(person);
        client.reset();
    }

    @Test
    public void searchForEventsByPersonIDPass() {
        // Create related people to plug into functions
        Person person1 = new Person("username", "jimmy", "lastName",
                "m", "fatherID", "motherID", "spouseID");

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        HashMap<String, List<Event>> userEventDict = new HashMap<String, List<Event>>();
        userEventDict.put(person1.getPersonID(), events);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserEventDict(userEventDict);

        List<Event> personEvents = client.getUserEventDict().get(person1.getPersonID());

        assertNotNull(personEvents);
        assertEquals(1, personEvents.size());
        client.reset();
    }

    @Test
    public void searchForEventsByPersonIDFail() {
        // Create related people to plug into functions
        Person person1 = new Person("username", "jimmy", "lastName",
                "m", "fatherID", "motherID", "spouseID");

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        HashMap<String, List<Event>> userEventDict = new HashMap<String, List<Event>>();
        userEventDict.put(person1.getPersonID(), events);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserEventDict(userEventDict);

        List<Event> personEvents = client.getUserEventDict().get("badPersonID");

        assertNull(personEvents);
        client.reset();
    }

}
