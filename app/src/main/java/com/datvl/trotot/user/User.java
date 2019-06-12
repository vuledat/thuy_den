package com.datvl.trotot.user;

import java.io.Serializable;

public class User implements Serializable {
    private String content;

    public User(String content) {
        this.content = content;
    }

    public User() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
