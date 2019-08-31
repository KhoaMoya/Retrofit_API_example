package com.khoa.retrofit_api_example.Model;

public class SimplePing {
    private float value_ping;
    private String address;
    private String time;

    public SimplePing(float value_ping, String address, String time) {
        this.value_ping = value_ping;
        this.address = address;
        this.time = time;
    }

    public float getValue_ping() {
        return value_ping;
    }

    public void setValue_ping(float value_ping) {
        this.value_ping = value_ping;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
