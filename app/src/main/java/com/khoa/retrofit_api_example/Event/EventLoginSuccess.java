package com.khoa.retrofit_api_example.Event;

import com.khoa.retrofit_api_example.Model.User;

public class EventLoginSuccess {
    private User user;

    public EventLoginSuccess(User user) {
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }
}
