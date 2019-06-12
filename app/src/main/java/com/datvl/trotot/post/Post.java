package com.datvl.trotot.post;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
    private int id;
    private String name;
    private int price;
    private String image;
    private String content;
    private String time;
    private String address;
    private int scale;
    private int is_save;

    public int getIs_save() {
        return is_save;
    }

    public void setIs_save(int is_save) {
        this.is_save = is_save;
    }

    public Post(int id, String name, int price, String image, String content, String address, String time, int scale, int is_save) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.content = content;
        this.address = address;
        this.time = time;
        this.scale = scale;
        this.is_save = is_save;
    }

    public Post(int id, String name, int price, String image, String content, String address, String time, int scale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.content = content;
        this.address = address;
        this.time = time;
        this.scale = scale;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
