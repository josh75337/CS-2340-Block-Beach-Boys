package com.ohrats.bbb.ohrats;

//import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
//import android.widget.TextView;

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

@SuppressWarnings("FieldCanBeLocal") // many of these fields should not be local for clarity
public class ViewRatReportListFragment extends Fragment {
    private static final String TAG = "RatReportListFragment";
    // Array of strings...
    private ListView simpleList;
    private LinkedList<RatSighting> sightingList = new LinkedList<>();

//    Button mUpdateButton;

//    Button mNextButton;
//    Button mPrevButton;

//    TextView mPageNumView;

    private DatabaseReference mDatabase;
    private final int SIGHTINGS_PER_PAGE = 50;

    // --Commented out by Inspection (11/11/2017 5:26 PM):private String lastKey;

    // --Commented out by Inspection (11/11/2017 5:26 PM):private int pageNum;
    // --Commented out by Inspection (11/11/2017 5:26 PM):private int count;

//
//    Firebase mRef;
//    FirebaseListAdapter<String> mAdapter;



    @Nullable
    @Override
    @SuppressWarnings("ChainedMethodCall") // chained methods are integral to firebase queries
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        //-------------------------------------------------
        View view = inflater.inflate(R.layout.view_rat_report_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        pageNum = 1;
//        count = 1;
//        lastKey = "";

        // Populates sightingList asynchronously,
        //     i.e. We can't have our final list view until it finishes populating

        // Button for going to the manual add and csv selector screen

//        mNextButton = (Button) view.findViewById(R.id.rNext);
//        mNextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextPage();
//            }
//        });
//
//        mPrevButton = (Button) view.findViewById(R.id.rPrevious);
//        mPrevButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                previousPage();
//            }
//        });
//        mPrevButton.setClickable(false);
//
//        mPageNumView = (TextView) view.findViewById(R.id.rPageNumber);

        return view;
    }

    /**
     * public method to be called by combo activity that updates list view
     */
    public void update() {
        updateListView();
    }

    /**
     * updates the ListView with the current contents of sightingList (i.e. RatSighting(s))
     */
    private void updateListView() {
        Log.v(TAG, "updateListView called while size of sightingList is" + sightingList.size());
        // this is a best practice statement for dealing with Android
        //noinspection ChainedMethodCall
        simpleList = (ListView) getActivity().findViewById(R.id.rat_reports);
        ListAdapter arrayAdapter =
                new ArrayAdapter<>(getActivity(),
                        R.layout.activity_rat_listview,
                        R.id.textView,
                        sightingList);
        //---------------------------------------------
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(getActivity(), ViewSightingActivity.class);
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
    @SuppressWarnings("ChainedMethodCall") // chained calls are integral to firebase queries
    private void updateSightingList() {
        Log.v(TAG, "updateSightingList called" );
        DatabaseReference sightingsRef = mDatabase.child("sightings");

        Query query = sightingsRef.orderByChild("date").limitToLast(SIGHTINGS_PER_PAGE);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded event" );
                if ((dataSnapshot != null) && (dataSnapshot.getValue(RatSighting.class) != null)) {
                    //noinspection ConstantConditions
                    Log.v(TAG, "UID of child sighting: "
                            + dataSnapshot.getValue(RatSighting.class).getKey());
                    //-----------------------------------------------------------
                    //noinspection ConstantConditions
                    Log.v(TAG, "Date of child sighting: "
                            + dataSnapshot.getValue(RatSighting.class).getDate());
                    //-----------------------------------------------------------
                }
                sightingList
                        .addFirst((dataSnapshot != null)
                                ?
                                dataSnapshot.getValue(RatSighting.class) : null);
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

//    public void nextPage() {
//        pageNum++;
//        if (pageNum > 1) {
//            mPrevButton.setClickable(true);
//        }
//        mPageNumView.setText(String.format("Page %d", pageNum));
//        updateSightingList();
//    }
//
//    public void previousPage() {
//        pageNum--;
//        if (pageNum <= 1) {
//            mPrevButton.setClickable(false);
//        }
//        mPageNumView.setText(String.format("Page %d", pageNum));
//        updateListView();
//    }

    /**
     * public method that sets the fragments sighting list
     * @param list input list to which the sighing list is changed to
     */
    public void setSightingList(LinkedList list) {
        // we know this should work given that sightingList is also a linked list
        //noinspection AssignmentToCollectionOrArrayFieldFromParameter,unchecked
        sightingList = list;
    }
}
