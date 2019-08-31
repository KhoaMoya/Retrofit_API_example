package com.khoa.retrofit_api_example.Event;

import com.khoa.retrofit_api_example.Model.User;

public class EventSignupSuccess {
    private User user;

    public EventSignupSuccess(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
