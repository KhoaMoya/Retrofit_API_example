package com.khoa.retrofit_api_example.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("confirm_code")
    private int confirm_code;
    @SerializedName("token")
    private String token;
    @SerializedName("id_avatar_image")
    private int id_avatar_image;

    public User(int id, String username, String email, String created_at, int confirm_code, String token, int id_avatar_image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.created_at = created_at;
        this.confirm_code = confirm_code;
        this.token = token;
        this.id_avatar_image = id_avatar_image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getConfirm_code() {
        return confirm_code;
    }

    public void setConfirm_code(int confirm_code) {
        this.confirm_code = confirm_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId_avatar_image() {
        return id_avatar_image;
    }

    public void setId_avatar_image(int id_avatar_image) {
        this.id_avatar_image = id_avatar_image;
    }
}
