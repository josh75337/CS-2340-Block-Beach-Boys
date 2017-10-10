package com.ohrats.bbb.ohrats;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

/**
 * a screen for adding rat sightings using singularly or through using a csv
 * Created by Matt on 10/3/2017.
 */

public class AddSightingActivity extends AppCompatActivity{


    //FireBase

    //Firebase Realtime DataBase
    private DatabaseReference mDatabase;

    //Keep a log for debugging
    private static final String TAG = "AddSightingActivity";

    private SectionsPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsighting);

        mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager  = (ViewPager) findViewById(R.id.scontainer);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SingleFragment(), "Single");
        adapter.addFragment(new CSVFragment(), "CSV");
        viewPager.setAdapter(adapter);
    }
}
