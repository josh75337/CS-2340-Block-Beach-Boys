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

//import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The simplest possible example of using AndroidPlot to plot some data.
 */
@SuppressWarnings("MagicNumber") // All of suppressed magic numbers are from API code
@SuppressLint("ClickableViewAccessibility") // support of performClick is out of scope
public class DefaultPieChart extends Activity
{

    @SuppressWarnings("FieldCanBeLocal") // Shouldn't be local var because bad for visibility
    private final String TAG = "DefaultPieChart";

    private static final int SELECTED_SEGMENT_OFFSET = 50;

    @SuppressWarnings("unused") // this is very clearly used, gives errors if removed
    private TextView donutSizeTextView;

    @SuppressWarnings("FieldCanBeLocal") // this is from API code, should be kept for clarity
    private SeekBar donutSizeSeekBar;

    private PieChart pie;

    // Shouldn't be local var because may be used in
    // multiple functions later and should be accessible
    @SuppressWarnings("FieldCanBeLocal")
    private ListView simpleList;

    private List<Integer> domain;
    private List<Integer> range;

    // --Commented out by Inspection (11/11/2017 4:46 PM):private Segment s1;
    // --Commented out by Inspection (11/11/2017 4:46 PM):private Segment s2;
    // --Commented out by Inspection (11/11/2017 4:46 PM):private Segment s3;
    // --Commented out by Inspection (11/11/2017 4:46 PM):private Segment s4;


    @Override
    // The API code uses quite a few chained methods
    // The API code's onCreate method is pretty long, but this is fine in this case
    @SuppressWarnings({"ChainedMethodCall", "OverlyLongMethod"})
    @SuppressLint("ClickableViewAccessibility") // this suppress doesn't work for some reason
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_pie_chart);

        domain = getIntent().getIntegerArrayListExtra("X_VALUES");
        if (domain != null) {
            Log.d(TAG, "Domain passed by intent is: " + domain.toString());
        } else {
            Log.d(TAG, "Domain passed by intent was null!");
        }

        range = getIntent().getIntegerArrayListExtra("Y_VALUES");
        if (range != null) {
            Log.d(TAG, "Range passed by intent is: " + range.toString());
        } else {
            Log.d(TAG, "Range passed by intent was null!");
        }

        updateListView();

        // initialize our XYPlot reference:
        pie = (PieChart) findViewById(R.id.mySimplePieChart);

        // enable the legend:
        pie.getLegend().setVisible(true);

        final float padding = PixelUtils.dpToPix(30);
        pie.getPie().setPadding(padding, padding, padding, padding);

        // detect segment clicks:

        // can't suppress an accessibility warning here
        // android is super pedantic about a touch versus a click
        pie.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PointF click = new PointF(motionEvent.getX(), motionEvent.getY());
                if(pie.getPie().containsPoint(click)) {
                    Segment segment =
                            pie.getRenderer(PieRenderer.class).getContainingSegment(click);
                    //---------------------------------------------------------------------
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

        //donutSizeTextView = (TextView) findViewById(R.id.donutSizeTextView);
        updateDonutText();

        List<Segment> pieSegments = new ArrayList<>();
        for(int i = 0; i < domain.size(); i++) {
            pieSegments.add(new Segment(domain.get(i).toString(), range.get(i)));
        }


//        s1 = new Segment("s1", 3);
//        s2 = new Segment("s2", 1);
//        s3 = new Segment("s3", 7);
//        s4 = new Segment("s4", 9);

//        EmbossMaskFilter emf = new EmbossMaskFilter(
//                new float[]{1, 1, 1}, 0.4f, 10, 8.2f);

        List<SegmentFormatter> pieSegmentFormats = new ArrayList<>();
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
            pieSegmentFormats.add(new SegmentFormatter(randomColor));
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
            pie.addSegment(pieSegments.get(i), pieSegmentFormats.get(i));
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

    @SuppressLint("SetTextI18n") // can't use @string value because is not static
    private void updateDonutText() {
        if (donutSizeTextView != null) {
            donutSizeTextView.setText(donutSizeSeekBar.getProgress() + "%");
        }
    }

    @SuppressWarnings("ChainedMethodCall") // this helps with code clarity for adding to the list
    private void updateListView() {
        ArrayList<String>pieDataList = new ArrayList<>();
        for(int i = 0; i < domain.size(); i++) {
            pieDataList.add("Time-frame: " + domain.get(i).toString()
                            + " Occurrences: " + range.get(i).toString());
        }
        simpleList = (ListView) this.findViewById(R.id.pie_listview);
        ListAdapter arrayAdapter =
                new ArrayAdapter<>(this,
                        R.layout.activity_pie_list_item,
                        R.id.textView,
                        pieDataList);
        //---------------------------------------------
        simpleList.setAdapter(arrayAdapter);
    }


    private void setupIntroAnimation() {

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