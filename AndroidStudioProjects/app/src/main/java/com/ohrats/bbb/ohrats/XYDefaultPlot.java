package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.xy.LineAndPointFormatter;
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
 * A class the creates an XY Plot
 * Eli Bailey
 */
public class XYDefaultPlot extends AppCompatActivity {

    private static final String TAG = "XYDefaultPlot";

    private Number[] xValues = new Number[10];

    private Number[] yValues = new Number[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyplot);

        @SuppressWarnings("ChainedMethodCall") ArrayList<Integer> domain;
        //noinspection ChainedMethodCall
        domain = getIntent().getIntegerArrayListExtra("X_VALUES");
        Log.d(TAG, "Domain passed by intent is: " + domain.toString());
        xValues = domain.toArray(xValues);

        @SuppressWarnings("ChainedMethodCall") ArrayList<Integer> range;
        //noinspection ChainedMethodCall
        range = getIntent().getIntegerArrayListExtra("Y_VALUES");
        Log.d(TAG, "Range passed by intent is: " + range.toString());
        yValues = range.toArray(yValues);


        //Line too long because of suppressed
        @SuppressWarnings("ChainedMethodCall") String timeFrame;
        //noinspection ChainedMethodCall
        timeFrame = getIntent().getStringExtra("SERIES_TITLE");

        // initialize our XYDefaultPlot reference:
        XYPlot plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = xValues;

        // turn the above arrays into XYSeries':
        //Log.d(TAG, yValues.toString());
        XYSeries monthly = new SimpleXYSeries(Arrays.asList(yValues),
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, timeFrame);
        //Log.d(TAG, monthly.toString());

        // create formatter to use for drawing a series using LineAndPointRenderer
        LineAndPointFormatter series1Format;
        series1Format = new LineAndPointFormatter(null, Color.WHITE,
                null, null);

        // axis labels may get messed up by this
        //PanZoom.attach(plot);

        // add a new series' to the xy plot:
        plot.addSeries(monthly, series1Format);

        //noinspection ChainedMethodCall,ChainedMethodCall
        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo,
                                       @NonNull FieldPosition pos) {
                Number tempNum = (Number) obj;
                double d = tempNum.doubleValue();
                int i = (int) Math.ceil(d);
                if (i < 0) {
                    i = 0;
                }
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });

    }

}
