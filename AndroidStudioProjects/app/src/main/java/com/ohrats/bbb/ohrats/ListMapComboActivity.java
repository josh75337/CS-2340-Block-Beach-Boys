package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Matt on 10/23/2017.
 */

public class ListMapComboActivity extends AppCompatActivity {


    //Keep a log for debugging
    private static final String TAG = "ListMapComboActivity";

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Created Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmapcombo);

        mViewPager  = (ViewPager) findViewById(R.id.scontainer);

        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Navigates to MainActivity when back button pressed
     */
    @Override
    public void onBackPressed(){
        // Navigate to the MainActivity
        Intent in = new Intent(ListMapComboActivity.this, MainActivity.class);
        startActivity(in);
    }

    /**
     * takes view pager and adds fragments that will be the different tabs
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ViewRatReportListFragment(), "List");
        adapter.addFragment(new ViewMapFragment(), "Map");
        viewPager.setAdapter(adapter);
    }
}

