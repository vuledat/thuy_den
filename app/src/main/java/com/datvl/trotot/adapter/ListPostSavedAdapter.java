package com.datvl.trotot.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.datvl.trotot.PostDetail;
import com.datvl.trotot.R;
import com.datvl.trotot.library.NumberFormat;
import com.datvl.trotot.post.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListPostSavedAdapter extends RecyclerView.Adapter<ListPostSavedAdapter.RecyclerViewHolder>{

    private List<Post> data = new ArrayList<>();

    public static final long MAGIC=86400000L;
    Animation animation;

    public ListPostSavedAdapter(List<Post> data) {
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home, parent, false);

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
        holder.txtTime.setText(timeAgo + " ");

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

        holder.imgHeart.setOnClickListener(new View.OnClickListener() {
            int checked = 0;
            @Override
            public void onClick(View v) {
                if (checked == 0){
                    holder.imgHeart.setImageResource(R.drawable.heart_active);
                    checked = 1;
                    holder.imgHeart.startAnimation(animation);
                }
                else{
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

    public int DateToDays (Date date){
        //  convert a date to an integer and back again
        long currentTime=date.getTime();
        currentTime=currentTime/MAGIC;
        return (int) currentTime;
    }

}
