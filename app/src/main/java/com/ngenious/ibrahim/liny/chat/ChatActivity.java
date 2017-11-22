package com.ngenious.ibrahim.liny.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Chat;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    @BindView(R.id.chat_recyclerview)RecyclerView mRecyclerView;
    @BindView(R.id.message)TextView _message;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseUser firebaseUser;
    private List<Chat> chatList = new ArrayList<>();
    private String uName, roomId;
    private int limitMessage = 5;
    private String friendsName;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uName = firebaseUser.getDisplayName();

        // roomId depuid ChatRoomActivity intent
        mFirebaseInstance = FirebaseDatabase.getInstance();
        Intent
                intent = getIntent();
        roomId = intent.getStringExtra("roomId");
        friendsName = intent.getStringExtra("uName");

        mFirebaseDatabase = mFirebaseInstance.getReference("chatRooms").child(roomId);



        mFirebaseDatabase.limitToLast(limitMessage).orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        addChatItems(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatAdapter(getApplicationContext(),chatList,uName);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void addChatItems(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleDataSnapshot:dataSnapshot.getChildren()) {
            if (singleDataSnapshot.getValue()!= null){
                Chat
                        chatRoom = singleDataSnapshot.getValue(Chat.class);
                chatList
                        .add(chatRoom);


            }
            //****** NOTE A NE PAS OUBLIER *****///
            //****** ici le ELSE pour afficher une Liste vide *****///
        }

    }
    @OnClick(R.id.btn_send)
    public void onClick(){
//        long createdAt = new Date().getTime();
        /*Calendar calendar = Calendar.getInstance();
        long createdAt =  Calendar.getInstance().get(Calendar.MILLISECOND);
        createdAt = createdAt /1000;*/
        if (!_message.getText().toString().isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            long date = calendar.getTime().getTime();
            long createdAt = date / 1000;
            String message = _message.getText().toString();
            Users users = new Users(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
            Chat chat =
                    new Chat(message, createdAt, users, firebaseUser.getUid());
            mFirebaseDatabase
                    .push()
                    .setValue(chat);
            _message
                    .setText("");
            chatList.clear();
            mAdapter
                    .notifyDataSetChanged();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_UP){
            limitMessage += limitMessage;
            mAdapter
                    .notifyDataSetChanged();
        }

        return super.onTouchEvent(event);

    }
}