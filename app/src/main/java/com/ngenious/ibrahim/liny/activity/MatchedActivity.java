package com.ngenious.ibrahim.liny.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.adapter.FriendListAdapter;
import com.ngenious.ibrahim.liny.chat.ChatRoomActivity;
import com.ngenious.ibrahim.liny.fragment.FriendsFragment;
import com.ngenious.ibrahim.liny.fragment.InvitationFragment;
import com.ngenious.ibrahim.liny.model.Chat;
import com.ngenious.ibrahim.liny.model.ChatRoom;
import com.ngenious.ibrahim.liny.model.Destination;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.ArrayList;
import java.util.List;

public class MatchedActivity extends AppCompatActivity implements InvitationFragment.OnFragmentInteractionListener,FriendsFragment.OnFriendsFragmentInteractionListener {
    private static String TAG =  "MatchedActivity";
    private TextView mTextMessage;
    private DatabaseReference mFirebaseDatabase,mFirebaseDatabaseGlobal;
    private DatabaseReference mDestFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String uid;
    private FirebaseAuth mAuth;
    private List<String> invitationList = new ArrayList<String>();
    private List<Destination> destinationList = new ArrayList<>();
    private List<String> peopleIdList = new ArrayList<>();
    private List<String> destitaionIdList = new ArrayList<>();



    private List<String> invitationChildKeyList = new ArrayList<>();
    private List<String> friendsList = new ArrayList<>();
    private List<Users>friendsDetailsList = new ArrayList<>();
    private InvitationFragment invitationFragment;
    private FriendsFragment friendsFragment;
    private Toolbar toolbar;

    private FriendListAdapter friendListAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        initViews();

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTextMessage = (TextView) findViewById(R.id.message);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabaseGlobal = mFirebaseInstance.getReference("chatRooms");
        mFirebaseDatabase.child(uid).child("friends")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        putInvitionList(dataSnapshot.child("invitation"));
                        putFriendsList(dataSnapshot.child("matched"));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

/*    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }*/
    private void loadFriendsList(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_matched_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                friendListAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();


    }

    private void putInvitionList(DataSnapshot dataSnapshot){
        Log.i(TAG, "DataSnapshot : ");

        for (DataSnapshot singleDataSnapshot:dataSnapshot.getChildren()){
            Log.i(TAG, "DataSnapshot getChildren : "+singleDataSnapshot.getChildren());
            Log.i(TAG, "DataSnapshot getKey : "+singleDataSnapshot.getKey());
            String value = singleDataSnapshot.getValue(String.class);
            String childValue = singleDataSnapshot.getKey();
            Log.i(TAG, "DataSnapshot value : "+value);

            invitationList.add(value);
            invitationChildKeyList.add(childValue);
            Log.i(TAG, "invitationList isEmpty : "+invitationList.isEmpty());
            Log.i(TAG, "invitationList size : "+invitationList.size());

        }

        Log.d(TAG, "InvitationList size : "+ invitationList.size());

        if (!invitationList.isEmpty())
        {
            mDestFirebaseDatabase  = mFirebaseInstance.getReference("destination");
            mDestFirebaseDatabase
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getDestinationList(dataSnapshot);
                            //dont forget to delete from list


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }



    }
    private void getDestinationList(DataSnapshot dataSnapshot){
        //
        for (DataSnapshot singleDataSnapshot:dataSnapshot.getChildren()) {
            Log.i(TAG, "destination : DataSnapshot getChildren : "+singleDataSnapshot.getChildren());
            Log.i(TAG, "destination : DataSnapshot getKey : "+singleDataSnapshot.getKey());

            String value = singleDataSnapshot.getKey();
            Log.i(TAG, "destination : DataSnapshot value : "+value);
            for (int i = 0; i < invitationList.size() ; i++) {

                if (value.equals(invitationList.get(i))){
                    //add to list
                    Destination destination = singleDataSnapshot.getValue(Destination.class);
                    destinationList
                            .add(destination);
                    peopleIdList
                            .add(destination.getFrom());
                    Log.d(TAG, "peopleIdList : "+peopleIdList.isEmpty());
                    Log.d(TAG, "peopleIdList : "+peopleIdList.size());
                    Log.d(TAG , "dest.getKey : "+ singleDataSnapshot.getKey());
                    Log.d(TAG , "dest.getChildren: "+ singleDataSnapshot.getChildren());
                    Log.d(TAG, "peopleIdGetFromDest : "+destination.getFrom());
                    destitaionIdList
                            .add(
                                    singleDataSnapshot.getKey()

                            );
                    Log.d(TAG, "invitation recu : destination is empty : "+destinationList.isEmpty());

                }

            }

        }
      /*  if (!destinationList.isEmpty()) {
            for (int i = 0; i < destinationList.size(); i++) {
                String invitFriendsID = destinationList.get(i).getFrom();
                Log.d(TAG, "invitFriendsID : " + invitFriendsID);
                invitFriendsList.add(invitFriendsID);
            }
        }

        Log.d(TAG, "invitFriendsList : "+invitFriendsList.size());
        Log.d(TAG, "invitFriendsList : "+invitFriendsList.isEmpty());*/

        invitationFragment = InvitationFragment.newInstance(destinationList,peopleIdList,destitaionIdList);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.invitation_content, invitationFragment)
                    .commit();

        }

    private void putFriendsList(DataSnapshot dataSnapshot) {
        Log.i(TAG, "putFriendsList DataSnapshot : ");

        for (DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()) {
            Log.i(TAG, "putFriendsList DataSnapshot getChildren : " + singleDataSnapshot.getChildren());
            Log.i(TAG, "putFriendsList DataSnapshot getKey : " + singleDataSnapshot.getKey());
            String value = singleDataSnapshot.getValue(String.class);
            Log.i(TAG, "putFriendsList DataSnapshot value : " + value);

            friendsList.add(value);
            Log.i(TAG, "putFriendsList isEmpty : " + friendsList.isEmpty());
            Log.i(TAG, "putFriendsList size : " + friendsList.size());

        }

        Log.d(TAG, "putFriendsList size : " + friendsList.size());
        Log.d(TAG,"firebaseDatabaseRef  : "+mFirebaseDatabase.getRef());
        if (!friendsList.isEmpty()) {
            for (int i = 0; i < friendsList.size(); i++) {
                String friendsID = friendsList.get(i);
                Log.d(TAG,"friendsID : "+friendsID);
                mFirebaseDatabase
                        .child(friendsID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                    Users users = dataSnapshot.getValue(Users.class);
                                    friendsDetailsList
                                            .add(users);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
//            friendListAdapter = new FriendListAdapter(getApplicationContext(),friendsDetailsList);
//           mRecyclerView.setAdapter(friendListAdapter);
            friendsFragment = FriendsFragment.newInstance(friendsDetailsList, friendsList);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.friends_content,friendsFragment)
                    .commit();


        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddFriend(String accepted, String peopleId , String displayNameDest) {
       if(accepted!=null){
            acceptInvitation(accepted, peopleId, displayNameDest);
        }
    }

    private void acceptInvitation(String accepted, String peopleId,String displayNameDest) {
        Log.d(TAG, "Accepted : "+accepted);
        Log.d(TAG, "peopleId : "+peopleId);

        if(accepted!=null){
            acceptedInvitation(accepted, peopleId, displayNameDest);
        }
    }
    private void acceptedInvitation(String accepted, String peopleId,String displayNameDest){
        Log.d(TAG , "testtttt get key"+mFirebaseDatabase.child(uid).child("friends")
                .child("matched")
                .child(accepted)
                .getKey());



        for (int i = 0; i <invitationList.size() ; i++) {
            Log.d(TAG, " List test : "+ invitationList.get(i));
            Log.d(TAG, " List Child test : "+ invitationChildKeyList.get(i));
            Log.d(TAG,"-"+invitationList.get(i) );
            if (accepted.equals(invitationList.get(i))){
                Log.d(TAG, " ignored id test : "+ invitationChildKeyList.get(i));


                mFirebaseDatabase.child(uid).child("friends")
                        .child("matched")
                        .push()
                        .setValue(peopleId);

                mFirebaseDatabase.child(uid).child("friends")
                        .child("invitation")
                        .child(invitationChildKeyList.get(i))
                        .removeValue();


                createConversation(peopleId, displayNameDest);

//                                refrechActivity();

                        }
                    }
                }

                private void createConversation(String peopleId, String displayNameDest)
                {
                    String idConversation = uid+"--"+peopleId;
                    String nameConversationUid = mAuth.getCurrentUser().getDisplayName();
                    String nameConversationPeopleId = displayNameDest ;
                    long timestamp = 1496641157;
                    int unreadCount = 1;

                    ChatRoom conversationUid = new ChatRoom(idConversation, nameConversationPeopleId,"Say Hello", timestamp , unreadCount);
                    ChatRoom conversationPeopleId = new ChatRoom(idConversation, nameConversationUid,"Accepted Your Match", timestamp , unreadCount);
                    String firstKey = mFirebaseDatabase.child(uid).child("conversation")
                            .push().getKey();

                    mFirebaseDatabase.child(uid).child("conversation").child(firstKey)
                            .setValue(conversationUid);

                    String secondKey = mFirebaseDatabase.child(peopleId)
                            .child("conversation")
                            .push()
                            .getKey();
                    mFirebaseDatabase.child(peopleId)
                            .child("conversation")
                            .child(secondKey)
                            .setValue(conversationPeopleId);

                    /*mFirebaseDatabaseGlobal
                            .child(idConversation);*/


                    friendsFragment
                            .onDetach();
                    invitationFragment
                            .onDetach();

                    invitationList.clear();
                    invitationChildKeyList.clear();
                    destinationList.clear();
                    destitaionIdList.clear();
                    friendsList.clear();
                    peopleIdList.clear();
                    friendsDetailsList.clear();





                    Intent chatRoomIntent = new Intent(this, ChatRoomActivity.class);
                    startActivity(chatRoomIntent);
                    finish();
                }

    @Override
    public void onDeleteFriend(String ignored) {
        if(ignored!=null){
            ignoredInvitation(ignored);
        }
    }
    private void ignoredInvitation(String ignored) {


        for (int i = 0; i <invitationList.size() ; i++) {
            Log.d(TAG, " List test : "+ invitationList.get(i));
            Log.d(TAG, " List Child test : "+ invitationChildKeyList.get(i));
            Log.d(TAG, "IGNORED VALUE : "+ignored);
            Log.d(TAG,"-"+invitationList.get(i) );
            if (ignored.equals(invitationList.get(i))){
                Log.d(TAG, " ignored id test : "+ invitationChildKeyList.get(i));

/*                mFirebaseDatabase.child(uid).child("friends")
                        .child("invitation")
                        .child(invitationChildKeyList.get(i))
                        .removeValue();
                refrechActivity();*/

                final int finalI = i;
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                mFirebaseDatabase.child(uid).child("friends")
                                        .child("invitation")
                                        .child(invitationChildKeyList.get(finalI))
                                        .removeValue(); // if is succeful do
                                   /* friendsFragment
                                            .onDetach();
                                    invitationFragment
                                            .onDetach();

                                    invitationList.clear();
                                    invitationChildKeyList.clear();
                                    destinationList.clear();
                                    destitaionIdList.clear();
                                    friendsList.clear();
                                    peopleIdList.clear();
                                    friendsDetailsList.clear();*/
                                    refrechActivity();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
//                invitationChildKeyList.get(i);
            }
        }


       /* mFirebaseDatabase
                .child("friends")
                .child("invitation")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot singleDataSnapshot:dataSnapshot.getChildren()){
                            String value = singleDataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value : "+singleDataSnapshot.getKey());


                        }
*//*                        if (value.equals(ignored))
                        {
                            mFirebaseDatabase
                                    .child("friends")
                                    .child("invitation")
                                    .child(value)
                                    .removeValue();

                        }*//*
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

/*        mFirebaseDatabase.child(uid)
                .child("friends")
                .child("invitation")
                .child(ignored)
                .removeValue();*/
        Log.d(TAG, "Delete : "+ignored+" from your Invitation List");

    }
    private void refrechActivity()
    {
        friendsFragment
                .onDetach();
        invitationFragment
                .onDetach();

        invitationList.clear();
        invitationChildKeyList.clear();
        destinationList.clear();
        destitaionIdList.clear();
        friendsList.clear();
        peopleIdList.clear();
        friendsDetailsList.clear();
        finish();
        startActivity(getIntent());
    }
}
