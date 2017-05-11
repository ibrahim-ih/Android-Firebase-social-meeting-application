package com.ngenious.ibrahim.liny.profile.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.model.Users;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionTabsFragment extends Fragment {
private DescriptionFragmentListener mListener;
    private String displayName;
    private static final String TAG = "DescriptionTAG";

    @BindView(R.id.displayNameTextView)TextView _displayName;
    @BindView(R.id.addressTextView)TextView _address;
    @BindView(R.id.visibilityTextView)TextView _visibility;
    @BindView(R.id.avatar)CircleImageView _avatar;
    @BindView(R.id.nameTextView)TextView _firstName;
    @BindView(R.id.lastNameTextView)TextView _lastName;
    @BindView(R.id.ageTextView)TextView _age;
    @BindView(R.id.genderTextView)TextView _gender;
    @BindView(R.id.professionTextView)TextView _profession;
    @BindView(R.id.aboutTextView)TextView _about;
    @BindView(R.id.cityTextView)TextView _city;
    @BindView(R.id.countryTextView)TextView _country;


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String uid;
    public DescriptionTabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description_tabs, container, false);
        ButterKnife.bind(this,view);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        User user = getArguments().getParcelable("User_Key");
        displayName = user.getDisplayName();

        Log.i(TAG, "name"+displayName);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getProfileInfo(uid);

        return view;
    }

    private void done(){
        if (mListener==null){
            throw new AssertionError();
        }
        //
        //blabla
        //
      //  User user = new User(name, email, password);

        //mListener.onDescriptionFragmentListenerFinish(user);

    }
    public interface DescriptionFragmentListener{
        void onDescriptionFragmentListenerFinish(User user);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //assert is for debug mode vs exception is for debug & release mode
        if (!(context instanceof DescriptionFragmentListener)) throw new AssertionError();
        mListener=(DescriptionFragmentListener)context;
    }
    public static DescriptionTabsFragment newInstance(User user){
        Bundle args = new Bundle();
        args.putParcelable("User_Key", user);

        DescriptionTabsFragment fragment = new DescriptionTabsFragment();
        fragment.setArguments(args);
        return fragment;

    }
    private void getProfileInfo(String uid){
        mFirebaseDatabase.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
        @Nullable
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users users  = dataSnapshot.getValue(Users.class);
                _displayName.setText(users.getDisplayName());
                if (users.getVisibility() != null){
                _visibility.setText(users.getVisibility());}
                if (users.getCity() != null){
                _city.setText(users.getCity());}
                _country.setText(users.getCountry());
                if (users.getFirstName() != null){
                _firstName.setText(users.getFirstName());}
                if (users.getLastName()!= null){
                _lastName.setText(users.getLastName());}
                _age.setText(String.valueOf(users.getAge()));
                _gender.setText(users.getGender());
                if (users.getProfession()!=null){
                _profession.setText(users.getProfession());}
                if (users.getAbout()!=null){
                _about.setText(users.getAbout());}
                Glide.with(getActivity())
                        .load(Uri.parse(users.getPicture()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(_avatar);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
