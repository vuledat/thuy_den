package com.datvl.trotot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datvl.trotot.R;
import com.datvl.trotot.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ListAreaAdapter extends RecyclerView.Adapter<ListAreaAdapter.RecyclerViewHolder>{

    private List<Message> data = new ArrayList<>();
    SharedPreferences sharedPreferences;
    String username;


    public ListAreaAdapter(List<Message> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_message, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if ((Boolean) sharedPreferences.getBoolean("is_login", false)) {
            username = sharedPreferences.getString("username", "Gest");
        }
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {


        holder.txtContent.setText(data.get(position).getContent());
        if (username.equals(data.get(position).getUser())) {
            holder.txtContent.setGravity(Gravity.RIGHT | Gravity.CENTER);
            holder.txtMyUser.setVisibility(View.VISIBLE);
            holder.txtMyUser.setText(username);
        }
        else {
            holder.txtContent.setGravity(Gravity.LEFT | Gravity.CENTER);
            holder.txtUser2.setVisibility(View.VISIBLE);
            holder.txtUser2.setText(data.get(position).getUser());
        }
    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent,txtUser2, txtMyUser;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtContent = (TextView) itemView.findViewById(R.id.message_content);
            txtUser2 = (TextView) itemView.findViewById(R.id.txt_user_2);
            txtMyUser = (TextView) itemView.findViewById(R.id.txt_my_user);
        }
    }
}
