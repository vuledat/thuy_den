package com.datvl.english.word;

public class WordAnswer {
    private String result;
    private int is_true;

    public WordAnswer(String result, int is_true) {
        this.result = result;
        this.is_true = is_true;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getIs_true() {
        return is_true;
    }

    public void setIs_true(int is_true) {
        this.is_true = is_true;
    }
}
