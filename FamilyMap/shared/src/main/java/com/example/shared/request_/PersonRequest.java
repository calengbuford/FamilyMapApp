package com.example.shared.request_;

import com.example.shared.model_.AuthToken;

public class PersonRequest {
    private AuthToken authToken;

    /**
     * Empty constructor
     */
    public PersonRequest() { }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
