package com.ngenious.ibrahim.liny.main;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.main.fragment.CenteredTextFragment;
import com.ngenious.ibrahim.liny.main.fragment.ChatFragment;
import com.ngenious.ibrahim.liny.main.fragment.NearbyFragment;
import com.ngenious.ibrahim.liny.main.menu.DrawerAdapter;
import com.ngenious.ibrahim.liny.main.menu.DrawerItem;
import com.ngenious.ibrahim.liny.main.menu.SimpleItem;
import com.ngenious.ibrahim.liny.main.menu.SpaceItem;
import com.ngenious.ibrahim.liny.model.Destination;
import com.ngenious.ibrahim.liny.model.LocationModel;
import com.ngenious.ibrahim.liny.profile.SimpleProfileActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;


import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NearbyFragment.NearbyFragmentListener {

    private static final int POS_NEARBY = 0;
    private static final int POS_MESSAGES = 1;
    private static final int POS_NOTIFICATION = 2;
    private static final int POS_MATCHED = 3;
    private static final int POS_SETTINGS = 5;
    private static final String TAG = "MainActivity";
    public static final String MY_LOCATION_PREFS = "";
    public static final String MY_PROFILE_INFO_PREFS = "";

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private String uid;
    private FirebaseUser firebaseUser;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private DatabaseReference mFirebaseDatabase;

    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDestinastionDatabase;

    private DatabaseReference mInvitationDatabase;
private String invitValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_NEARBY).setChecked(true),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_NOTIFICATION),
                createItemFor(POS_MATCHED),
                new SpaceItem(48),
                createItemFor(POS_SETTINGS)));
        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);


        adapter.setSelected(POS_NEARBY);

        //test picture//
        final ImageView imageView = (ImageView) findViewById(R.id.profile_image);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            Glide.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                //String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                Log.i(TAG, "provider info : id :" + providerId/*+"uid : "+uid+"name"+name+"email"+email+"photoUrl"+photoUrl*/);
         /*       SharedPreferences.Editor
                        editor =
                        getSharedPreferences(MY_PROFILE_INFO_PREFS, MODE_PRIVATE)
                                .edit();
                editor.putString("name", name);
                editor.putString("uid", uid);
                Log.i(TAG, "uid Main:"+uid);
                Log.i(TAG, "name Main:"+name);

                editor.apply();*/
            }
        } else {
            Log.i(TAG, "no picture found here");
            // No user is signed in
        }


        ///////////////Test///////////////////


        if (firebaseUser != null) {
            for (UserInfo userInfo : firebaseUser.getProviderData()) {
                if (userInfo.getProviderId().equals("facebook.com")) {
                    Toast.makeText(MainActivity.this, "User is signed in with Facebook", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "User is signed in with Facebook");
                }

                if (userInfo.getProviderId().equals("google.com")) {
                    Toast.makeText(MainActivity.this, "You are signed in Google!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "User is signed in with Google!");

                }
            }
        }
        //////////
        //        Disk Persistence
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        //Keeping Data Fresh
       // mFirebaseDatabase.keepSynced(true);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, simpleProfileFragment)
                        .commit();*/
                Intent i = new Intent(MainActivity.this, SimpleProfileActivity.class);
                i.putExtra("userId", uid);
                startActivity(i);
            }
        });

        //****** Google Api ************//
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        //create destination

        mDestinastionDatabase = mFirebaseInstance.getReference("destination");
        Destination destination = new Destination(uid,"QgIGke8y7sOduD8yhHEBkZ5ENqF3","millennium","cafe","monastir");
        String destId = mDestinastionDatabase.push().getKey();
        mDestinastionDatabase.child(destId).setValue(destination);

        mInvitationDatabase = mFirebaseInstance.getReference("users");

       mInvitationDatabase.child(uid).child("invitation").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot dataSnap:dataSnapshot.getChildren()) {
                   Log.i(TAG,"FireListener :"+uid);
                   String inviKey= dataSnap.getKey();
                   Log.i(TAG, "FireListener :"+"invikey : "+inviKey);
               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Log.i(TAG,"FireListener :",databaseError.toException());
           }
       });











    }


    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            Log.i(TAG, "google api connected");
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void locationDatabaseUpdate() {
        LocationModel dblocation = new LocationModel(mLatitude, mLongitude);

        mFirebaseDatabase.child(uid).child("location")
                .setValue(dblocation);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {



        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

                if (mLastLocation != null) {
                    mLatitude =  mLastLocation.getLatitude();
                    mLongitude = mLastLocation.getLongitude();
                    Log.i(TAG, "Location"+mLatitude+"long"+mLongitude);

                }
                else {
                    Log.i(TAG, "Location failed");

                }
            }
//        }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onItemSelected(int position) {
        /*if (position == POS_LOGOUT) {
            finish();
        }*/

       /* if(position == 1)
        {
         Fragment selectedScreen = new NearbyFragment();

        }*/
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
      /*  NearbyFragment selectedScreen = new NearbyFragment();


        showFragment(selectedScreen);*/


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        android.support.v4.app.Fragment fragment = new android.support.v4.app.Fragment();
        switch (position)
        {
            case 0:
                fragment = new NearbyFragment();
                break;
            case 1:
                fragment = new ChatFragment();
                break;
            case 2:
            fragment = new NearbyFragment();
            break;
            case 3:
                fragment = new NearbyFragment();

                break;

        }
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }

   /* private void showFragment(NearbyFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        *//*getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();*//*
    }
*/
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    public void listenerForInvitation(){

    }

    public void createNotification(String key) {
        NotificationManager notif=(NotificationManager)getSystemService(this.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder
                (getApplicationContext())
                .setContentTitle("tittle")
                .setContentText("body").
                setContentTitle("key: "+key)
                .setSmallIcon(R.drawable.cast_ic_notification_0)
                .build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

    }

    @Override
    public void showingFilterDialog(boolean state) {
            Log.i(TAG,"dialog run");
        DialogFragment dialogFragment = SearchFilterDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(),"dialog");

    }
}