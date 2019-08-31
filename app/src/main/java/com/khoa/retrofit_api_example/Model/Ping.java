package com.khoa.retrofit_api_example.Model;

import com.google.gson.annotations.SerializedName;

public class Ping {

    @SerializedName("id_user")
    private int id_user;
    @SerializedName("address")
    private String address;
    @SerializedName("value_ping")
    private String value_ping;
    @SerializedName("id")
    private int id;

    public Ping(int id_user, String address, String value_ping, int id) {
        this.id_user = id_user;
        this.address = address;
        this.value_ping = value_ping;
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue_ping() {
        return value_ping;
    }

    public void setValue_ping(String value_ping) {
        this.value_ping = value_ping;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
