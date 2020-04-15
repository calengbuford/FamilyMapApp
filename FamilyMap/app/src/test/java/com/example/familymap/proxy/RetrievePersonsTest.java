package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.model_.Person;
import com.example.shared.request_.LoginRequest;
import com.example.shared.response_.LoginResponse;
import com.example.shared.response_.PersonIDResponse;
import com.example.shared.response_.PersonResponse;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test a successful retrieval of data for one person and many persons.
 * Test a failed retrieval of data for one person and many persons.
 */
public class RetrievePersonsTest {
    private String serverHost = "192.168.0.103";
    private String serverPort = "8080";

    @Test
    public void retrievePersonPass() {
        // Login a user and get their authToken
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResponse loginResponse = Proxy.login(serverHost, serverPort, request);
        assertNotNull(loginResponse);

        // Retrieve data
        PersonIDResponse personIDResponse = Proxy.getPersonID(serverHost, serverPort,
                loginResponse.getPersonID(), loginResponse.getAuthToken());

        // Verify the information in the response object
        assertNotNull(personIDResponse);
        assertTrue(personIDResponse.getSuccess());
        assertEquals("sheila", personIDResponse.getAssociatedUsername());
        assertEquals("Sheila", personIDResponse.getFirstName());
        assertEquals("Parker", personIDResponse.getLastName());
    }

    @Test
    public void retrievePersonFail() {
        // Login a user and get their authToken
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResponse loginResponse = Proxy.login(serverHost, serverPort, request);
        assertNotNull(loginResponse);

        // Attempt to retrieve data with a bad authToken
        PersonIDResponse personIDResponse = Proxy.getPersonID(serverHost, serverPort,
                loginResponse.getPersonID(), "badAuthToken");

        assertNull(personIDResponse);   // Indicates a bad request
    }

    @Test
    public void retrieveManyPersonsPass() {
        // Login a user and get their authToken
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResponse loginResponse = Proxy.login(serverHost, serverPort, request);
        assertNotNull(loginResponse);

        // Retrieve data
        PersonResponse personResponse = Proxy.dataSyncPersons(serverHost, serverPort, loginResponse.getAuthToken());

        // Verify the information in the response object
        assertNotNull(personResponse);
        assertTrue(personResponse.getSuccess());

        Person[] personsData = personResponse.getData();
        assertTrue(personsData.length > 0);

        // Test sheila as first person
        Person person1 = personsData[0];
        assertEquals("sheila", person1.getAssociatedUsername());
        assertEquals("Sheila", person1.getFirstName());
        assertEquals("Parker", person1.getLastName());

        // Test sheila's spouse as second person
        Person person2 = personsData[1];
        assertEquals(person1.getSpouseID(), person2.getPersonID());
        assertEquals("Davis", person2.getFirstName());
        assertEquals("Hyer", person2.getLastName());

        // Test sheila's father as third person
        Person person3 = personsData[2];
        assertEquals(person1.getFatherID(), person3.getPersonID());
        assertEquals("Blaine", person3.getFirstName());
        assertEquals("McGary", person3.getLastName());

        // Test sheila's mother as fourth person
        Person person4 = personsData[3];
        assertEquals(person1.getMotherID(), person4.getPersonID());
        assertEquals("Betty", person4.getFirstName());
        assertEquals("White", person4.getLastName());
    }

    @Test
    public void retrieveManyPersonsFail() {
        // Attempt to retrieve data with a bad authToken
        PersonResponse personResponse = Proxy.dataSyncPersons(serverHost, serverPort, "badAuthToken");

        // Verify the information in the response object
        assertNull(personResponse);   // Indicates a bad request
    }

}
