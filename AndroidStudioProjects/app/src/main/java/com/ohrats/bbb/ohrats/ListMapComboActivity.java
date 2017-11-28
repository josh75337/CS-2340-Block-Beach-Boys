package com.ohrats.bbb.ohrats;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 * Created by Matt on 10/23/2017.
 */

@SuppressWarnings({"DefaultFileTemplate", "CyclicClassDependency"})
public class ListMapComboActivity extends AppCompatActivity {


    //Keep a log for debugging
    private static final String TAG = "ListMapComboActivity";

    private DatabaseReference mDatabase;

    private ViewMapFragment mapFragment;
    private ViewRatReportListFragment ratReportListFragment;

    private LinkedList<RatSighting> sightingList = new LinkedList<>();

    private Button mSearchButton;

    private TextView mStartDate;
    private TextView mEndDate;

    private long searchStart;
    private long searchEnd;

    private long startDateMillis;
    private long endDateMillis;

    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;

    @SuppressWarnings("ChainedMethodCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ViewPager mViewPager;
        Log.d(TAG, "Created Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listmapcombo);
        mViewPager  = (ViewPager) findViewById(R.id.scontainer);

        //suppressed because Firebase requires weird rules
        mDatabase = FirebaseDatabase.getInstance().getReference();

        startDateMillis = 0;
        //suppressed because has to be done per instance
        endDateMillis = Calendar.getInstance().getTimeInMillis();

        searchStart = startDateMillis;
        searchEnd = endDateMillis;

        mapFragment = new ViewMapFragment();
        ratReportListFragment = new ViewRatReportListFragment();
        updateSightingList();


        mStartDate = (TextView) findViewById(R.id.mstartdate);
        mStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            ListMapComboActivity.this,
                            mStartDateSetListener,
                            year, month, day);
//                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //suppressed because sets bounds for date in text box
                    dialog.getDatePicker().setMaxDate(searchEnd);
                    dialog.show();
                }
            }
        });

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("DefaultLocale")
            @SuppressWarnings({"ConstantConditions", "AssignmentToMethodParameter"})
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                searchStart = new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();
                //noinspection AssignmentToMethodParameter
                month = month + 1;
                //suppressed because string only used in U.S.
                mStartDate.setText(String.format("%02d/%02d/%4d", month, dayOfMonth, year));
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                mSearchButton.setEnabled(true);
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        };

        mEndDate = (TextView) findViewById(R.id.menddate);
        mEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            ListMapComboActivity.this,
                            mEndDateSetListener,
                            year, month, day);
//                  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getDatePicker().setMinDate(searchStart);
                    dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                    dialog.show();
                }
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("DefaultLocale")
            @SuppressWarnings({"ConstantConditions", "AssignmentToMethodParameter"})
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                searchEnd = new GregorianCalendar(year, month,
                        dayOfMonth + 1).getTimeInMillis() - 1;
                //noinspection AssignmentToMethodParameter
                month = month + 1;
                mEndDate.setText(String.format("%02d/%02d/%4d", month, dayOfMonth, year));
                if (getCurrentFocus() != null) {
                    getCurrentFocus().clearFocus();
                }
                mSearchButton.setEnabled(true);
                InputMethodManager imm =
                        (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        };

        mSearchButton = (Button) findViewById(R.id.rsearch);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

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
     * @param viewPager the passed in viewPager that reads in the list and map
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(ratReportListFragment, "List");
        adapter.addFragment(mapFragment, "Map");
        viewPager.setAdapter(adapter);
    }


//    /**
//     * Switches to the AddSightingActivity
//     */
//    private void viewAddSightingActivity() {
//        Intent inView = new Intent(this, AddSightingActivity.class);
//        startActivity(inView);
//    }


    /**
     * fetches RatSighting(s) from database and adds them to sightingList
     */
    @SuppressWarnings("ChainedMethodCall")
    private void updateSightingList() {
        final int SIGHTINGS_PER_PAGE = 50;
        Log.v(TAG, "updateSightingList called");
        DatabaseReference sightingsRef = mDatabase.child("sightings");
        sightingList = new LinkedList<>();

        //noinspection ChainedMethodCall,ChainedMethodCall,ChainedMethodCall
        // suppressed because used to set up sightings by date
        Query query = sightingsRef.orderByChild("date")
                .startAt(DateStandardsBuddy.getISO8601MINStringForDate(new Date(startDateMillis)))
                .endAt(DateStandardsBuddy.getISO8601MAXStringForDate(new Date(endDateMillis)))
                .limitToLast(SIGHTINGS_PER_PAGE);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RatSighting sightingToAdd = data.getValue(RatSighting.class);
                    sightingList.addFirst(sightingToAdd);
                }
                mapFragment.setSightingList(sightingList);
                mapFragment.update();
                ratReportListFragment.setSightingList(sightingList);
                ratReportListFragment.update();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    /**
     * changes update values to the new search values and updates list with new search
     */
    private void search() {
        startDateMillis = searchStart;
        endDateMillis = searchEnd;
        updateSightingList();
    }

//    private void filterSightingsListByDate() {
//        DatabaseReference sightingsRef = mDatabase.child("sightings");
//        sightingList = new LinkedList<>();
//        String startDate = DateStandardsBuddy.garbageAmericanStringToISO8601ESTDate();
//        String endDate = "";
//        Query query = sightingsRef.orderByChild("date").limitToLast(SIGHTINGS_PER_PAGE);
//    }
}

