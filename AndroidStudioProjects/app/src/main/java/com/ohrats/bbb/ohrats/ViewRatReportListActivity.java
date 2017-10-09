package com.ohrats.bbb.ohrats;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
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

    Firebase mRef;
    FirebaseListAdapter<String> mAdapter;

    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rat_report_list);
        simpleList = (ListView)findViewById(R.id.rat_reports);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_rat_listview, R.id.textView, countryList);
        //simpleList.setAdapter(arrayAdapter);
        mRef = new Firebase("https://androidstudioprojects-b4132.firebaseio.com/");
//        mListView = (ListView) findViewById(R.id.ListView);
        mAdapter = new FirebaseListAdapter<RatSighting>(this, RatSighting.class, R.layout.activity_rat_listview, mRef) {
            @Override
            protected void populateView(View view, RatSighting ratSighting, int position) {
                //Set the value for the views
                ((TextView)view.findViewById(R.id.xxx)).setText(ratSighting.getName());
                //text.set(s);
                //...
            }
        };
        simpleList.setAdapter(mAdapter);
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
