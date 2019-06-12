package com.datvl.trotot;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.library.NumberFormat;
import com.datvl.trotot.post.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostDetail extends AppCompatActivity {

    Post post;
    Common cm;
    private ProgressDialog pg = null;
    public String url = cm.getUrlPost();
    String phone = "191";
    Button btn_call, btn_sms, btn_chat;

    ConstraintLayout postDetail;
    ProgressBar pb;
    Animation animation;
    String created_by;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        pb = (ProgressBar)findViewById(R.id.progressBarDetail);
        postDetail = (ConstraintLayout)findViewById(R.id.post_detail);
        animation = AnimationUtils.loadAnimation(getApplication(), R.anim.scale_list);


//      ẩn nội dung trước khi load được dữ liệu
        postDetail.setVisibility(View.GONE);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");

        final TextView namePost = (TextView) findViewById(R.id.text_message);
        final ImageView imgPost = (ImageView) findViewById(R.id.imgPost);
        final ImageView imgHeart = findViewById(R.id.post_heart_active);
        final ImageView imgAvatar = (ImageView) findViewById(R.id.avatar);
        final TextView txtPrice = (TextView) findViewById(R.id.price);
        final TextView txtContent = (TextView) findViewById(R.id.content);
        final TextView txtScale = (TextView) findViewById(R.id.post_scale);
        final TextView txtTimeAgo = (TextView) findViewById(R.id.timeAgo);
        final TextView txtDateSignUp = (TextView) findViewById(R.id.date_sign_up);
        final TextView txtAddressUser = (TextView) findViewById(R.id.address_user);
        final TextView txtUserName = (TextView) findViewById(R.id.name_user);
        btn_call = findViewById(R.id.button_call);
        btn_sms = findViewById(R.id.button_sms);
        btn_chat = findViewById(R.id.button_chat);

        GetApi getApi = new GetApi(url + post.getId(),getApplication(), new OnEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(JSONArray o) {
                try {
                    JSONObject jsonObject = o.getJSONObject(0);
                    namePost.setText(jsonObject.getString("name"));
                    Picasso.get()
                            .load(jsonObject.getString("image"))
                            .into(imgPost);

                    Picasso.get()
                            .load(jsonObject.getString("user_avatar"))
                            .fit()
                            .into(imgAvatar);

                    txtContent.setText(jsonObject.getString("content"));
                    int price = Integer.parseInt(jsonObject.getString("price"));
                    txtPrice.setText("" + NumberFormat.getFormatedNum(price) + " đ");
                    txtScale.setText(" Diện tích: " + Integer.parseInt(jsonObject.getString("scale")) + ": " + getString(R.string.met));
                    txtTimeAgo.setText("" + jsonObject.getString("created_at_string"));
                    txtDateSignUp.setText("Đã tham gia: " + jsonObject.getString("user_created_at"));
                    txtAddressUser.setText("" + jsonObject.getString("user_address"));
                    txtUserName.setText("" + jsonObject.getString("user_name"));
                    phone = jsonObject.getString("user_phone");
                    created_by = jsonObject.getString("user_name");
                    postDetail.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        });



        imgHeart.setOnClickListener(new View.OnClickListener() {
            int checked = 0;
            @Override
            public void onClick(View v) {
                if (checked == 0){
                    imgHeart.setImageResource(R.drawable.heart_active);
                    checked = 1;
                    imgHeart.startAnimation(animation);
                }
                else{
                    imgHeart.setImageResource(R.drawable.heart);
                    checked =0;
                    imgHeart.startAnimation(animation);

                }
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
            }
        });

        btn_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body","" + post.getName());
                intent.setData(Uri.parse("sms:" + phone));
                startActivity(intent);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent= new Intent(v.getContext(), Chat.class);

                intent.putExtra("post", post);
                intent.putExtra("username2", created_by);

                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_call:
                  startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                return true;
            case R.id.button_share:
//                showHelp();
                return true;
            case R.id.button_message:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body","" + post.getName());
                intent.setData(Uri.parse("sms:" + phone));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
