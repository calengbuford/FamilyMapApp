package com.example.shared.response_;

import com.example.shared.model_.Event;

public class EventResponse {
    private Event[] data;
    private Boolean success = null;
    private String message;

    /**
     * Empty constructor
     */
    public EventResponse() { }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
