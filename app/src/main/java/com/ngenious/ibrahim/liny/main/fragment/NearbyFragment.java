package com.ngenious.ibrahim.liny.main.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.main.MainActivity;
import com.ngenious.ibrahim.liny.main.SearchFilterDialogFragment;
import com.ngenious.ibrahim.liny.main.adapter.NearbyRvAdapter;
import com.ngenious.ibrahim.liny.main.adapter.ProfileCardsAdapter;
import com.ngenious.ibrahim.liny.model.Users;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import link.fls.swipestack.SwipeStack;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private String uid;
    private double mlatitude;
    private double mlongitude;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NearbyRvAdapter nearbyRvAdapter;
 //   private List<Users>usersList;
    private List<Users>usersList;
    private List<String>idList;
    private SwipeStack cardStack;
    private ProfileCardsAdapter profileCardsAdapter;
    private int currentPosition;
    private boolean listStatut;
    private NearbyFragmentListener nearbyFragmentListener;

    @BindView(R.id.cancelButton)ImageButton _btnCancel;
    @BindView(R.id.inviteButton)ImageButton _btnInvite;
    @BindView(R.id.placeSearch)RippleBackground _placeSearch;
    @BindView(R.id.buttonLayout)LinearLayout _buttonLayout;

    public NearbyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_nearby, container, false);
        ButterKnife.bind(this,view);
        usersList = new ArrayList<Users>();
        idList = new ArrayList<String>();
        mFirebaseInstance = mFirebaseInstance.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        cardStack = (SwipeStack) view.findViewById(R.id.container);
        final FragmentActivity nearFrag = getActivity();


        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                usersList.clear();
                getAllUsers(dataSnapshot, nearFrag);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (usersList.size()>0)

        {
            startRipple(false);
            cardsControl(nearFrag);
        }else {
            startRipple(true);
        }

        //toolbar
/*
        Toolbar _toolbarNearby = (Toolbar)view.findViewById(R.id.toolbar_nearby);
        ((AppCompatActivity)getActivity()).setSupportActionBar(_toolbarNearby);
        ImageButton _filterImageButton = (ImageButton)view.findViewById(R.id.filterImageButton);*/


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_nearby_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_filter:
                // Filter for nearby search
                nearbyFragmentListener.showingFilterDialog(true);
                return true;
            case R.id.action_destination:
                // Destination
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void getAllUsers(DataSnapshot dataSnapshot, FragmentActivity nearFrag){
        for (final DataSnapshot singleDataSnap:dataSnapshot.getChildren())
            {
                    String id = singleDataSnap.getKey();

                    idList.add(id);

                    Users users = singleDataSnap.getValue(Users.class);
                    Log.i("DataSnapshot:", "Users Class" + users.getDisplayName());
                    usersList.add(users);
                    profileCardsAdapter = new ProfileCardsAdapter(nearFrag, usersList);
                    cardStack.setAdapter(profileCardsAdapter);


            }

        }
        private void cardsControl(FragmentActivity nearFrag){
            currentPosition = 0;
            //Handling swipe event of Cards stack
            cardStack.setListener(new SwipeStack.SwipeStackListener() {
                @Override
                public void onViewSwipedToLeft(int position) {
                    currentPosition = position + 1;
                    Log.i("Swipe","SwipedToLeft");

                }

                @Override
                public void onViewSwipedToRight(int position) {
                    inviteUser(idList.get(position));
                    currentPosition = position + 1;

                }

                @Override
                public void onStackEmpty() {
                    startRipple(true);

                }
            });
            _btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cardStack.swipeTopViewToRight();
                }
            });

            _btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //    Toast.makeText(MainActivity.this, "You liked " + cardItems.get(currentPosition).getName(),Toast.LENGTH_SHORT).show();
                    cardStack.swipeTopViewToLeft();

                }
            });

        }

    private void inviteUser(String id){
        String invid = mFirebaseDatabase.child(id).child("invitation").push().getKey();
        mFirebaseDatabase.child(id).child("invitation").child(invid).setValue("KjO2l9Uz86jCkaqj5Jy");

    }
    private void startRipple(boolean listStatut){
        if (listStatut == true){
            _buttonLayout.setVisibility(View.GONE);
            _placeSearch.startRippleAnimation();
           onDetach();
            onAttach(getContext());
        }else {
            _placeSearch.stopRippleAnimation();
            _buttonLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof NearbyFragmentListener)) throw new AssertionError();
        nearbyFragmentListener=(NearbyFragmentListener)context;
    }

public interface NearbyFragmentListener{
        void showingFilterDialog(boolean state);
    }
}


