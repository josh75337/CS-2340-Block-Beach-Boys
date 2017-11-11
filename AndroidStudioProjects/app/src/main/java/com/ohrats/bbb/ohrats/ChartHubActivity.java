package com.ohrats.bbb.ohrats;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Eli Bailey
 * Intended as a hub for charts
 * This is the last class that should handle rat sightings and dates. All the charts should be
 * passed simpler information
 */
public class ChartHubActivity extends AppCompatActivity {

    private static final String TAG = "ChartHubActivity";

    // Can be set to something more reasonable to limit how much data is pulled from the databse
    private final int SIGHTINGS_LIMIT = Integer.MAX_VALUE;

    //spinner to select the type of chart
    private Spinner typeSpinner;
    private Spinner timeSpinner;

    private DatabaseReference mDatabase;

    private Button mLaunchChartButton;

    //Arraylist for keeping ratsightings.
    private ArrayList<RatSighting> sightingList = new ArrayList<>();

    //date range
    private TextView mStartDate;
    private TextView mEndDate;
    private long chartEnd;
    private long chartStart;
    private long startDateMillis;
    private long endDateMillis;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mEndDateSetListener;

    private String type = "XY Plot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

        //get firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        timeSpinner = (Spinner) findViewById(R.id.time_spinner);

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chart_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.chart_time_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter1);

        //default times
        startDateMillis = 0;
        endDateMillis = Calendar.getInstance().getTimeInMillis();

        //initialize times for the chart
        chartStart = startDateMillis;
        chartEnd = endDateMillis;

        mStartDate = (TextView) findViewById(R.id.mstartdate);
        mStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(ChartHubActivity.this,
                            mStartDateSetListener, year, month, day);
                    dialog.getDatePicker().setMaxDate(chartEnd);
                    dialog.show();
                }
            }
        });

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                chartStart = new GregorianCalendar(year, month, dayOfMonth).getTimeInMillis();
                month = month + 1;
                mStartDate.setText(String.format("%02d/%02d/%4d", month, dayOfMonth, year));
                try {
                    getCurrentFocus().clearFocus();
                    mLaunchChartButton.setEnabled(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException npe) {
                    Log.d(TAG, "Caught null pointer exception");
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
                    DatePickerDialog dialog = new DatePickerDialog(ChartHubActivity.this,
                            mEndDateSetListener, year, month, day);
                    dialog.getDatePicker().setMinDate(chartStart);
                    dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                    dialog.show();
                }
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                chartEnd = new GregorianCalendar(year, month, dayOfMonth + 1).getTimeInMillis() - 1;
                month = month + 1;
                mEndDate.setText(String.format("%02d/%02d/%4d", month, dayOfMonth, year));
                try {
                    getCurrentFocus().clearFocus();
                    mLaunchChartButton.setEnabled(true);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (NullPointerException npe) {
                    Log.d(TAG, "Caught null pointer exception");
                }
            }
        };

        //launch chart button
        mLaunchChartButton = (Button) findViewById(R.id.launch_chart_button);
        mLaunchChartButton.setEnabled(false);
        mLaunchChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRangedSightings();
                Toast.makeText(ChartHubActivity.this, "Please Wait, Loading...",
                        Toast.LENGTH_LONG).show();
            }
        });
    }



    private void startCreateChart(ArrayList<RatSighting> inputs) {
        //determine what type of chart the user wants
        final String chartType = (String) typeSpinner.getSelectedItem();
        final String chartTime = (String) timeSpinner.getSelectedItem();
        Boolean isYears = chartTime.equals("Yearly");

        //create a title based of of the chart
        String title = isYears ? "Sighting by year" : "Sightings by Month and Year";
        switch (chartType) {
            case "Pie Chart": title = title + ": Pie Chart";
                break;
            default: title = title + ": Bar Chart";
                break;
        }

        Log.d(TAG, "Arraylist size: " + inputs.size());


        HashMap<String, Integer> sortedTimeFrame = null;
        if (isYears) {
            sortedTimeFrame = sortByYear(inputs);
        } else if (chartTime.equals("Monthly")) {
            sortedTimeFrame = sortByMonth(inputs);
        } else {
            Log.d(TAG, "Charttime was invalid, not meeting any expected values");
            //@TODO: maybe throw a pop-up and make them restart
        }


        Log.d(TAG, sortedTimeFrame.toString());
        try {
            createChart(title, chartType, sortedTimeFrame, isYears);
        } catch (NullPointerException npe) {
            Log.d(TAG, "sortedTimeFrame may have been null");
        }
    }

    /**
     * Creates an intent and populated a chart activity before starting that activity
     * @param title The desired title of the chart @TODO: not working yet
     * @param chartType The type of chart desired (XY Plot, histogram, etc)
     * @param inputData The data from firebase
     */
    private void createChart(String title, String chartType, HashMap<String, Integer> inputData, Boolean isYears) {
        Log.v(TAG, "Called createChart()");

        Intent in = null;

        //determine what plot the user wants
        switch (chartType) {
            case "Pie Chart":
                Log.v(TAG, "creating pie chart");
                in = new Intent(this, DefaultPieChart.class);
                break;
            default:
                Log.v(TAG, "creating bar chart");
                in = new Intent(this, DefaultBarChart.class);
                break;
        }

        if (isYears) {
            Log.v(TAG, "Sending yearly title");
            in.putExtra("SERIES_TITLE", "Yearly");
        } else {
            Log.v(TAG, "Sending monthly title");
            in.putExtra("SERIES_TITLE", "Monthly");
        }

        TreeMap<String, Integer> sortedPairs = new TreeMap<>(inputData);

        ArrayList<String> tempString = new ArrayList<>(sortedPairs.keySet());
        ArrayList<Integer> xValues = new ArrayList<>();
        for (String str : tempString) {
            xValues.add(Integer.parseInt(str));
        }
        //Collections.reverse(xValues);

        ArrayList<Integer> yValues = new ArrayList<>(sortedPairs.values());
        //Collections.reverse(yValues);

        Log.v(TAG, xValues.toString());
        Log.v(TAG, yValues.toString());

        //pass data with the intent
        in.putExtra("X_VALUES", xValues);
        in.putExtra("Y_VALUES", yValues);

        //navigate the the plot
        startActivity(in);
    }

    private void getRangedSightings(){

        DatabaseReference sightingsRef = mDatabase.child("sightings");

        final ArrayList<RatSighting> toReturn = new ArrayList<>();

        Query query = sightingsRef.orderByChild("date").startAt(DateStandardsBuddy.getISO8601MINStringForDate(new Date(chartStart))).
                endAt(DateStandardsBuddy.getISO8601MAXStringForDate(new Date(chartEnd))).limitToLast(SIGHTINGS_LIMIT);

        Log.v(TAG, "Query is: " + query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    RatSighting sightingToAdd = data.getValue(RatSighting.class);
                    toReturn.add(sightingToAdd);
                    Log.v(TAG, "Sighting populated with Date: "  + sightingToAdd.getDate());

                }
                Log.v(TAG, "Arraylist is: " + toReturn.toString());
                startCreateChart(toReturn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }

    /**
     * Count the sightings and sort them into a HashMap
     *
     * I'm making the assumption here that the ArrayList is inorder by date from earliest to latest
     *
     * @param inputList An arraylist of rat sightings
     * @return A hashmap with years as keys and integers for how many sightings per year
     */
    private HashMap<String, Integer> sortByYear(ArrayList<RatSighting> inputList) {
        HashMap<String, Integer> toReturn = new HashMap<>();
        for (int i = 0; i < inputList.size(); i++) {
            try {
                Date aDate = DateStandardsBuddy.getDateFromISO8601ESTString(inputList.get(i).getDate());
                String isoDate = DateStandardsBuddy.getISO8601ESTSansDayStringForDate(aDate);
                String[] isoSplit = isoDate.split("-");
                String rYear = isoSplit[0];

                Log.v(TAG, rYear);

                if (toReturn.containsKey(rYear)) {
                    Log.v(TAG, "Increasing:" + rYear);
                    Integer amount = toReturn.get(rYear);
                    toReturn.put(rYear, ++amount);
                } else {
                    Log.v(TAG, "New key is:" + rYear);
                    toReturn.put(rYear, 1);
                }

            } catch (ParseException pe) {
                Log.d(TAG, "Parse Exception in sortByYear");
            } catch (Exception e) {
                Log.d(TAG, "Date from sighting caused exception in sortByYear");
            }
        }
        return toReturn;
    }

    /**
     * Count the sightings and sort them into a HashMap
     *
     * I'm making the assumption here that the ArrayList is inorder by date from earliest to latest
     *
     * @param inputList An arraylist of rat sightings
     * @return A hashmap with months as keys and integers for how many sightings per month
     */
    private HashMap<String, Integer> sortByMonth(ArrayList<RatSighting> inputList) {
        // need to count the amount of sightings per year/per month
        HashMap<String, Integer> toReturn = new HashMap<>();
        Log.v(TAG, "Input array list size is: " + inputList.size());
        for (int i = 0; i < inputList.size(); i++) {
            try {
                Date aDate = DateStandardsBuddy.getDateFromISO8601ESTString(inputList.get(i).getDate());
                String isoDate = DateStandardsBuddy.getISO8601ESTSansDayStringForDate(aDate);
                String month = isoDate.replace("-", "");

                Log.v(TAG, "Key is: " + month);

                if (toReturn.containsKey(month)) {
                    Log.v(TAG, "Incrementing key: " + month);
                    Integer amount = toReturn.get(month);
                    toReturn.put(month, ++amount);
                } else {
                    Log.v(TAG, "added key:" + month);
                    toReturn.put(month, 1);
                }

            } catch (ParseException pe) {
                Log.d(TAG, "Parse Exception in sortByMonth");
            } catch (Exception e) {
                Log.d(TAG, "Date from sighting caused exception in sortByMonth");
            }
        }
        return toReturn;
    }
}
