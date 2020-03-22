package com.example.shared.request_;

import com.example.shared.model_.AuthToken;

public class EventRequest {
    private AuthToken authToken;

    /**
     * Empty constructor
     */
    public EventRequest() { }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
