package com.ngenious.ibrahim.liny.activity.friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Friend;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FriendsListActivity extends AppCompatActivity {
private List<Friend>list = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getFriends();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.friendsRecyclerView);

      recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FriendsAdapter(list));
ajouterVilles();
    }
    private void getFriends()
    {
        mFirebaseDatabase.child(uid).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
  //              Friend friend = dataSnapshot.getValue(Friend.class);
//                list.add(new Friend(friend.getDisplayName(), friend.getPicture()));
               /* Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext())
                {
                    Friend value =iterator.next().getValue(Friend.class);
                    list.add(value);
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void ajouterVilles() {
        list.add(new Friend("France","http://www.telegraph.co.uk/travel/destination/article130148.ece/ALTERNATES/w620/parisguidetower.jpg"));
        list.add(new Friend("Angleterre","http://www.traditours.com/images/Photos%20Angleterre/ForumLondonBridge.jpg"));
        list.add(new Friend("Allemagne","http://tanned-allemagne.com/wp-content/uploads/2012/10/pano_rathaus_1280.jpg"));
        list.add(new Friend("Espagne","http://www.sejour-linguistique-lec.fr/wp-content/uploads/espagne-02.jpg"));
        list.add(new Friend("Italie","http://retouralinnocence.com/wp-content/uploads/2013/05/Hotel-en-Italie-pour-les-Vacances2.jpg"));
        list.add(new Friend("Russie","http://www.choisir-ma-destination.com/uploads/_large_russie-moscou2.jpg"));
    }
}
