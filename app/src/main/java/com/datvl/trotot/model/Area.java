package com.datvl.trotot.model;

public class Area {
    private int id;
    private String name;
    private String gps;

    public Area(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Area(int id, String name, String gps) {
        this.id = id;
        this.name = name;
        this.gps = gps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Override
    public String toString() {
        return name;
    }
}
