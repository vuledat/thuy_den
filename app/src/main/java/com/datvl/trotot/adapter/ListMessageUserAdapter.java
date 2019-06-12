package com.datvl.trotot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datvl.trotot.Chat;
import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.PostDetail;
import com.datvl.trotot.R;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.fragment.FragmentMessage;
import com.datvl.trotot.model.Message;
import com.datvl.trotot.model.MessageUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListMessageUserAdapter extends RecyclerView.Adapter<ListMessageUserAdapter.RecyclerViewHolder>{

    private List<MessageUser> data = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String username;
    Common cm;
    public ListMessageUserAdapter(List<MessageUser> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message_user, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if ((Boolean) sharedPreferences.getBoolean("is_login", false)) {
            username = sharedPreferences.getString("username", "Gest");
        }
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        final String url = cm.deleteMessage() + data.get(position).getId();
        holder.txtDate.setText(data.get(position).getDate());
        holder.txtUsername.setText(data.get(position).getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("datvl", "item selected");
                final Intent intent= new Intent(v.getContext(), Chat.class);

                intent.putExtra("username2", data.get(position).getUsername());

                v.getContext().startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //delete message

                GetApi getApi = new GetApi(url, v.getContext(), new OnEventListener() {
                    @Override
                    public void onSuccess(JSONArray object) {
                        Log.d("datvl", "onSuccess: delete ok");
                        Toast.makeText( v.getContext(), "Đã xóa tin nhắn", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                //reload fragment
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fragmentManager = activity.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentMessage fragmentMessage = new FragmentMessage();
                fragmentTransaction.replace(R.id.content,fragmentMessage);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtUsername;
        ImageView imgDelete;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txt_list_date);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_list_username);
            imgDelete = itemView.findViewById(R.id.img_delete_message);
        }
    }
}
