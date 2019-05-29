package com.datvl.english;

public interface OnEventListener<JSONArray> {
    public void onSuccess(org.json.JSONArray jsonAray);

    public void onFailure(Exception e);
}
