package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
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

public class XYDefaultPlot extends AppCompatActivity {

    private static final String TAG = "XYDefaultPlot";

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
        Log.d(TAG, yVals.toString());
        XYSeries monthly = new SimpleXYSeries(Arrays.asList(yVals), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, timeframe);
        Log.d(TAG, monthly.toString());

        // create formatters to use for drawing a series using LineAndPointRenderer
        LineAndPointFormatter series1Format = new LineAndPointFormatter(null, Color.BLUE, null, null);

        PanZoom.attach(plot);

        //limit the amount of lines on the x axis
        plot.getGraph().setLinesPerDomainLabel(domain.size());

        //set insets on the graph
        plot.getGraph().getLineLabelInsets().setLeft(PixelUtils.dpToPix(-5));

        // add a new series' to the xyplot:
        plot.addSeries(monthly, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Number tempNum = (Number) obj;
                double d = tempNum.doubleValue();
                int i = (int) Math.ceil(d);
                if (i < 0) {
                    i = 0;
                }
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }

}
