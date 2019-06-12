package com.datvl.trotot;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.datvl.trotot.adapter.ListMessageAdapter;
import com.datvl.trotot.api.GetApi;
import com.datvl.trotot.common.Common;
import com.datvl.trotot.library.NumberFormat;
import com.datvl.trotot.model.Message;
import com.datvl.trotot.post.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Chat extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Post> listPost;
    List<Message> listMessage;
    Post post;
    Common cm;
    ConstraintLayout pb;
    Animation animation;
    private DatabaseReference mDatabase;
    EditText edtSendMessage;
    DatabaseReference myRef;
    private String message_id = "0";
    private String username;
    private String username2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getMessageID();

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d("test", "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("test", "Failed to read value.", error.toException());
//            }
//        });
    }

    public void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        myRef = FirebaseDatabase.getInstance().getReference("message").child(message_id);
    }

    public String getMessageID(){
        final SharedPreferences sharedPreferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        pb = findViewById(R.id.progressBarChat);
        Intent intent = getIntent();
        username2 = (String)intent.getSerializableExtra("username2");
        if ((Boolean) sharedPreferences.getBoolean("is_login", false)) {
            username = sharedPreferences.getString("username", "Gest");
        }
        if (username.equals(username2)) {
            setDefault();
            edtSendMessage = findViewById(R.id.edt_message_content);
            edtSendMessage.setVisibility(View.GONE);
        }
        else{
            String url = cm.getMessageID() + username + "/" + username2;
            GetApi getApi = new GetApi(url, getApplication(), new OnEventListener() {
                @Override
                public void onSuccess(JSONArray object) {
                    try {
                        JSONObject jsonObject = object.getJSONObject(0);
                        message_id = jsonObject.getString("id");
                        init();
                        setDefault();
                        getListMessageFirebase();
                        sendMessage();
                        pb.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

        return message_id;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void sendMessage() {
        edtSendMessage = findViewById(R.id.edt_message_content);
        edtSendMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtSendMessage.getRight() - 2 * edtSendMessage.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        String content = String.valueOf(edtSendMessage.getText());

                        if (content.equals("")) {
                            cm.showToast(getApplication(),"Vui lòng nhập tin nhắn",Toast.LENGTH_SHORT);
                        }
                        else{
                            Message message = new Message(content, "10-04-2019", username);
                            mDatabase.child("message").child(message_id).push().setValue(message);
                            edtSendMessage.setText("");
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void setDefault(){
        animation = AnimationUtils.loadAnimation(getApplication(), R.anim.scale_list);
        recyclerView = findViewById(R.id.recycler_message_view);

        Intent intent = getIntent();
        post = (Post) intent.getSerializableExtra("post");

        TextView txtUserName, txtPrice, txtAddress, txtTime;
        final ImageView imgPost, imgHeart;
        txtUserName = (TextView) findViewById(R.id.user_name);
        txtPrice = (TextView) findViewById(R.id.id_price);
        imgPost = (ImageView) findViewById(R.id.img_view);
        txtAddress = (TextView) findViewById(R.id.address);
        txtTime = (TextView) findViewById(R.id.time);
        imgHeart = (ImageView) findViewById(R.id.item_heart);
        setTitle(username2);

        if (post != null){
            String name = post.getName();
            String name_sub = name.length() > 40 ? name.substring(0,40) + "..." : name;
            int price = post.getPrice();
            String address = post.getAddress();
            String timeAgo = post.getTime();

            txtUserName.setText(name_sub);
            txtPrice.setText("" + NumberFormat.getFormatedNum((int) price) + " đ");
            txtAddress.setText(address);
            txtTime.setText(timeAgo + " | ");

            Picasso.get()
                    .load(post.getImage())
                    .resize(60,60)
                    .into(imgPost);

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
        }
        else {
            LinearLayout post_in_chat = findViewById(R.id.post_in_chat);
            post_in_chat.setVisibility(View.GONE);
        };
    }

    public void setListMessage(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListMessageAdapter viewAdapter = new ListMessageAdapter(listMessage);
        recyclerView.setAdapter(viewAdapter);
    }

    public void getListMessageFirebase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listMessage = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Message value = postSnapshot.getValue(Message.class);
                    Log.d("DatVL", "Failed to read value." + value.getContent());
                    listMessage.add(new Message(value.getContent(), value.getTime(), value.getUser()));
                }
                setListMessage();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("DatVL", "Failed to read value.", error.toException());
            }
        });
    }
}
