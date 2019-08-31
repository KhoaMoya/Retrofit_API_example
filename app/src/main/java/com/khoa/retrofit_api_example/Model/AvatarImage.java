package com.khoa.retrofit_api_example.Model;

import com.google.gson.annotations.SerializedName;

public class AvatarImage {
    @SerializedName("id")
    private int id;
    @SerializedName("url")
    private String url;
    @SerializedName("message")
    private String message;

    public AvatarImage(int id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
