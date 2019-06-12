package com.datvl.trotot;

import org.json.JSONArray;

public interface OnEventListener<JSONArray> {
    public void onSuccess(org.json.JSONArray jsonAray);

    public void onFailure(Exception e);
}
