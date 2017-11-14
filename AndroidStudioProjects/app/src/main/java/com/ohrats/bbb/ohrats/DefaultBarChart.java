package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.BarFormatter;
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

/**
 * A class the creates a bar chart
 * Eli Bailey
 */
public class DefaultBarChart extends AppCompatActivity {

    private Number[] xValues = new Number[10];

    private Number[] yValues = new Number[10];

    /**
     * Called on the creation of the app
     * @param savedInstanceState Passed in by the activity lifestyle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyplot);

        //Getting information from intent, suppressed method
        @SuppressWarnings("ChainedMethodCall") ArrayList<Integer> domain;
        //noinspection ChainedMethodCall
        domain = getIntent().getIntegerArrayListExtra("X_VALUES");
        String TAG = "DefaultBarChart";
        Log.d(TAG, "Domain passed by intent is: " + domain.toString());
        xValues = domain.toArray(xValues);

        //Getting information from intent, suppressed method
        @SuppressWarnings("ChainedMethodCall") ArrayList<Integer> range;
        //noinspection ChainedMethodCall
        range = getIntent().getIntegerArrayListExtra("Y_VALUES");
        Log.d(TAG, "Range passed by intent is: " + range.toString());
        yValues = range.toArray(yValues);

        //Getting information from intent, suppressed method
        @SuppressWarnings("ChainedMethodCall")
        String timeFrame;
        //noinspection ChainedMethodCall
        timeFrame = getIntent().getStringExtra("SERIES_TITLE");

        // initialize our XYDefaultPlot reference:
        XYPlot plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = xValues;

        // turn the above arrays into XYSeries':
        //Log.v(TAG, yValues.toString());
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(yValues),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, timeFrame);
        //Log.d(TAG, series1.toString());

        BarFormatter bf = new BarFormatter(Color.RED, Color.WHITE);

        PanZoom.attach(plot);

        // add a new series' to the xy plot:
        plot.addSeries(series1, bf);

        //Getting information from intent, suppressed method
        //STANDARD for the android plot library
        //noinspection ChainedMethodCall,ChainedMethodCall
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo,
                                       @NonNull FieldPosition pos) {
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
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });

    }

}