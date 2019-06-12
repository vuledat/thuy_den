package com.datvl.trotot.model;

public class MessageUser {
    private int id;
    private String username;
    private String date;

    public MessageUser(int id, String username, String date) {
        this.id = id;
        this.username = username;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
