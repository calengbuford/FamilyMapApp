package com.example.familymap.proxy;

import com.example.familymap.network.Proxy;
import com.example.shared.model_.Event;
import com.example.shared.request_.LoginRequest;
import com.example.shared.response_.EventResponse;
import com.example.shared.response_.LoginResponse;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test a successful retrieval of data for many events.
 * Test a failed retrieval of data for many events.
 */
public class RetrieveEventsTest {
    private String serverHost = "192.168.0.103";
    private String serverPort = "8080";

    @Test
    public void retrieveManyPersonsPass() {
        // Login a user and get their authToken
        LoginRequest request = new LoginRequest("sheila", "parker");
        LoginResponse loginResponse = Proxy.login(serverHost, serverPort, request);
        assertNotNull(loginResponse);

        // Retrieve data
        EventResponse eventResponse = Proxy.dataSyncEvents(serverHost, serverPort, loginResponse.getAuthToken());

        // Verify the information in the response object
        assertNotNull(eventResponse);
        assertTrue(eventResponse.getSuccess());

        Event[] eventsData = eventResponse.getData();
        assertTrue(eventsData.length > 0);

        // Test sheila's first event
        Event event1 = eventsData[0];
        assertEquals("sheila", event1.getAssociatedUsername());
        assertEquals("birth", event1.getEventType());
        assertEquals("Melbourne", event1.getCity());

        // Test sheila's second event
        Event event2 = eventsData[1];
        assertEquals("sheila", event2.getAssociatedUsername());
        assertEquals("marriage", event2.getEventType());
        assertEquals("Los Angeles", event2.getCity());

        // Test sheila's third event
        Event event3 = eventsData[2];
        assertEquals("sheila", event3.getAssociatedUsername());
        assertEquals("completed asteroids", event3.getEventType());
        assertEquals("Qaanaaq", event3.getCity());

        // Test sheila's fourth event
        Event event4 = eventsData[3];
        assertEquals("sheila", event4.getAssociatedUsername());
        assertEquals("COMPLETED ASTEROIDS", event4.getEventType());
        assertEquals("Qaanaaq", event4.getCity());
    }

    @Test
    public void retrieveManyPersonsFail() {
        // Attempt to retrieve data with a bad authToken
        EventResponse eventResponse = Proxy.dataSyncEvents(serverHost, serverPort, "badAuthToken");

        // Verify the information in the response object
        assertNull(eventResponse);   // Indicates a bad request
    }
}
