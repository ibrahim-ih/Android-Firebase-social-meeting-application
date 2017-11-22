package com.ngenious.ibrahim.liny.foursquare.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.foursquare.adapter.HistoryDestAdapter;
import com.ngenious.ibrahim.liny.model.Destination;
import com.ngenious.ibrahim.liny.model.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDestFragment extends Fragment {

    private List<Destination> destinationList = new ArrayList<>();
    private List<History> historiesList = new ArrayList<>();
//    private List<History> pickedList = new ArrayList<>();
    private RecyclerView recyclerView;
    private HistoryDestAdapter mAdapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabaseDest;
    private String invitAvailableId;
    private TextView emptyListMsg ;
    Handler handler;
    FirebaseUser user;
    private static final String TAG =  "HistoryDestFragment";
    private boolean readyInvitCard;


    @BindView(R.id.card_hist_picked)CardView _cardPicked;
    @BindView(R.id.placeNamePicked)TextView _placeNamePicked;
    @BindView(R.id.locationPlaceLinearLayout)LinearLayout _locationPlaceLinearLayout;
    @BindView(R.id.locationPlaceImageView)ImageView _locationPlaceImageView;
    @BindView(R.id.locationPlacePickedTextView)TextView _locationPlacePickedTextView;
    @BindView(R.id.timePlacePicked)TextView _timePlacePicked;



    public HistoryDestFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_dest, container, false);
        ButterKnife.bind(this, view);
        emptyListMsg = (TextView)view.findViewById(R.id.emptyList);
        recyclerView = (RecyclerView) view.findViewById(R.id.h_dest_recycler_view);

        final FragmentActivity fragmentActivity = getActivity();

        mAdapter = new HistoryDestAdapter(fragmentActivity, destinationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        readyInvitCard = false;

        mFirebaseInstance = mFirebaseInstance.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
         user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("Hist", "uid : "+user.getUid());
        mFirebaseDatabase.child(user.getUid()).child("history").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prepareDestData(dataSnapshot.child("invitNotAvailable"), fragmentActivity);

                prepareValidHistData(dataSnapshot.child("invitAvailable"));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        // for the picked place

        mFirebaseDatabase.child(user.getUid()).child("history").child("invitAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                preparePickedDestData(dataSnapshot, fragmentActivity);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/




        return view;
    }

    private void prepareValidHistData(DataSnapshot invitAvailable) {
        for (final DataSnapshot singleDataSnap:invitAvailable.getChildren()) {
            Log.i(TAG,"getKey"+ singleDataSnap.getKey()); // invitationId
            Log.i(TAG,"getChildren"+ singleDataSnap.getChildren()); //com.google.firebase.database.DataSnapshot$1@b9944a1
            Log.i(TAG,"getValue"+ singleDataSnap.getValue()); //-Kl3ictQgL_0DnVTuXLz

            if (singleDataSnap.getValue() != null){
                Log.i(TAG, "getValue not null");
                invitAvailableId = singleDataSnap.getValue(String.class);
                Log.i(TAG, "invitAvailableId"+invitAvailableId);
                readyInvitCard = true;

            }

        }
    }

    private void prepareDestData(DataSnapshot dataSnapshot, FragmentActivity fragmentActivity){
        for (final DataSnapshot singleDataSnap:dataSnapshot.getChildren())
        {

            History history = singleDataSnap.getValue(History.class);
            historiesList.add(history);

        }
        if (historiesList.size() == 0){
            emptyListMsg.setVisibility(View.VISIBLE);
        }else {
            emptyListMsg.setVisibility(View.GONE);
            prepareAllDest();
        }
    }

/*    private void preparePickedDestData(DataSnapshot dataSnapshot, FragmentActivity fragmentActivity){
        for (final DataSnapshot singleDataSnap:dataSnapshot.getChildren())
        {

            History history = singleDataSnap.getValue(History.class);
            pickedList.add(history);

        }
    }*/


    private void prepareAllDest(){
        mFirebaseDatabaseDest = mFirebaseInstance.getReference("destination");
            mFirebaseDatabaseDest
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot singleDataSnap:dataSnapshot.getChildren()){

                                //check for ready invit card
                                if (readyInvitCard)
                                {
                                    String keyLoop = singleDataSnap.getKey();
                                    if (keyLoop.equals(invitAvailableId))
                                    {
                                        Destination destinationAvailable = singleDataSnap.getValue(Destination.class);
                                        _placeNamePicked.setText(destinationAvailable.getPlaceName());
                                        _locationPlacePickedTextView.setText(destinationAvailable.getAddress());

                                        Long unixTimestamp = destinationAvailable.getTimestampUnix();
                                        Date date = new Date(unixTimestamp * 1000);

                                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                                        String timeOutput = timeFormat.format(date);
                                        Calendar todayDate = Calendar.getInstance();

                                        _cardPicked.setVisibility(View.VISIBLE);
                                        // je doit ici verifier la date et leur
//                                        la date est cv le problem c'est au niveau du Time

                                     /*   Log.i(TAG, "TIME AND DATE : " +
                                                "unixTimestamp (long) : "+unixTimestamp+" " +
                                                "date : "+date+ " " +
                                                "timeOutput : "+timeOutput+ " " +
                                                "TodayDate get time : "+todayDate.getTime()+ " "+
                                                "todayDate.getTIME"+ todayDate.getTime());

                                        Log.i(TAG, "TEST CONDITIONS : "+
                                        "todayDate.after(date)" + todayDate.after(date)+
                                        "todayDate.getTime().equals(date)"+todayDate.getTime().equals(date)+
                                        "todayDate.getTime().getTime()"+todayDate.getTime().getTime()+
                                        "date.getTime() "+date.getTime()+
                                        "date.after(todayDate.getTime()"+date.after(todayDate.getTime()));
                                        Log.i(TAG, " variable : today date  :"+todayDate+
                                                "\n"+"todaydate.getTime"+todayDate.getTime()+
                                                "\n"+"todayDate.getTime().getTime()"+todayDate.getTime().getTime()+
                                        "\n"+"date.getTime"+date.getTime()+
                                        "\n"+"date"+date);*/
                                        Log.i(TAG, "TodayDate : "+todayDate.getTime()+ "date : "+ date);

                                       if (todayDate.getTime().after(date)){
                                            mFirebaseDatabase
                                                    .child(user.getUid())
                                                    .child("history")
                                                    .child("invitAvailable")
                                                    .removeValue();
                                            Log.i(TAG, "Condition true");
                                           _cardPicked.setVisibility(View.GONE);
                                           //refresh
                                        onDetach();
                                        onAttach(getContext());

                                        }else {
                                           _timePlacePicked.setText(timeOutput);
                                       }

                                        Log.i(TAG, "Today date"+todayDate.after(date));

/*                                        _cardPicked;
                                        _placeNamePicked;
                                        _locationPlaceLinearLayout;
                                        _locationPlaceImageView;
                                        _locationPlacePickedTextView;
                                        _timePlacePicked;*/


                                        Log.i(TAG,"getAddress"+ destinationAvailable.getAddress());

                                    }
                                }



                                Log.i(TAG, "Children Keys : "+singleDataSnap.getKey());
                                Log.i(TAG, "HistoriesList : "+historiesList.size());
                                if (historiesList.size()>0) {
                                            if (checkIfDestFound(singleDataSnap.getKey())) {
                                                Log.i(TAG, "check : " + "true");

                                                Destination destination = singleDataSnap.getValue(Destination.class);
                                                destinationList
                                                        .add(destination);


                                            }
                                }


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

/*        if (pickedList.size()>0){
            mFirebaseDatabaseDest
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot secondDataSnap:dataSnapshot.getChildren()){
                                {
                                    if (checkIfPickedFound(secondDataSnap.getKey())){
                                        //show picked
                                    }
                                }}
                            }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }*/

    private boolean checkIfDestFound(String destKey){
        boolean found = false;

        for (History history : historiesList){
            if (history.getInvitationId().equals(destKey)){
                found = true;
            }
        }

        return found;
    }
/*    private boolean checkIfPickedFound(String key) {

        boolean found = false;

        for (History history : pickedList){
            if (history.getInvitationId().equals(key)){
                found = true;
            }
        }

        return found;
    }*/
}
