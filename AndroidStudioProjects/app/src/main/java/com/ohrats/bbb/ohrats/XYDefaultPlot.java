package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.graphics.DashPathEffect;
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

public class XYDefaultPlot extends AppCompatActivity {

    private XYPlot plot;

    private Number[] xVals;

    private Number[] yVals;

    private String timeframe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyplot);

        ArrayList<Integer> domain = getIntent().getIntegerArrayListExtra("X_VALS");
        xVals = new Number[domain.size()];
        for (int j = 0; j < domain.size(); j++) {
            xVals[j] = domain.get(j);
        }

        ArrayList<Integer> range = getIntent().getIntegerArrayListExtra("Y_VALS");
        yVals = new Number[range.size()];
        for (int j = 0; j < range.size(); j++) {
            yVals[j] = range.get(j);
        }
        timeframe = getIntent().getStringExtra("XYSERIES_TITLE");

        // initialize our XYDefaultPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Number[] domainLabels = xVals;
        Number[] series1Numbers = yVals;

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries monthly = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, timeframe);

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.BLUE, Color.WHITE, null, null);


        // add a new series' to the xyplot:
        plot.addSeries(monthly, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Log.v("XY Default Plot", obj.toString());
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }

}
