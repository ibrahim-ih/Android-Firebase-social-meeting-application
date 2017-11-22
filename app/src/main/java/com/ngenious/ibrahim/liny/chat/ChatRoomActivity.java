package com.ngenious.ibrahim.liny.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.helper.ClickListener;
import com.ngenious.ibrahim.liny.model.ChatRoom;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatRoomActivity extends AppCompatActivity {
@BindView(R.id.chatroom_recyclerview)RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Toolbar toolbar;

    private List<ChatRoom>chatRoomList = new ArrayList<>();
    private List<String>chatRoomChilKeys = new ArrayList<>();
    private String uid;

    private static String TAG = "ChatRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users").child(uid).child("conversation");

        //only for test this block//
/*       long timestamp = 1496641157;
        int unreadCount = 6;

        ChatRoom
                test = new ChatRoom("idChat", "nameChat", "messageChat", timestamp , unreadCount);
        mFirebaseDatabase
                .push()
                .setValue(test);*/
        //////////////////////////

        mFirebaseDatabase
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        addChatItems(dataSnapshot);
                        Log.i(TAG, "success connect to : "+dataSnapshot.getRef());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i(TAG, "Cancel from Db connection cause : "+databaseError.getDetails());
                    }
                });



        Log.i(TAG, "Chat List Size : "+chatRoomList.size());

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ChatRoomAdapter(chatRoomList);
        mRecyclerView.setAdapter(mAdapter);
        final Intent
                intent = new Intent(this, ChatActivity.class);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Log.i(TAG,"Single Click on position : "+position+" get name : "+chatRoomList.get(position).getName());
                /*Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();*/
                intent.putExtra("roomId",chatRoomList.get(position).getId());
                intent.putExtra("uName",chatRoomList.get(position).getName());
                ChatRoom clearCountMsg
                        = new ChatRoom(0);
                mFirebaseDatabase.child(chatRoomChilKeys.get(position).toString()).child("unreadCount").setValue(0);
               startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {
               /* Toast.makeText(MainActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();*/
                Log.d(TAG,"Long press on position : "+position);

            }
        }));

    }

    private void addChatItems(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleDataSnapshot:dataSnapshot.getChildren()) {
            if (singleDataSnapshot.getValue()!= null){
                ChatRoom
                        chatRoom = singleDataSnapshot.getValue(ChatRoom.class);
                chatRoomList
                        .add(chatRoom);
                chatRoomChilKeys
                        .add(singleDataSnapshot.getKey());

            }
            //****** NOTE A NE PAS OUBLIER *****///
            //****** ici le ELSE pour afficher une Liste vide *****///
        }





    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
