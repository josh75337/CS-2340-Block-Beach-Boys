package com.ohrats.bbb.ohrats;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * a screen for adding rat sightings using singularly or through using a csv
 * 
 * Created by Matt on 10/3/2017.
 */
public class AddSightingActivity extends AppCompatActivity{



    //Keep a log for debugging
    @SuppressWarnings("unused")
    //this field is used in debugging so we keep it around
    private static final String TAG = "AddSightingActivity";

// --Commented out by Inspection START (11/13/2017 2:15 PM):
//    //Both of these could be local variables but it is better
//    //to have them as fields because they could be needed
//    //for additional methods to the code. Also,
//    //this is the convention for firebase
//    @SuppressWarnings("FieldCanBeLocal")
//    private SectionsPageAdapter mSectionPageAdapter;
// --Commented out by Inspection STOP (11/13/2017 2:15 PM)
    @SuppressWarnings("FieldCanBeLocal")
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsighting);

        //mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager  = (ViewPager) findViewById(R.id.scontainer);

        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }

    /**
     * takes view pager and adds fragments that will be the different tabs
     * @param viewPager - the viewpager object that the fragments will be added to
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new SingleFragment(), "Single");
        adapter.addFragment(new CSVFragment(), "CSV");
        viewPager.setAdapter(adapter);
    }
}
