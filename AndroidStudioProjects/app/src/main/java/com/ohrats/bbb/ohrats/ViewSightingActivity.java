package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Activity which allows the user to view details of an individual RatSighting
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

        if (sighting.getKey() != null) {
            mUniqueKey.setText(sighting.getKey());
        }
        if (sighting.getDate() != null) {
            mCreatedDate.setText(sighting.getDate().toString());
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
