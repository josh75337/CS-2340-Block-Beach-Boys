package com.ohrats.bbb.ohrats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//import com.firebase.client.Firebase;
//import com.firebase.ui.database.FirebaseListAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;


/**
 * Created by Justin on 10/4/2017.
 */

public class ViewRatReportListActivity extends Activity{
    // Array of strings...
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    Button mAddSightingButton;

//
//    Firebase mRef;
//    FirebaseListAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rat_report_list);
        simpleList = (ListView)findViewById(R.id.rat_reports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_rat_listview, R.id.textView, countryList);
        simpleList.setAdapter(arrayAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //find someway to get position. All examples had static arrays with no change in indices
//                Intent intent = new Intent(view.getContext(), RatReport.class);
//                startActivityForResult(intent, position);
                if (position == 0) {
                    Intent myIntent = new Intent(view.getContext(), ViewSightingActivity.class);
                    startActivityForResult(myIntent, 0);
                }
//
//                if (position == 1) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 2) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity1.class);
//                    startActivityForResult(myIntent, 0);
//                }
//
//                if (position == 3) {
//                    Intent myIntent = new Intent(view.getContext(), ListItemActivity2.class);
//                    startActivityForResult(myIntent, 0);
//                }
            }
        });


        mAddSightingButton = (Button) findViewById(R.id.raddsighting);
        mAddSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAddRatSightingPage();
            }
        });



//        mRef = new Firebase("https://androidstudioprojects-b4132.firebaseio.com/");
//        mListView = (ListView) findViewById(R.id.ListView);
//        mAdapter = new FirebaseListAdapter<RatSighting>(this, RatSighting.class, R.layout.activity_rat_listview, mRef) {
//            @Override
//            protected void populateView(View view, RatSighting ratSighting, int position) {
//                //Set the value for the views
//                ((TextView)view.findViewById(R.id.xxx)).setText(ratSighting.getName());
//                //text.set(s);
//                //...
//            }
//        };
//        simpleList.setAdapter(mAdapter);
    }

    private void viewAddRatSightingPage() {
        Intent inView = new Intent(ViewRatReportListActivity.this, AddSightingActivity.class);
        startActivity(inView);
    }

//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mRef = new Firebase("https://<myURL>..");
//
//        myAdapter = new FirebaseListAdapter<String>(this,String.class,android.R.layout.simple_list_item_1,mRef) {
//            @Override
//            protected void populateView(View view, String s, int i) {
//                TextView text = (TextView) view.findViewById(android.R.id.text1);
//                text.setText(s);
//            }
//        };
//        final ListView lv = (ListView) findViewById(R.id.listView);
//        lv.setAdapter(myAdapter);
//
//        Button addBtn = (Button) findViewById(R.id.add_button);
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRef.push().setValue("test123");
//            }
//        });
}
