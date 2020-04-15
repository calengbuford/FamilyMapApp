package com.example.familymap.model;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilterTest {

    @Test
    public void calculateFamilyRelationshipsChildrenPass() {
        // Create related people to plug into functions
        Person person2 = new Person("username", "johnny", "lastName",
                "m", "fatherID", "motherID1", "spouseID1");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), "motherID2", "spouseID2");
        List<Person> personsList = new ArrayList<Person>();
        personsList.add(person1);
        personsList.add(person2);
        Person[] persons = new Person[personsList.size()];
        for (int i = 0; i < personsList.size(); i++) {
            persons[i] = personsList.get(i);
        }

        // Get person2's children
        Filter filter = new Filter();
        List<Person> children = filter.getPersonsChildren(persons, person2.getPersonID());

        assertNotNull(children);
        assertEquals(1, children.size());

        // Confirm the child of person2 is person1
        Person child = children.get(0);

        assertEquals(person1.getPersonID(), child.getPersonID());
    }

    @Test
    public void calculateFamilyRelationshipsNoChildrenPass() {
        // Create related people to plug into functions
        Person person2 = new Person("username", "johnny", "lastName",
                "m", "fatherID1", "motherID1", "spouseID1");
        Person person1 = new Person("username", "greg", "lastName",
                "m", "fatherID2", "motherID2", "spouseID2");
        List<Person> personsList = new ArrayList<Person>();
        personsList.add(person1);
        personsList.add(person2);
        Person[] persons = new Person[personsList.size()];
        for (int i = 0; i < personsList.size(); i++) {
            persons[i] = personsList.get(i);
        }

        // Get person2's children
        Filter filter = new Filter();
        List<Person> children = filter.getPersonsChildren(persons, person2.getPersonID());

        assertNotNull(children);
        assertEquals(0, children.size());
    }

    @Test
    public void calculateFamilyRelationshipsFatherSidePass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Get person1's father's side
        Filter filter = new Filter();
        HashSet<String> fatherSide = filter.filterFatherSidePersonIDSet();

        assertNotNull(fatherSide);
        assertEquals(2, fatherSide.size());
        client.reset();
    }

    @Test
    public void calculateFamilyRelationshipsFatherSideEmptyPass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", "fatherID1", person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Get person1's father's side
        Filter filter = new Filter();
        HashSet<String> fatherSide = filter.filterFatherSidePersonIDSet();

        assertNotNull(fatherSide);
        assertEquals(0, fatherSide.size());
        client.reset();
    }

    @Test
    public void calculateFamilyRelationshipsMotherSidePass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", person4.getPersonID(), "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", "fatherID2", "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Get person1's father's side
        Filter filter = new Filter();
        HashSet<String> motherSide = filter.filterMotherSidePersonIDSet();

        assertNotNull(motherSide);
        assertEquals(2, motherSide.size());
        client.reset();
    }

    @Test
    public void calculateFamilyRelationshipsMotherSideEmptyPass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", person4.getPersonID(), "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", "fatherID2", "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), "motherID1", "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);

        // Get person1's father's side
        Filter filter = new Filter();
        HashSet<String> motherSide = filter.filterMotherSidePersonIDSet();

        assertNotNull(motherSide);
        assertEquals(0, motherSide.size());
        client.reset();
    }

    @Test
    public void filterEventsNonePass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        Event event2 = new Event("username", person2.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(event1);
        eventList.add(event2);
        Event[] events = new Event[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            events[i] = eventList.get(i);
        }

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);
        client.setFamilyEvents(events);

        // Set up settings
        Settings settings = Settings.getInstance();

        Filter filter = new Filter();
        filter.filterEvents();

        List<Event> filteredFamilyEvents = client.getFilteredFamilyEvents();
        HashMap<String, List<Event>> userFamilyEventDict = client.getFilteredEventDict();

        assertNotNull(filteredFamilyEvents);
        assertNotNull(userFamilyEventDict);
        assertEquals(userFamilyEventDict.keySet().size(), filteredFamilyEvents.size());
        assertEquals(2, filteredFamilyEvents.size());
        client.reset();
        settings.reset();
    }

    @Test
    public void filterEventsNoMalesPass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        Event event2 = new Event("username", person3.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(event1);
        eventList.add(event2);
        Event[] events = new Event[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            events[i] = eventList.get(i);
        }

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);
        client.setFamilyEvents(events);

        // Set up settings
        Settings settings = Settings.getInstance();
        settings.setMaleEventSwitchStatus(false);

        Filter filter = new Filter();
        filter.filterEvents();

        List<Event> filteredFamilyEvents = client.getFilteredFamilyEvents();
        HashMap<String, List<Event>> userFamilyEventDict = client.getFilteredEventDict();

        assertNotNull(filteredFamilyEvents);
        assertNotNull(userFamilyEventDict);
        assertEquals(userFamilyEventDict.keySet().size(), filteredFamilyEvents.size());
        assertEquals(1, filteredFamilyEvents.size());
        client.reset();
        settings.reset();
    }

    @Test
    public void filterEventsNoFemalesPass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        Event event2 = new Event("username", person3.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(event1);
        eventList.add(event2);
        Event[] events = new Event[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            events[i] = eventList.get(i);
        }

        // Set up client
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);
        client.setFamilyEvents(events);

        // Set up settings
        Settings settings = Settings.getInstance();
        settings.setFemaleEventSwitchStatus(false);

        Filter filter = new Filter();
        filter.filterEvents();

        List<Event> filteredFamilyEvents = client.getFilteredFamilyEvents();
        HashMap<String, List<Event>> userFamilyEventDict = client.getFilteredEventDict();

        assertNotNull(filteredFamilyEvents);
        assertNotNull(userFamilyEventDict);
        assertEquals(userFamilyEventDict.keySet().size(), filteredFamilyEvents.size());
        assertEquals(1, filteredFamilyEvents.size());
        client.reset();
        settings.reset();
    }

    @Test
    public void filterEventsNoFatherSidePass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        Event event2 = new Event("username", person2.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(event1);
        eventList.add(event2);
        Event[] events = new Event[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            events[i] = eventList.get(i);
        }

        // Set up client
        Filter filter = new Filter();
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);
        client.setFamilyEvents(events);
        client.setFatherSidePersonIDSet(filter.filterFatherSidePersonIDSet());

        // Set up settings
        Settings settings = Settings.getInstance();
        settings.setFatherSideSwitchStatus(false);

        filter.filterEvents();

        List<Event> filteredFamilyEvents = client.getFilteredFamilyEvents();
        HashMap<String, List<Event>> userFamilyEventDict = client.getFilteredEventDict();

        assertNotNull(filteredFamilyEvents);
        assertNotNull(userFamilyEventDict);
        assertEquals(userFamilyEventDict.keySet().size(), filteredFamilyEvents.size());
        assertEquals(1, filteredFamilyEvents.size());
        client.reset();
        settings.reset();
    }

    @Test
    public void filterEventsNoMotherSidePass() {
        // Create related people to plug into functions
        Person person4 = new Person("username", "jimmy", "lastName",
                "m", "fatherID4", "motherID4", "spouseID");
        Person person3 = new Person("username", "katie", "lastName",
                "f", "fatherID3", "motherID3", "spouseID");
        Person person2 = new Person("username", "johnny", "lastName",
                "m", person4.getPersonID(), "motherID2", "spouseID");
        Person person1 = new Person("username", "greg", "lastName",
                "m", person2.getPersonID(), person3.getPersonID(), "spouseID");

        // Create user family dict
        HashMap<String, Person> userFamilyDict = new HashMap<String, Person>();
        userFamilyDict.put(person1.getPersonID(), person1);
        userFamilyDict.put(person2.getPersonID(), person2);
        userFamilyDict.put(person3.getPersonID(), person3);
        userFamilyDict.put(person4.getPersonID(), person4);

        // Create family events
        Event event1 = new Event("username", person1.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        Event event2 = new Event("username", person3.getPersonID(), (float)1, (float)1,
                "country", "city", "eventType", 2020);
        List<Event> eventList = new ArrayList<Event>();

        eventList.add(event1);
        eventList.add(event2);
        Event[] events = new Event[eventList.size()];
        for (int i = 0; i < eventList.size(); i++) {
            events[i] = eventList.get(i);
        }

        // Set up client
        Filter filter = new Filter();
        Client client = Client.getInstance();
        client.setPerson(person1);
        client.setUserFamilyDict(userFamilyDict);
        client.setFamilyEvents(events);
        client.setMotherSidePersonIDSet(filter.filterMotherSidePersonIDSet());

        // Set up settings
        Settings settings = Settings.getInstance();
        settings.setMotherSideSwitchStatus(false);

        filter.filterEvents();

        List<Event> filteredFamilyEvents = client.getFilteredFamilyEvents();
        HashMap<String, List<Event>> userFamilyEventDict = client.getFilteredEventDict();

        assertNotNull(filteredFamilyEvents);
        assertNotNull(userFamilyEventDict);
        assertEquals(userFamilyEventDict.keySet().size(), filteredFamilyEvents.size());
        assertEquals(1, filteredFamilyEvents.size());
        client.reset();
        settings.reset();
    }
}
