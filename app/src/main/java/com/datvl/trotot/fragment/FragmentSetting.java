package com.datvl.trotot.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datvl.trotot.LocationLike;
import com.datvl.trotot.Login;
import com.datvl.trotot.PostDetail;
import com.datvl.trotot.PostSaved;
import com.datvl.trotot.R;

public class FragmentSetting extends Fragment {

    LinearLayout posts_saved, location_like, user_info;
    TextView txt_username_setting, txt_location_like;
    LinearLayout not_login_setting, login_setting;
    ImageView img_signout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting,container,false);

        setHandleUser(view);
        signOut(view);
        /**
         *Posts_saved
         */
        posts_saved = view.findViewById(R.id.posts_saved);
        posts_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent= new Intent(v.getContext(), PostSaved.class);

                v.getContext().startActivity(intent);
            }
        });
        /**
         *end Posts_saved
         * */
        location_like = view.findViewById(R.id.location_like);
        location_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent= new Intent(v.getContext(), LocationLike.class);

                v.getContext().startActivity(intent);
            }
        });

        return view;

    }
    public void openActiviti(LinearLayout layout){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent= new Intent(v.getContext(), Login.class);

                v.getContext().startActivity(intent);
            }
        });
    }

    public void setHandleUser(View view) {

        not_login_setting = view.findViewById(R.id.not_login_setting);
        login_setting = view.findViewById(R.id.login_setting);
        txt_username_setting = view.findViewById(R.id.user_name_setting);
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if ((Boolean) sharedPreferences.getBoolean("is_login", false)) {
            login_setting.setVisibility(View.VISIBLE);
            String username = sharedPreferences.getString("username", "Gest");
//            String avatar = sharedPreferences.getString("avatar", "");
            txt_username_setting.setText(username);
//            Log.d("ok", "user" + jsonObject.getString("name"));
        }
        else {

            /**
             *User_info
             */
            user_info = view.findViewById(R.id.user_info);
            user_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent= new Intent(v.getContext(), Login.class);

                    v.getContext().startActivity(intent);
                }
            });
            /**
             *end User_info
             * */
            not_login_setting.setVisibility(View.VISIBLE);
        }
    }
    public void signOut(View view) {
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        img_signout = view.findViewById(R.id.signout);
        img_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("is_login", false);
                editor.commit();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSetting fragmentSetting = new FragmentSetting();
                fragmentTransaction.replace(R.id.content,fragmentSetting);
                fragmentTransaction.commit();
            }
        });
    }
}
