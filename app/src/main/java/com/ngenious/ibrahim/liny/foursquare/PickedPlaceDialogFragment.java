package com.ngenious.ibrahim.liny.foursquare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.Destination;
import com.ngenious.ibrahim.liny.model.History;
import com.ngenious.ibrahim.liny.model.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ibrahim on 17/05/17.
 */
public class PickedPlaceDialogFragment extends DialogFragment {
    private static String TAG = "PickedPlace";
    @BindView(R.id.timePicker)TimePicker _timePicker;
    @BindView(R.id.placeNameTextView)TextView _placeName;
    @BindView(R.id.placeAddressTextView)TextView _placeAddress;
    @BindView(R.id.timePickedTextView)TextView _timePicked;
    CharSequence placeName;
    CharSequence address;
    String id;
    double latitude;
    double longitude;
    long unixTime;
    int day, month, year, hour, minute, second, millisecond;
    String format;
    private Context context;
    TimePicker timePicker1;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDestinastionDatabase;
    private DatabaseReference mInvitationHistoryDatabase;
    private FirebaseUser firebaseUser;
    boolean availability;
    Date date;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialogfragment_pickedplace, null);
        builder
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "positiveButton clicked");
                        String sPlaceName = placeName.toString();
                        String sAddress = address.toString();
                        getTimestamp();
                        //***************************************
                        //Start the history activity
                        //With list of invitation card (active and not active invitation)
                        // so we will create firstable the table Invitation (pushKey, PlaceName,PlaceId(to be verified), Address,unixTimestamp)
                        //secondable we will create the table history in the user table, and add the key of the invitation card;
                        // thirdable and finally for this suggest we will create implement all this work in the history activity
                        //***************************************


                        //we need a method to verify the entries query
                        availability = true;
                        saveinDb();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        Log.i(TAG, "negativeButton clicked");
                    }
                })
                .setView(v);

        ButterKnife.bind(this, v);

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        placeName = this.getArguments().getCharSequence("name");
        address = this.getArguments().getCharSequence("address");
        latitude =this.getArguments().getDouble("latitude");
        longitude = this.getArguments().getDouble("longitude");
        id = this.getArguments().getString("id");
        setInput();
        getTimePicker();

        setTimePicker();
        return v;
    }

    private void setInput(){
        _placeName.setText(placeName);
        _placeAddress.setText(address);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentDateTime();
    }

    //#1
    private void getCurrentDateTime(){

        Calendar calendar = Calendar.getInstance();
         day = calendar.get(Calendar.DAY_OF_MONTH);
         month = calendar.get(Calendar.MONTH);
         year = calendar.get(Calendar.YEAR);
         hour = calendar.get(Calendar.HOUR);
         minute = calendar.get(Calendar.MINUTE);
         second = calendar.get(Calendar.SECOND);
         millisecond = calendar.get(Calendar.MILLISECOND);
        Log.i(TAG, "Month : "+ month+ " day : "+day);

    }

    //#2
    private void setTimePicker(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _timePicker.setMinute(minute);
            _timePicker.setHour(hour);
        }else {
            _timePicker.setCurrentHour(hour);
            _timePicker.setCurrentMinute(minute);
        }

    }

    private void getTimePicker(){
        _timePicker
                .setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        showTime(hourOfDay, minute);
                    }
                });
    }

    private void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        _timePicked.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }


    @NonNull
    private String concateTime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           hour = _timePicker.getHour();
           minute = _timePicker.getMinute();
        }else {
            hour = _timePicker.getCurrentHour();
            minute = _timePicker.getCurrentMinute();

        }


        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append("-");
        sb.append(month+1);//+1 car Calendar.MONTH se commence par 0 pour janvier
        sb.append("-");
        sb.append(day);
        sb.append(" ");
        sb.append(hour);
        sb.append(":");
        sb.append(minute);
        sb.append(":");
        sb.append(second);

        return sb.toString();

    }

    private void getTimestamp(){
        String timeStampStr = concateTime();
        Log.i(TAG, "timeStampStr : "+timeStampStr);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(timeStampStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        unixTime = (long) date.getTime() / 1000;
        Log.i(TAG, "unixTime :"+ unixTime);
    }
@NonNull
private String getUserId(){
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    if (user != null){
        return user.getUid();
    }else
        return "not found";
}
    private void saveinDb(){


        String uid = getUserId();

        Log.i(TAG, " uid :"+uid);
         if (uid == "not found")
         {
             Log.i(TAG, "Uid not found ");
         }
        mFirebaseInstance = FirebaseDatabase.getInstance();


        mDestinastionDatabase = mFirebaseInstance.getReference("destination");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Users users = new Users(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());

        Destination destination = new Destination(availability, uid, placeName.toString(), address.toString(), id, latitude,  longitude, unixTime, users);
        String destId = mDestinastionDatabase.push().getKey();
        Log.i(TAG, "Query  --> Destination  : destId"+destId + "availability"+availability+"uid"+uid+"placeName"+placeName.toString()+""+address.toString()
                +"id"+id+"latitude"+latitude+"longitude"+longitude+"unixTime"+unixTime);

        mDestinastionDatabase.child(destId).setValue(destination);

        History historyInvit = new History(destId);


        mInvitationHistoryDatabase = mFirebaseInstance.getReference("users");
        mInvitationHistoryDatabase.child(uid)
                .child("history")
                .child("invitAvailable")
                .setValue(historyInvit);
        mInvitationHistoryDatabase
                .child(uid)
                .child("history")
                .child("invitNotAvailable")
                .push()
                .setValue(historyInvit);
        Log.i(TAG, "Query  --> History "+historyInvit );



//        Invitation invitation = new Invitation(String id, String from, placeName, address, id, latitude,  longitude, unixTime);

    }
}
