package com.ngenious.ibrahim.liny.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.adapter.FriendsAdapter;
import com.ngenious.ibrahim.liny.helper.ClickListener;
import com.ngenious.ibrahim.liny.model.Users;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";
    private static final String ARG_FRIENDS = "friends";
    private static final String ARG_FRIENDS_UID = "friendsuid";

    private List<Users> friendsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    private String mParam1;
    private String mParam2;
    private List<String> friendUid = new ArrayList<>();

    private OnFriendsFragmentInteractionListener mListener;
    private Context mContext;
    public FriendsFragment() {
    }


    public static FriendsFragment newInstance(List<Users> friendsList, List<String> friendUid) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_FRIENDS, (ArrayList<? extends Parcelable>) friendsList);
        args.putStringArrayList(ARG_FRIENDS_UID, (ArrayList<String>)friendUid);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = mContext;
        if (getArguments() != null) {
            friendsList = getArguments().getParcelableArrayList(ARG_FRIENDS);
            friendUid = getArguments().getStringArrayList(ARG_FRIENDS_UID);
            Log.d(TAG, "friendsList isEmpty() : "+friendsList.isEmpty());
            Log.d(TAG, "friendUid isEmpty() : "+friendUid.isEmpty());
            Log.d(TAG, "friendUid size() : "+friendUid.size());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        FragmentActivity fragmentActivity = getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.friends_recycler_view);

        friendsAdapter = new FriendsAdapter(fragmentActivity, friendsList, friendUid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(fragmentActivity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendsAdapter);
       /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                //Values are passing to activity & to fragment as well
                Log.i(TAG,"Single Click on position : "+position+" get name : "+friendsList.get(position));
                *//*Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
                        Toast.LENGTH_SHORT).show();*//*
                *//*intent.putExtra("roomId",chatRoomList.get(position).getId());
                intent.putExtra("uName",chatRoomList.get(position).getName());
                ChatRoom clearCountMsg
                        = new ChatRoom(0);
                mFirebaseDatabase.child(chatRoomChilKeys.get(position).toString()).child("unreadCount").setValue(0);
                startActivity(intent);*//*
                Log.i(TAG, "CLICKED !!!");

            }

            @Override
            public void onLongClick(View view, int position) {
               *//* Toast.makeText(MainActivity.this, "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();*//*
                Log.d(TAG,"Long press on position : "+position);

            }
        }));*/

        return view;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFriendsFragmentInteractionListener) {
            mListener = (OnFriendsFragmentInteractionListener) context;
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

    public interface OnFriendsFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


/*    ////inner class////
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
    }*/
}
