package com.ohrats.bbb.ohrats;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Justin on 10/4/2017.
 */

public class ViewRatReportListActivity extends Activity{
    // Array of strings...
    ListView simpleList;
    String countryList[] = {"India", "China", "australia", "Portugle", "America", "NewZealand"};

    @Override   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rat_report_list);
        simpleList = (ListView)findViewById(R.id.rat_reports);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_rat_listview, R.id.textView, countryList);
        simpleList.setAdapter(arrayAdapter);
    }
}
