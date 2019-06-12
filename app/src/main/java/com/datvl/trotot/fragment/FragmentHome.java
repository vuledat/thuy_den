package com.datvl.trotot.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.datvl.trotot.NewPost;
import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.R;
import com.datvl.trotot.adapter.ListPostAdapter;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.model.Area;
import com.datvl.trotot.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<Post> listPost;
    Common cm;
    SharedPreferences sharedPreferences;
    public String url;
    String view_type;
    Button btn_new_post;

    GestureDetector gestureScanner;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home,container,false);
        setNewPost(view);

        gestureScanner = new GestureDetector(new Gesture());
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureScanner.onTouchEvent(event);
                return true;
            }
        });

        sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        Boolean is_login = sharedPreferences.getBoolean("is_login", false);
        url = cm.getUrlListPostsUser() + sharedPreferences.getString("user_id", "0");
        if ( is_login == false) {
            url = cm.getUrlListPosts();
        }

        Intent intent = getActivity().getIntent();
        filter(view);

        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);

        listPost = new ArrayList<>();
        GetApi getApi = new GetApi(url, getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
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

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("fillter", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                view_type = sharedPreferences.getString("view_type", "List View");
                int item_home_grid = R.layout.item_home_grid;

                setLayout(view_type, recyclerView, view);

                cm.setHideProgress(view, R.id.progressBarHome);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        //pull refresh
        final SwipeRefreshLayout swipeLayout;
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.contentViewList);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    List<Post> listPost;
                    @Override public void run() {
                        listPost = new ArrayList<>();
                        GetApi getApi = new GetApi(url, getActivity(), new OnEventListener() {
                            @Override
                            public void onSuccess(JSONArray object) {
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
                                                Integer.parseInt(jsonObject.getString("scale"))
                                        ));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                setLayout(view_type, recyclerView, view);

                                swipeLayout.setRefreshing(false);
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                }, 2000);
            }
        });
        //end pull refresh

        return view;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {

    }

    public void filter(final View view) {
//        String arr_view_type[]={
//                "Grid View",
//                "List View"};
//        String arr_price_type[]={
//                "Price ⇧",
//                "Price ⇩"};
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("fillter", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final Spinner spin=(Spinner) view.findViewById(R.id.spinner_view);
        final Spinner spin_price=(Spinner) view.findViewById(R.id.spinner_price);
        final String view_type = sharedPreferences.getString("view_type", "Grid View");
        final String price_type = sharedPreferences.getString("price_type", "Price ");


        final List<Area> listArea = new ArrayList<>();
//        listArea.add(new Area(1, "Gia Lam"));
//        listArea.add(new Area(2, "Dong Da"));
//        listArea.add(new Area(3, "Ha Dong"));

        GetApi getApi = new GetApi(cm.getListArea(), getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);

                        listArea.add(new Area(Integer.parseInt(jsonObject.getString("id")),
                                jsonObject.getString("name")
                        ));
//                        listPost.add(new Post(
//                                Integer.parseInt(jsonObject.getString("id")),
//                                jsonObject.getString("name"),
//                                Integer.parseInt(jsonObject.getString("price")) ,
//                                jsonObject.getString("image"),
//                                jsonObject.getString("content"),
//                                jsonObject.getString("address"),
//                                jsonObject.getString("created_at_string"),
//                                Integer.parseInt(jsonObject.getString("scale"))
//                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setAdapterSpinner("price",price_type, spin_price, listArea, editor);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        List arr_view_type = new ArrayList();
        arr_view_type.add("Grid View");
        arr_view_type.add("List View");

        List arr_price_type = new ArrayList();
        arr_price_type.add("Price ⇧");
        arr_price_type.add("Price ⇩");

        setAdapterSpinner("view",view_type, spin, arr_view_type, editor);
//        setAdapterSpinner("price",price_type, spin_price, arr_price_type, editor);
//        setAdapterSpinner("price",price_type, spin_price, listArea, editor);
    }

    public void reloadFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentHome fragmentHome = new FragmentHome();
        fragmentTransaction.replace(R.id.content,fragmentHome);
        fragmentTransaction.commit();
    }

    public void setLayout(String view_type, RecyclerView recyclerView, View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_home_view);
        switch (view_type) {
            case "Grid View":
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            case "List View":
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
        }
        ListPostAdapter viewAdapter = new ListPostAdapter(listPost);
        recyclerView.setAdapter(viewAdapter);
    }

    public void setAdapterSpinner(final String type, String data, Spinner spin, List listArea, final SharedPreferences.Editor editor){
        ArrayAdapter<Area> adapter=new ArrayAdapter<Area>
                (
                    this.getActivity(),
                    android.R.layout.simple_spinner_item,
                    listArea
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switch (type){
            case "view":
                switch (data) {
                    case "Grid View":
                        spin.setSelection(0);
                        break;
                    case "List View":
                        spin.setSelection(1);
                        break;
                }
                break;
            case "price":
                switch (data) {
                    case "Price ⇧":
                        spin.setSelection(0);
                        break;
                    case "Price ⇩":
                        spin.setSelection(1);
                        break;
                }
                break;
        }

        final int iCurrentSelection = spin.getSelectedItemPosition();

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case "view":
                        if(iCurrentSelection != position){
                            editor.putString("view_type", parent.getAdapter().getItem(position).toString());
                            editor.commit();
                            reloadFragment();
                        }
                        break;
                    case "price":
                        if(iCurrentSelection != position){
                            editor.putString("price_type", parent.getAdapter().getItem(position).toString());
                            editor.commit();
                            reloadFragment();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    class Gesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("MotionEvent", "onFling");
            try {
                if (Math.abs(e1.getY() - e2.getY()) > 100){
                    Log.d("touch", "onFling: +");
                    return true;
                }
                // right to left swipe
                if (e1.getX() - e2.getX() > 1 && Math.abs(velocityX) > 100) {


                    return true;
                } else if (e2.getX() - e1.getX() > 1 && Math.abs(velocityX) > 100) {
                    Log.d("touch", "onFling: +");
                    return true;
                }
            } catch (Exception e) {
                // nothing
            }
            return true;
        }
    }

    public void setNewPost(View view) {
        btn_new_post = view.findViewById(R.id.btn_new_post);
        btn_new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPost.class);
                startActivity(intent);
            }
        });
    }
}

