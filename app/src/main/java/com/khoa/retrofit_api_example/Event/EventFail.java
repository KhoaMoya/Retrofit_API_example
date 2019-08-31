package com.khoa.retrofit_api_example.Event;

public class EventFail {
    private String error;

    public EventFail(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
