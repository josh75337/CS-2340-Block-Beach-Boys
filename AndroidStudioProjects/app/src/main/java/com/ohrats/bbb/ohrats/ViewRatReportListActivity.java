package com.ohrats.bbb.ohrats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.LinkedList;

//import com.firebase.client.Firebase;
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;


/**
 * Activity which displays rat sightings and allows for navigation to CSV loader
 *
 * Created by Justin on 10/4/2017.
 */

public class ViewRatReportListActivity extends Activity{
    private static final String TAG = "RatReportListActivity";
    // Array of strings...
    ListView simpleList;
    LinkedList<RatSighting> sightingList = new LinkedList<>();

    Button mAddSightingButton;

    private DatabaseReference mDatabase;
    private final int SIGHTINGS_PER_PAGE = 50;

    int count;

//
//    Firebase mRef;
//    FirebaseListAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rat_report_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Populates sightingList asynchronously,
        //     i.e. We can't have our final list view until it finishes populating
        updateSightingList();

        // Button for going to the manual add and csv selector screen
        mAddSightingButton = (Button) findViewById(R.id.raddsighting);
        mAddSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAddSightingActivity();
            }
        });
    }

    /**
     * Switches to the AddSightingActivity
     */
    private void viewAddSightingActivity() {
        Intent inView = new Intent(ViewRatReportListActivity.this, AddSightingActivity.class);
        startActivity(inView);
    }

    /**
     * updates the ListView with the current contents of sightingList (i.e. RatSighting(s))
     */
    private void updateListView() {
        Log.v(TAG, "updateListView called while size of sightingList is" + sightingList.size());
        simpleList = (ListView) findViewById(R.id.rat_reports);
        ArrayAdapter<RatSighting> arrayAdapter =
                new ArrayAdapter<>(this,
                        R.layout.activity_rat_listview,
                        R.id.textView,
                        sightingList);
        //---------------------------------------------
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(view.getContext(), ViewSightingActivity.class);
                myIntent.putExtra("RAT_SIGHTING", sightingList.get(position));
                startActivity(myIntent);
            }
        });

    }

    /**
     * fetches RatSighting(s) from database and adds them to sightingList
     *
     * also calls updateListView() every time a RatSighting is fetched in order to populate
     * the list view
     */
    private void updateSightingList() {
        Log.v(TAG, "updateSightingList called" );
        DatabaseReference sightingsRef = mDatabase.child("sightings");

        Query query = sightingsRef.orderByKey().limitToLast(SIGHTINGS_PER_PAGE);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded event" );
                Log.v(TAG, "UID of child sighting: "
                        + dataSnapshot.getValue(RatSighting.class).getKey());
                //-----------------------------------------------------------
                Log.v(TAG, "Date of child sighting: "
                        + dataSnapshot.getValue(RatSighting.class).getDate());
                //-----------------------------------------------------------

                sightingList.addFirst(dataSnapshot.getValue(RatSighting.class));
                while(sightingList.size() > SIGHTINGS_PER_PAGE) {
                    sightingList.removeLast();
                }

                updateListView();


                Log.v(TAG, "size of sightingList after adding: " + sightingList.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
