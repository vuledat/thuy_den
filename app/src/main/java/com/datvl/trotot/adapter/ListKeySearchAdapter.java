package com.datvl.trotot.adapter;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datvl.trotot.Chat;
import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.OnItemClickListener;
import com.datvl.trotot.R;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.fragment.FragmentMessage;
import com.datvl.trotot.fragment.FragmentSearch;
import com.datvl.trotot.model.KeySearch;
import com.datvl.trotot.model.MessageUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ListKeySearchAdapter extends RecyclerView.Adapter<ListKeySearchAdapter.RecyclerViewHolder>{

    private List<KeySearch> data = new ArrayList<>();
    private OnItemClickListener listener;
    SharedPreferences sharedPreferences;
    String username;
    Common cm;
    public ListKeySearchAdapter(List<KeySearch> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_key_search, parent, false);
        sharedPreferences = parent.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if ((Boolean) sharedPreferences.getBoolean("is_login", false)) {
            username = sharedPreferences.getString("username", "Gest");
        }
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {

        final String url = cm.deleteKeyword() + data.get(position).getId();
        holder.txtKeyword.setText(data.get(position).getKeyword());
        holder.txtStt.setText("" + position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("datvl", "item selected");
            }
        });

        holder.bind(data.get(position), listener);

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //delete message

                GetApi getApi = new GetApi(url, v.getContext(), new OnEventListener() {
                    @Override
                    public void onSuccess(JSONArray object) {
                        Log.d("datvl", "onSuccess: delete ok");
                        Toast.makeText( v.getContext(), "Đã xóa từ khóa", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                //reload fragment
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                FragmentManager fragmentManager = activity.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentSearch fragmentMessage = new FragmentSearch();
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
        TextView txtKeyword, txtStt;
        ImageView imgDelete;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtKeyword = (TextView) itemView.findViewById(R.id.txt_key_word);
            imgDelete = itemView.findViewById(R.id.img_delete_key);
            txtStt = itemView.findViewById(R.id.txt_list_stt_key);
        }

        public void bind(final KeySearch item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
