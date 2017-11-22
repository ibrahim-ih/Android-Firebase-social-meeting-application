package com.ngenious.ibrahim.liny.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.adapter.InvitationAdapter;
import com.ngenious.ibrahim.liny.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class InvitationFragment extends Fragment {

    private static final String TAG = "InvitationFragment";
    //private static final String ARG_INVITATION = "invitation_id";
    private static final String ARG_DESTINATION = "destination";
    private static final String ARG_FRIENDS_UID = "friendsuid";
    private static final String ARG_Destination_ID = "destinationid";

    private List<String> peopleIdList = new ArrayList<>();
    private List<String> destitaionIdList = new ArrayList<>();


    private List<Destination> destinationList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;
    private InvitationAdapter invitationAdapter;
    private RecyclerView recyclerView;
    private Context mContext;
    private  String accepted, ignored, peopleId, displayNameDest;
    public InvitationFragment() {

    }

    public static InvitationFragment newInstance(List<Destination> invitationList, List<String> peopleIdList, List<String> destitaionIdList) {
        InvitationFragment fragment = new InvitationFragment();
        Bundle args = new Bundle();

       args.putParcelableArrayList(ARG_DESTINATION, (ArrayList<? extends Parcelable>) invitationList);
        args.putStringArrayList(ARG_FRIENDS_UID, (ArrayList<String>)peopleIdList);
        args.putStringArrayList(ARG_Destination_ID, (ArrayList<String>)destitaionIdList);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = mContext;

        if (getArguments() != null) {
            destinationList = getArguments().getParcelableArrayList(ARG_DESTINATION);
            Log.d(TAG, "invitationList isEmpty() : "+destinationList.isEmpty());
            peopleIdList = getArguments().getStringArrayList(ARG_FRIENDS_UID);
            destitaionIdList = getArguments().getStringArrayList(ARG_Destination_ID);


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_invitation, container, false);

        FragmentActivity fragmentActivity = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.invitaion_recycler_view);

        invitationAdapter = new InvitationAdapter(fragmentActivity, destinationList, peopleIdList,destitaionIdList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(invitationAdapter);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-order"));

        // https://stackoverflow.com/questions/35008860/how-to-pass-values-from-recycleadapter-to-mainactivity-or-other-activities
        return view;
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            accepted = intent.getStringExtra("Accepted");
            ignored = intent.getStringExtra("Ignored");
            peopleId = intent.getStringExtra("peopleId");
            displayNameDest = intent.getStringExtra("displayName");
            Log.d(TAG, "Decision by broadcastReceiver accpted : "+accepted);
            Log.d(TAG, "Decision by broadcastReceiver ignored : "+ignored);

                mListener.onAddFriend(accepted, peopleId,displayNameDest);

                mListener.onDeleteFriend(ignored);
//            Toast.makeText(MainActivity.this,ItemName +" "+qty ,Toast.LENGTH_SHORT).show();
        }
    };


    public void onButtonPressed(Uri uri) {
        if (mListener==null){
            throw new AssertionError();
        }
        else {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

       void onFragmentInteraction(Uri uri);
        void onAddFriend(String accepted, String peopleId ,String displayNameDest);
        void onDeleteFriend(String ignored);
    }
}
