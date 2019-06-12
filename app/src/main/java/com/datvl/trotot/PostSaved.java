package com.datvl.trotot;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.datvl.trotot.adapter.ListPostAdapter;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PostSaved extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Post> listPost;
    Common cm;
    public String url;
    SharedPreferences sharedPreferences;
    ProgressBar pb;
    String view_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_saved);
        sharedPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "0");
        view_type = sharedPreferences.getString("view_type", "List View");
        url = cm.getUrlListPostsSaved() + user_id;
        GetApi getApi = new GetApi(url, getApplication(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {

                listPost = new ArrayList<>();
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);
                        listPost.add(new Post(
                                Integer.parseInt(jsonObject.getString("id")),
                                jsonObject.getString("name"),
                                Integer.parseInt(jsonObject.getString("price")) ,
                                jsonObject.getString("image"),
                                jsonObject.getString("content"),
                                jsonObject.getString("address"),
                                jsonObject.getString("created_at_string"),
                                Integer.parseInt(jsonObject.getString("scale")),
                                jsonObject.has("is_save") ? Integer.parseInt(jsonObject.getString("is_save")) : 0
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView = (RecyclerView) findViewById(R.id.recycler_post_saved_view);
                setLayout(view_type, recyclerView);
                setHideProgress();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    public void setHideProgress(){
        pb = findViewById(R.id.progressBarSaved);
        pb.setVisibility(View.GONE);
    }

    public void setLayout(String view_type, RecyclerView recyclerView){
        switch (view_type) {
            case "Grid View":
                recyclerView.setLayoutManager(new GridLayoutManager(getApplication(), 2));
                break;
            case "List View":
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
                break;
        }
        ListPostAdapter viewAdapter = new ListPostAdapter(listPost);
        recyclerView.setAdapter(viewAdapter);
    }
}
