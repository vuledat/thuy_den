package com.datvl.trotot.model;

public class Message {
    private String content;
    private String time;
    private String user;

    public Message(String content, String time, String user) {
        this.content = content;
        this.time = time;
        this.user = user;
    }

    public Message(String content) {
        this.content = content;
    }

    public Message() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
