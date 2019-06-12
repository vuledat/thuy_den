package com.datvl.trotot.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.OnItemClickListener;
import com.datvl.trotot.R;
import com.datvl.trotot.adapter.ListKeySearchAdapter;
import com.datvl.trotot.adapter.ListPostAdapter;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.model.KeySearch;
import com.datvl.trotot.model.Message;
import com.datvl.trotot.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class FragmentSearch extends Fragment {

    RecyclerView recyclerView;
    List<Post> listPost;
    List<KeySearch> listKeysearch;
    EditText edt_search;
    Common cm;
    ProgressBar progressBar;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_search,container,false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_search_view);
        progressBar = view.findViewById(R.id.progressBarSearch);
        edt_search = (EditText) view.findViewById(R.id.edt_search);


        GetApi getApi = new GetApi(cm.getListKeySearch(), getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
                listKeysearch = new ArrayList<>();
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);
                        listKeysearch.add(new KeySearch(
                                Integer.parseInt(jsonObject.getString("id")),
                                jsonObject.getString("keyword")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventAfterClick();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListKeySearchAdapter viewAdapter = new ListKeySearchAdapter(listKeysearch, new OnItemClickListener() {
                    @Override
                    public void onItemClick(KeySearch keySearch) {
                        loadPostByKeword(keySearch.getKeyword());
                    }
                });
                recyclerView.setAdapter(viewAdapter);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });

        edt_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_search.getRight() - 2 * edt_search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        String key = String.valueOf(edt_search.getText());
                        loadPostByKeword(key);
                        return true;
                    }
                    if (event.getRawX() <= (edt_search.getLeft() + 120)) {
                        Log.d(TAG, "Nhap lai " + edt_search.getText());
                        if (edt_search.getText().toString().matches("") ) {
                            Log.d(TAG, "Nhap lai " + edt_search.getText());
                            Toast.makeText(v.getContext(),"Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else
                            {
                                GetApi addKeyword = new GetApi(cm.addKeyword() + (edt_search.getText().equals("") ? edt_search.getText() : "null" ), getActivity(), new OnEventListener() {
                                    @Override
                                    public void onSuccess(JSONArray object) {

                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                    }
                                });
                                //url = cm.getListKeySearch();
                                loadListKeyword(cm.getListKeySearch());
                            };
                        return true;
                    }
                }
                return false;
            }
        });

        LinearLayout content_fragmnt_search = view.findViewById(R.id.content_fragmnt_search);
        content_fragmnt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAfterClick();
            }
        });

        return view;
    }

    public void hideProcess(){
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void loadListKeyword(String link) {
        GetApi getApi = new GetApi(link, getActivity(), new OnEventListener() {
            @Override
            public void onSuccess(JSONArray object) {
                listKeysearch = new ArrayList<>();
                for (int i=0 ; i< object.length() ; i++){
                    try {
                        JSONObject jsonObject = object.getJSONObject(i);
                        listKeysearch.add(new KeySearch(
                                Integer.parseInt(jsonObject.getString("id")),
                                jsonObject.getString("keyword")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                eventAfterClick();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListKeySearchAdapter viewAdapter = new ListKeySearchAdapter(listKeysearch, new OnItemClickListener() {
                    @Override
                    public void onItemClick(KeySearch keySearch) {
                        loadPostByKeword(keySearch.getKeyword());
                    }
                });
                recyclerView.setAdapter(viewAdapter);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void eventAfterClick() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt_search.getWindowToken(), 0);
    }

    public void loadPostByKeword(String key) {
        GetApi getApi = new GetApi((cm.getUrlListSearch() + key).replace(" ", "%20"), getActivity(), new OnEventListener() {
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
                eventAfterClick();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                ListPostAdapter viewAdapter = new ListPostAdapter(listPost);
                recyclerView.setAdapter(viewAdapter);
            }
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
