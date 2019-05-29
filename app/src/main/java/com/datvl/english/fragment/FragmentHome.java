package com.datvl.english.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.datvl.english.OnEventListener;
import com.datvl.english.R;
import com.datvl.english.adapter.ListMessageAdapter;
import com.datvl.english.adapter.ListWordRSAdapter;
import com.datvl.english.api.GetApi;
import com.datvl.english.common.Common;
import com.datvl.english.word.Word;
import com.datvl.english.word.WordAnswer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    List<Word> listWord;
    List<WordAnswer> listWordAnser;
    Common cm;
    SharedPreferences sharedPreferences;
    public String url;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_home,container,false);
        recyclerView = view.findViewById(R.id.recycler_word_view);
        url = cm.getNew();

        Intent intent = getActivity().getIntent();
        final BottomNavigationView navigation = (BottomNavigationView) view.findViewById(R.id.navigation);

        listWord = new ArrayList<>();
        listWordAnser = new ArrayList<>();
        GetApi getApi = new GetApi(url, getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);
                        listWord.add(new Word(
                                Integer.parseInt(jsonObject.getString("id")),
                                Integer.parseInt(jsonObject.getString("mem")),
                                jsonObject.getString("en"),
                                jsonObject.getString("vn"),
                                jsonObject.getString("vn_f1"),
                                jsonObject.getString("vn_f2")
                        ));
//                        listWordAnser.add(new WordAnswer(jsonObject.getString("vn"), 1));
//                        listWordAnser.add(new WordAnswer(jsonObject.getString("vn_f1"), 0));
//                        listWordAnser.add(new WordAnswer(jsonObject.getString("vn_f2"), 0));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "onSuccess: " + listWord.get(0).getEn());

                listWordAnser.add(new WordAnswer(listWord.get(0).getVn(), 1));
                listWordAnser.add(new WordAnswer(listWord.get(0).getVn_f1(), 0));
                listWordAnser.add(new WordAnswer(listWord.get(0).getVn_f2(), 0));
                Collections.shuffle(listWordAnser);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListWordRSAdapter viewAdapter = new ListWordRSAdapter(listWordAnser);
                recyclerView.setAdapter(viewAdapter);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });


        return view;
    }

    public void loadResult( List<Word> listWord) {
        listWordAnser = new ArrayList<>();
        listWordAnser.add(new WordAnswer(listWord.get(0).getVn(), 1));
        listWordAnser.add(new WordAnswer(listWord.get(0).getVn_f1(), 0));
        listWordAnser.add(new WordAnswer(listWord.get(0).getVn_f2(), 0));
        Collections.shuffle(listWordAnser);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ListWordRSAdapter viewAdapter = new ListWordRSAdapter(listWordAnser);
        recyclerView.setAdapter(viewAdapter);
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {

    }

    public void reloadFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentHome fragmentHome = new FragmentHome();
        fragmentTransaction.replace(R.id.content,fragmentHome);
        fragmentTransaction.commit();
    }
}

