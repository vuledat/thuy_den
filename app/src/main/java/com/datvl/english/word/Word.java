package com.datvl.english.word;

public class Word {
    private int id;
    private int mem;
    private String en;
    private String vn;
    private String vn_f1;
    private String vn_f2;

    public Word(int id, int mem, String en, String vn, String vn_f1, String vn_f2) {
        this.id = id;
        this.mem = mem;
        this.en = en;
        this.vn = vn;
        this.vn_f1 = vn_f1;
        this.vn_f2 = vn_f2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMem() {
        return mem;
    }

    public void setMem(int mem) {
        this.mem = mem;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public String getVn_f1() {
        return vn_f1;
    }

    public void setVn_f1(String vn_f1) {
        this.vn_f1 = vn_f1;
    }

    public String getVn_f2() {
        return vn_f2;
    }

    public void setVn_f2(String vn_f2) {
        this.vn_f2 = vn_f2;
    }
}
