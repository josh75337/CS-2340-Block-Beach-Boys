package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

/**
 * Activity which allows the user to view details of an individual RatSighting
 *
 * Created by Matt on 10/9/2017.
 */

public class ViewSightingActivity extends AppCompatActivity{
    //debugging log
    private static final String TAG = "ViewSightingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsighting);

        // grabs rat sighting object passed with intent
        RatSighting sighting = (RatSighting) getIntent().getSerializableExtra("RAT_SIGHTING");

        // grab textview elements from ui
        TextView mUniqueKey = (TextView) findViewById(R.id.runiquekey);
        TextView mCreatedDate = (TextView) findViewById(R.id.rdate);
        TextView mLocationType = (TextView) findViewById(R.id.rlocationtype);
        TextView mIncidentZip = (TextView) findViewById(R.id.rzip);
        TextView mIncidentAddress = (TextView) findViewById(R.id.raddress);
        TextView mCity = (TextView) findViewById(R.id.rcity);
        TextView mBorough = (TextView) findViewById(R.id.rborough);
        TextView mLatitude = (TextView) findViewById(R.id.rlatitude);
        TextView mLongitude = (TextView) findViewById(R.id.rlongitude);

        if (sighting.getKey() != null) {
            mUniqueKey.setText(sighting.getKey());
        }
        if (sighting.getDate() != null) {
            mCreatedDate.setText(sighting.getDate() + " EST");
            try {
                Date dateFromString =
                        DateStandardsBuddy.getDateFromISO8601ESTString(sighting.getDate());
                String maxIso8601StringFromDate =
                        DateStandardsBuddy.getISO8601MAXStringForDate(dateFromString);
                String minIso8601StringFromDate =
                        DateStandardsBuddy.getISO8601MINStringForDate(dateFromString);
                Log.d(TAG,
                        "Maximum of "
                        + sighting.getDate()
                        + " is : "
                        + maxIso8601StringFromDate);
                //----------------------------------
                Log.d(TAG,
                        "Minimum of "
                        + sighting.getDate()
                        + " is : "
                        + minIso8601StringFromDate);
                //----------------------------------
            } catch (ParseException e) {
                Log.d(TAG, "ParseException while trying to parse " + sighting.getDate());
            }

        }
        if (sighting.getLocationType() != null) {
            mLocationType.setText(sighting.getLocationType());
        }
        if (sighting.getZip() != null) {
            mIncidentZip.setText(sighting.getZip());
        }
        if (sighting.getAddress() != null) {
            mIncidentAddress.setText(sighting.getAddress());
        }
        if (sighting.getCity() != null) {
            mCity.setText(sighting.getCity());
        }
        if (sighting.getBorough() != null) {
            mBorough.setText(sighting.getBorough());
        }
        if (sighting.getLatitude() != 0) {
            mLatitude.setText(Double.toString(sighting.getLatitude()));
        }
        if (sighting.getLongitude() != 0) {
            mLongitude.setText(Double.toString(sighting.getLongitude()));
        }
    }
}
