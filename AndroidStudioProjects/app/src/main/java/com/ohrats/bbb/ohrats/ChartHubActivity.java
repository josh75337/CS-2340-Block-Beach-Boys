package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

public class ChartHubActivity extends AppCompatActivity {

    private static final String TAG = "ChartHubActivity";

    //spinner to selection date range: Monthly, Yearly, etc.
    private Spinner typeSpinner;

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

        //load chart button
        //Registration Button
        Button rRegisterButton = (Button) findViewById(R.id.launch_chart_button);
        rRegisterButton.setOnClickListener(new View.OnClickListener() {
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
