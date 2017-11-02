package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;

public class DefaultBarChart extends AppCompatActivity {

    private final String TAG = "DefaultBarChart";

    private XYPlot plot;

    private Number[] xVals = new Number[10];

    private Number[] yVals = new Number[10];

    private String timeframe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyplot);

        ArrayList<Integer> domain = getIntent().getIntegerArrayListExtra("X_VALS");
        Log.d(TAG, "Domain passed by intent is: " + domain.toString());
        xVals = (Number[]) domain.toArray(xVals);

        ArrayList<Integer> range = getIntent().getIntegerArrayListExtra("Y_VALS");
        Log.d(TAG, "Range passed by intent is: " + range.toString());
        yVals = (Number[]) range.toArray(yVals);


        timeframe = getIntent().getStringExtra("SERIES_TITLE");

        // initialize our XYDefaultPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = xVals;

        // turn the above arrays into XYSeries':
        Log.v(TAG, yVals.toString());
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(yVals), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, timeframe);
        Log.d(TAG, series1.toString());

        BarFormatter bf = new BarFormatter(Color.RED, Color.WHITE);

        PanZoom.attach(plot);

        // add a new series' to the xyplot:
        plot.addSeries(series1, bf);

        final ArrayList<Integer> alreadyListed = new ArrayList<>();
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Number tempNum = (Number) obj;
                double d = tempNum.doubleValue();
                int i = (int) Math.ceil(d);
                return toAppendTo.append(domainLabels[i]);
//                if (alreadyListed.contains(i)) {
//                    Log.d(TAG, obj.toString() + "integer is already contained.");
//                    return toAppendTo.append("");
//                } else {
//                    alreadyListed.add(i);
//                    Log.d(TAG, obj.toString() + "integer is " + i);
//                    return toAppendTo.append(domainLabels[i]);
//                }
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }

}