/*
 * Copyright 2015 AndroidPlot.com
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ohrats.bbb.ohrats;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.*;

import java.util.*;

/**
 * The simplest possible example of using AndroidPlot to plot some data.
 */
public class DefaultPieChart extends Activity
{

    private final String TAG = "DefaultPieChart";

    public static final int SELECTED_SEGMENT_OFFSET = 50;

    private TextView donutSizeTextView;
    private SeekBar donutSizeSeekBar;

    public PieChart pie;

    private ListView simpleList;

//    private Number[] xVals;
//    private Number[] yVals;
    ArrayList<Integer> domain;
    ArrayList<Integer> range;

    private Segment s1;
    private Segment s2;
    private Segment s3;
    private Segment s4;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_pie_chart);

        domain = getIntent().getIntegerArrayListExtra("X_VALS");
        Log.d(TAG, "Domain passed by intent is: " + domain.toString());
//        xVals = (Number[]) domain.toArray(xVals);

        range = getIntent().getIntegerArrayListExtra("Y_VALS");
        Log.d(TAG, "Range passed by intent is: " + range.toString());
//        yVals = (Number[]) range.toArray(yVals);

        updateListView();

        // initialize our XYPlot reference:
        pie = (PieChart) findViewById(R.id.mySimplePieChart);

        // enable the legend:
        pie.getLegend().setVisible(true);

        final float padding = PixelUtils.dpToPix(30);
        pie.getPie().setPadding(padding, padding, padding, padding);

        // detect segment clicks:
        pie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if(pie.getPie().containsPoint(click)) {
                    Segment segment = pie.getRenderer(PieRenderer.class).getContainingSegment(click);

                    if(segment != null) {
                        final boolean isSelected = getFormatter(segment).getOffset() != 0;
                        deselectAll();
                        setSelected(segment, !isSelected);
                        pie.redraw();
                    }
                }
                return false;
            }

            private SegmentFormatter getFormatter(Segment segment) {
                return pie.getFormatter(segment, PieRenderer.class);
            }

            private void deselectAll() {
                List<Segment> segments = pie.getRegistry().getSeriesList();
                for(Segment segment : segments) {
                    setSelected(segment, false);
                }
            }

            private void setSelected(Segment segment, boolean isSelected) {
                SegmentFormatter f = getFormatter(segment);
                if(isSelected) {
                    f.setOffset(SELECTED_SEGMENT_OFFSET);
                } else {
                    f.setOffset(0);
                }
            }
        });

        donutSizeSeekBar = (SeekBar) findViewById(R.id.donutSizeSeekBar);
        donutSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pie.getRenderer(PieRenderer.class).setDonutSize(seekBar.getProgress()/100f,
                        PieRenderer.DonutMode.PERCENT);
                pie.redraw();
                updateDonutText();
            }
        });

//        donutSizeTextView = (TextView) findViewById(R.id.donutSizeTextView);
//        updateDonutText();

        ArrayList<Segment> pieSegments = new ArrayList<Segment>();
        for(int i = 0; i < domain.size(); i++) {
            pieSegments.add(new Segment(domain.get(i).toString(), range.get(i)));
        }


//        s1 = new Segment("s1", 3);
//        s2 = new Segment("s2", 1);
//        s3 = new Segment("s3", 7);
//        s4 = new Segment("s4", 9);

        EmbossMaskFilter emf = new EmbossMaskFilter(
                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

        ArrayList<SegmentFormatter> pieSegmentFormatters = new ArrayList<SegmentFormatter>();
        Random rand = new Random();
        int randomColor;
        int r;
        int g;
        int b;
        for (int i = 0; i < domain.size(); i++) {
            r = rand.nextInt(255);
            g = rand.nextInt(255);
            b = rand.nextInt(255);
            randomColor = Color.rgb(r, g, b);
            pieSegmentFormatters.add(new SegmentFormatter(randomColor));
        }
//        SegmentFormatter sf1 = new SegmentFormatter(Color.RED);
//        SegmentFormatter sf2 = new SegmentFormatter(Color.BLACK);
//        SegmentFormatter sf3 = new SegmentFormatter(Color.BLUE);
//        SegmentFormatter sf4 = new SegmentFormatter(Color.GREEN);

//        SegmentFormatter sf1 = new SegmentFormatter(this, R.xml.pie_segment_formatter1);
//        sf1.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
//        sf1.getFillPaint().setMaskFilter(emf);
//
//        SegmentFormatter sf2 = new SegmentFormatter(this, R.xml.pie_segment_formatter2);
//        sf2.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
//        sf2.getFillPaint().setMaskFilter(emf);
//
//        SegmentFormatter sf3 = new SegmentFormatter(this, R.xml.pie_segment_formatter3);
//        sf3.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
//        sf3.getFillPaint().setMaskFilter(emf);
//
//        SegmentFormatter sf4 = new SegmentFormatter(this, R.xml.pie_segment_formatter4);
//        sf4.getLabelPaint().setShadowLayer(3, 0, 0, Color.BLACK);
//        sf4.getFillPaint().setMaskFilter(emf);

        for(int i = 0; i < pieSegments.size(); i++) {
            pie.addSegment(pieSegments.get(i), pieSegmentFormatters.get(i));
        }

//        pie.addSegment(s1, sf1);
//        pie.addSegment(s2, sf2);
//        pie.addSegment(s3, sf3);
//        pie.addSegment(s4, sf4);

        pie.getBorderPaint().setColor(Color.TRANSPARENT);
        pie.getBackgroundPaint().setColor(Color.TRANSPARENT);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupIntroAnimation();
    }

    protected void updateDonutText() {
        donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
    }

    private void updateListView() {
        ArrayList<String>pieDataList = new ArrayList<String>();
        for(int i = 0; i < domain.size(); i++) {
            pieDataList.add("Time-frame: " + domain.get(i).toString()
                            + " Occurrences: " + range.get(i).toString());
        }
        simpleList = (ListView) this.findViewById(R.id.pie_listview);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this,
                        R.layout.activity_pie_list_item,
                        R.id.textView,
                        pieDataList);
        //---------------------------------------------
        simpleList.setAdapter(arrayAdapter);
    }


    protected void setupIntroAnimation() {

        final PieRenderer renderer = pie.getRenderer(PieRenderer.class);
        // start with a zero degrees pie:

        renderer.setExtentDegs(0);
        // animate a scale value from a starting val of 0 to a final value of 1:
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        // use an animation pattern that begins and ends slowly:
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
                renderer.setExtentDegs(360 * scale);
                pie.redraw();
            }
        });

        // the animation will run for 1.5 seconds:
        animator.setDuration(1500);
        animator.start();
    }
}