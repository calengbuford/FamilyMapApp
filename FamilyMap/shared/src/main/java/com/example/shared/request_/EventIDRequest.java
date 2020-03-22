package com.example.shared.request_;

import com.example.shared.model_.AuthToken;

public class EventIDRequest {
    private AuthToken authToken;

    /**
     * Empty constructor
     */
    public EventIDRequest() { }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
