package com.datvl.english;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datvl.english.common.Common;
import com.datvl.english.fragment.FragmentHome;
import com.datvl.english.fragment.FragmentMessage;
import com.datvl.english.fragment.FragmentNotice;
import com.datvl.english.fragment.FragmentSearch;
import com.datvl.english.fragment.FragmentSetting;
import com.datvl.english.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.datvl.english.api.GetApi;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private TextView mTextMessage;
    List<Post> listPost;
    Common cm;
    public String url = cm.getUrlListPosts();
    ProgressBar pb;
    GetApi getApi;
    String post = null;
    private FirebaseAuth mAuth;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    String lat;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    getIntent().putExtra("ListPost", (Serializable) listPost);
                    FragmentHome fragmentHome = new FragmentHome();
                    fragmentTransaction.replace(R.id.content,fragmentHome);
                    fragmentTransaction.commit();

                    return true;

                case R.id.navigation_message:
                    FragmentMessage fragmentMessage = new FragmentMessage();
                    fragmentTransaction.replace(R.id.content,fragmentMessage);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_search:
                    FragmentSearch fragmentSearch = new FragmentSearch();
                    fragmentTransaction.replace(R.id.content,fragmentSearch);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_notifications:
                    FragmentNotice fragmentNotice = new FragmentNotice();
                    fragmentTransaction.replace(R.id.content,fragmentNotice);
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_setting:
                    FragmentSetting fragmentSetting = new FragmentSetting();
                    fragmentTransaction.replace(R.id.content,fragmentSetting);
                    fragmentTransaction.commit();

                    return true;
            }
            return false;
        }
    };
    public void setHideProgress(){
        pb = findViewById(R.id.progressBarHome);
        pb.setVisibility(View.GONE);
    }
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        getIntent().putExtra("ListPost", (Serializable) listPost);
        FragmentHome fragmentHome = new FragmentHome();
        fragmentTransaction.replace(R.id.content,fragmentHome);
        fragmentTransaction.commit();

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
                                Integer.parseInt(jsonObject.getString("scale"))
                                ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        updateUI(currentUser);
//    }

    public String getFormatedNum(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("dat", "onLocationChanged: " + "Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
