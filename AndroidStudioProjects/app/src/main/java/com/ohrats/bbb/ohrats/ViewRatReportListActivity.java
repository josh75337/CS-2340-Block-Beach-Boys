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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

//import com.firebase.client.Firebase;
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;


/**
 * Created by Justin on 10/4/2017.
 */

public class ViewRatReportListActivity extends Activity{
    private static final String TAG = "RatReportListActivity";
    // Array of strings...
    ListView simpleList;
    LinkedList<RatSighting> sightingList = new LinkedList<>();

    Button mAddSightingButton;

    private DatabaseReference mDatabase;

    int count;

//
//    Firebase mRef;
//    FirebaseListAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rat_report_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        count = 10;

//        RatSighting r0 = new RatSighting("0", null, "Georgia Tech Dorm", "30332", "NO", "Definitely not Atlanta", "Maybe Georgia", 8.1, 10.1);
//        RatSighting r1 = new RatSighting("1");
//        RatSighting r2 = new RatSighting("2");
//        RatSighting r3 = new RatSighting("3");
//
//        sightingList.add(r0);
//        sightingList.add(r1);
//        sightingList.add(r2);
//        sightingList.add(r3);
        updateSightingList();
        Log.v(TAG, "sightingList.size is: " + sightingList.size());
        for(int i = 0; i < sightingList.size(); i++){
            Log.d(TAG, "Element no " + i + " of sightingList is: " + sightingList.get(i).toString());
        }

        mAddSightingButton = (Button) findViewById(R.id.raddsighting);
        mAddSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAddRatSightingPage();
            }
        });
    }

    private void viewAddRatSightingPage() {
        Intent inView = new Intent(ViewRatReportListActivity.this, AddSightingActivity.class);
        startActivity(inView);
    }

    private void updateListView() {
        Log.v(TAG, "updateListView called while size of sightingList is" + sightingList.size());
        simpleList = (ListView) findViewById(R.id.rat_reports);
        ArrayAdapter<RatSighting> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_rat_listview, R.id.textView, sightingList);
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

    private void updateSightingList() {
        Log.v(TAG, "updateSightingList called" );
        DatabaseReference sightingsRef = mDatabase.child("sightings");

        Query query = sightingsRef.orderByKey().limitToLast(10);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded event" );
                Log.v(TAG, "UID of child sighting: " + dataSnapshot.getValue(RatSighting.class).getKey());
                Log.v(TAG, "Date of child sighting: " + dataSnapshot.getValue(RatSighting.class).getDate());
                sightingList.addFirst(dataSnapshot.getValue(RatSighting.class));
                Log.v(TAG, "size of sightingList after adding: " + sightingList.size());
                updateListView();
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
        updateListView();
        count += 10;
    }
}
