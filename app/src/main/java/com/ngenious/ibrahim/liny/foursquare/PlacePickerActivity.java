package com.ngenious.ibrahim.liny.foursquare;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ngenious.ibrahim.liny.R;
import com.ngenious.ibrahim.liny.foursquare.fragments.ChooseDestFragment;
import com.ngenious.ibrahim.liny.foursquare.fragments.HistoryDestFragment;

import java.util.ArrayList;
import java.util.List;

public class PlacePickerActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
/*    private int[] tabIcons = {
            R.drawable.ic_coffee,
            R.drawable.ic_coffee,
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    /*    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fragment choosedestFrag = new ChooseDestFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.placepickercontent, choosedestFrag)
                .commit();*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*Fragment choosedestFrag = new HistoryDestFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.placepickercontent, choosedestFrag)
                .commit();*/
        viewPager = (ViewPager) findViewById(R.id.viewpagerplacepicker);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabsplacepicker);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Choose a Distination");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_coffee, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("History");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_filter_list_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ChooseDestFragment(), "Choose a Distination");
        adapter.addFrag(new HistoryDestFragment(), "History");
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
}