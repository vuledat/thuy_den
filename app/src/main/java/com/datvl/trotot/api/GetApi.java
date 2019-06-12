package com.datvl.trotot.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.datvl.trotot.OnEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetApi {
    public static String kq = null;
    private String url;
    private OnEventListener callback;
    private Context context;

    public GetApi(String url, Context context, OnEventListener callback) {
        this.context = context;
        this.callback = callback;
        this.url = url;
        getAPI(url, context,callback);
    }

    public static String getAPI(String url, final Context context, final OnEventListener callback){
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Function GetApi:", "Can't connect " + error);
                    }
                });
        queue.add(stringRequest);
        return kq;
    }
}
