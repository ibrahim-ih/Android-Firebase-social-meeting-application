package com.ngenious.ibrahim.liny.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.model.User;
import com.ngenious.ibrahim.liny.profile.fragment.DescriptionTabsFragment;
import com.ngenious.ibrahim.liny.profile.fragment.GalleryTabsFragment;
import com.ngenious.ibrahim.liny.profile.fragment.gallery.adapter.GalleryAdapter;

public class SimpleProfileActivity extends AppCompatActivity implements DescriptionTabsFragment.DescriptionFragmentListener,
        GalleryTabsFragment.GalleryTabsFragmentListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_gallery,
            R.drawable.ic_info
    };
   // private ProgressDialog pDialog;
   private String uid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference userDatabaseRef = database.getReference("users");
    private String visibility;
    private String displayName;
    private Uri picture;
    private String email;
    private String firstName;
    private String lastName;
    private String profession;
    private String dateOfBirth;
    private int age;
    private String gender;
    private String country;
    private String city;
    private String about;
    private double mLatitude;
    private double mLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
      // pDialog = new ProgressDialog(getActivity().getApplicationContext());
        Intent intent = getIntent();
        uid = intent
                .getStringExtra("userId");
        if(uid == null)
        {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        //final User user = new User(visibility,displayName,picture,firstName,lastName,profession, age,gender,country,city,about,mLatitude,mLongitude);
     /*   userDatabaseRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                visibility = user.getVisibility();
                displayName = user.getDisplayName();
               // picture = user.getPicture();
                firstName = user.getFirstName();
                lastName = user.getLastName();
                profession = user.getProfession();
                age = user.getAge();
                gender = user.getGender();
                country = user.getCountry();
                city = user.getCity();
                about = user.getAbout();
                mLatitude = user.getmLatitude();
                mLongitude= user.getmLongitude();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }
    private void setupTabIcons(){
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Details");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_info, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Gallery");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_gallery, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*adapter.addFrag(new DescriptionTabsFragment(),"DETAILS");
        adapter.addFrag(new GalleryTabsFragment(),"GALLERY");*/
        User user = new User(visibility,displayName,picture,firstName,lastName,profession, age,gender,country,city,about,mLatitude,mLongitude);
        adapter.addFrag(DescriptionTabsFragment.newInstance(user),"DETAILS");
        adapter.addFrag(new GalleryTabsFragment(),"GALLERY");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void showImage(RecyclerView recyclerView, final ArrayList images){
        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onDescriptionFragmentListenerFinish(User user) {
        //   Log.i(TAG, "onFragmentFinish: "+ user.getEmail() +","+user.getDisplayName()+","+user.getPassword());

    }

    @Override
    public void onGalleryTabsFragmentListenerFinish(User user) {
        //  Log.i(TAG, "onFragmentFinish: "+ user.getEmail() +","+user.getDisplayName()+","+user.getPassword());

    }

}
