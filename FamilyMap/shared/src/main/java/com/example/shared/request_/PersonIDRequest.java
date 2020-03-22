package com.example.shared.request_;

import com.example.shared.model_.AuthToken;

public class PersonIDRequest {
    private AuthToken authToken;

    /**
     * Empty constructor
     */
    public PersonIDRequest() { }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
