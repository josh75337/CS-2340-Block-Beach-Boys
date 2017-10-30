package com.ohrats.bbb.ohrats;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChartHubActivity extends AppCompatActivity {

    private static final String TAG = "ChartHubActivity";

    //spinner to selection date range: Monthly, Yearly, etc.
    private Spinner typeSpinner;

    private Button mLaunchChartButton;

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

        typeSpinner = (Spinner) findViewById(R.id.type_spinner);

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.chart_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

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
                getCurrentFocus().clearFocus();
                mLaunchChartButton.setEnabled(true);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
                getCurrentFocus().clearFocus();
                mLaunchChartButton.setEnabled(true);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        //launch chart
        mLaunchChartButton = (Button) findViewById(R.id.launch_chart_button);
        mLaunchChartButton.setEnabled(false);
        mLaunchChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateChart();
            }
        });



    }


    private void startCreateChart() {
        //determine what type of chart the user wants
        String chartType = (String) typeSpinner.getSelectedItem();

        //create a title based of of the chart
        String title = "Sighting by Month and Year";
        switch (chartType) {
            case "XY Plot": title = title + ": XY Plot";
                break;
            default: chartType = "XY Plot";
                break;
        }

        createChart(title, chartType);
    }

    private void createChart(String title, String chartType) {
        Log.v(TAG, "Called createChart()");

        //create a default to the xy plot
        Intent in = new Intent(this, XYDefaultPlot.class);

        //determine what plot the user wants
        switch (chartType) {
            case "Histogram":
                Log.v(TAG, "creating histogram");
                break;
            default:
                //this is the XYDefaultPlot
                break;
        }

        //setup the plot with the user selected values


        //navigate the the plot
        startActivity(in);
    }
}
