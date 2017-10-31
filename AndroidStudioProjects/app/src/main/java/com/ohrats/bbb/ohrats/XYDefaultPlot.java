package com.ohrats.bbb.ohrats;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.nfc.Tag;
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

    private static final String TAG = "XYDefaultPlot";

    private XYPlot plot;

    private Integer[] xVals;

    private Integer[] yVals;

    private String timeframe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xyplot);

        ArrayList<Integer> domain = getIntent().getIntegerArrayListExtra("X_VALS");
        xVals = (Integer[]) domain.toArray();

        ArrayList<Integer> range = getIntent().getIntegerArrayListExtra("Y_VALS");
//        yVals = (Number[]) range.toArray();


        timeframe = getIntent().getStringExtra("XYSERIES_TITLE");

        // initialize our XYDefaultPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create a couple arrays of y-values to plot:
        final Integer[] domainLabels = xVals;

        // turn the above arrays into XYSeries':
        XYSeries monthly = new SimpleXYSeries(domain, range, timeframe);

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.BLUE, Color.WHITE, null, null);


        // add a new series' to the xyplot:
        plot.addSeries(monthly, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
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
