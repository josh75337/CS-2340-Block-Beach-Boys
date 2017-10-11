package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Matt on 10/9/2017.
 */

public class ViewSightingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewsighting);

        RatSighting sighting = (RatSighting) getIntent().getSerializableExtra("RAT_SIGHTING");
        TextView mUniqueKey = (TextView) findViewById(R.id.runiquekey);
        TextView mCreatedDate = (TextView) findViewById(R.id.rdate);
        TextView mLocationType = (TextView) findViewById(R.id.rlocationtype);
        TextView mIncidentZip = (TextView) findViewById(R.id.rzip);
        TextView mIncidentAddress = (TextView) findViewById(R.id.raddress);
        TextView mCity = (TextView) findViewById(R.id.rcity);
        TextView mBorough = (TextView) findViewById(R.id.rborough);
        TextView mLatitude = (TextView) findViewById(R.id.rlatitude);
        TextView mLongitude = (TextView) findViewById(R.id.rlongitude);

        if (!sighting.getKey().equals("")) {
            mUniqueKey.setText(sighting.getKey());
        }
        if (!sighting.getDate().equals("")) {
            mCreatedDate.setText(sighting.getDate());
        }
        if (!sighting.getLocationType().equals("")) {
            mLocationType.setText(sighting.getLocationType());
        }
        if (!sighting.getZip().equals("")) {
            mIncidentZip.setText(sighting.getZip());
        }
        if (!sighting.getAddress().equals("")) {
            mIncidentAddress.setText(sighting.getAddress());
        }
        if (!sighting.getCity().equals("")) {
            mCity.setText(sighting.getCity());
        }
        if (!sighting.getBorough().equals("")) {
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
