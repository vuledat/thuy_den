package com.datvl.trotot.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.datvl.trotot.OnEventListener;
import com.datvl.trotot.PostDetail;
import com.datvl.trotot.R;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.library.NumberFormat;
import com.datvl.trotot.post.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListPostAdapter extends RecyclerView.Adapter<ListPostAdapter.RecyclerViewHolder>{

    private List<Post> data = new ArrayList<>();

    Animation animation;
    ViewGroup view;
    Common cm;
    public String urlDelete = cm.getUrlDelete();
    public String urlSave = cm.getUrlPostSaved();

    public ListPostAdapter(List<Post> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = parent;
        SharedPreferences sharedPreferences = parent.getContext().getSharedPreferences("fillter", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String view_type = sharedPreferences.getString("view_type", "Grid View");
        int item_home_grid = R.layout.item_home_grid;
        switch (view_type) {
            case "Grid View":
                item_home_grid = R.layout.item_home_grid;
                break;
            case "List View":
                item_home_grid = R.layout.item_home;
                break;
        }

        View view = inflater.inflate(item_home_grid, parent, false);

         animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.scale_list);
//        view.startAnimation(animation);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        String name = data.get(position).getName();
        String name_sub = name.length() > 40 ? name.substring(0,40) + "..." : name;
        int price = data.get(position).getPrice();
        String address = data.get(position).getAddress();
        String timeAgo = data.get(position).getTime();

        holder.txtUserName.setText(name_sub);
        holder.txtPrice.setText("" + NumberFormat.getFormatedNum((int) price) + " đ");
        holder.txtAddress.setText(address);
        holder.txtTime.setText(timeAgo + " | ");

        Picasso.get()
                .load(data.get(position).getImage())
                .resize(60,60)
                .into(holder.imgPost);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent= new Intent(v.getContext(), PostDetail.class);

                intent.putExtra("post", data.get(position));

                v.getContext().startActivity(intent);
            }
        });

        int checked = data.get(position).getIs_save();

        if (checked == 1){
            holder.imgHeart.setImageResource(R.drawable.heart_active);
            holder.imgHeart.startAnimation(animation);
        }
        else
        {
            holder.imgHeart.setImageResource(R.drawable.heart);
            holder.imgHeart.startAnimation(animation);
        }

        holder.imgHeart.setOnClickListener(new View.OnClickListener() {
            int checked = data.get(position).getIs_save();
            SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String user_id = sharedPreferences.getString("user_id", "0");
            @Override
            public void onClick(View v) {
                if (checked == 0){
                    GetApi getApi = new GetApi(urlSave + data.get(position).getId() + "/" + user_id, view.getContext(), new OnEventListener() {
                        @Override
                        public void onSuccess(JSONArray object) {
                            cm.showToast(view.getContext(), "Đã lưu lại tin", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            cm.showToast(view.getContext(), "Lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT);
                        }
                    });
                    holder.imgHeart.setImageResource(R.drawable.heart_active);
                    checked = 1;
                    holder.imgHeart.startAnimation(animation);
                }
                else{
                    GetApi getApi = new GetApi(urlDelete + data.get(position).getId() + "/" + user_id, view.getContext(), new OnEventListener() {
                        @Override
                        public void onSuccess(JSONArray object) {
                            cm.showToast(view.getContext(), "Đã huỷ theo dõi tin này", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            cm.showToast(view.getContext(), "Lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT);
                        }
                    });
                    holder.imgHeart.setImageResource(R.drawable.heart);
                    checked =0;
                    holder.imgHeart.startAnimation(animation);
                }
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
        TextView txtUserName, txtPrice, txtAddress, txtTime;
        ImageView imgPost, imgHeart;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.user_name);
            txtPrice = (TextView) itemView.findViewById(R.id.id_price);
            imgPost = (ImageView) itemView.findViewById(R.id.img_view);
            txtAddress = (TextView) itemView.findViewById(R.id.address);
            txtTime = (TextView) itemView.findViewById(R.id.time);
            imgHeart = (ImageView) itemView.findViewById(R.id.item_heart);

        }
    }

}
