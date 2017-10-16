package com.ohrats.bbb.ohrats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * fragment that handles uploading csvs
 * Created by Matt on 10/6/2017.
 */


public class SingleFragment extends Fragment {
    private static final String TAG = "SingleFragment";

    //UI references
    private Spinner locationType;
    private EditText address;
    private EditText zip;
    private EditText city;
    private Spinner boroughSpinner;
    private EditText latitude;
    private EditText longitude;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        locationType = (Spinner) view.findViewById(R.id.IncidentLocationType);
        address = (EditText) view.findViewById(R.id.IncidentAddress);
        zip = (EditText) view.findViewById(R.id.IncidentZip);
        city = (EditText) view.findViewById(R.id.IncidentCity);
        boroughSpinner = (Spinner) view.findViewById(R.id.boroughSpinner);
        latitude = (EditText) view.findViewById(R.id.IncidentLatitude);
        longitude = (EditText) view.findViewById(R.id.IncidentLongitude);

        ArrayAdapter<Boroughs> boroughAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, Arrays.asList(Boroughs.values()));
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

        ArrayAdapter<LocationTypes> locationTypeAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, Arrays.asList(LocationTypes.values()));
        locationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationType.setAdapter(locationTypeAdapter);


        Button submitButton = (Button) view.findViewById(R.id.submitSightingButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRatSighting();
            }
        });
    }

    /**
     * called when the user clicks the submit button. Calls validation method
     * and creates rat sighting object and if valid adds it to the database
     */
    private void submitRatSighting() {
        if(isValid()) {
            String incidentLocationType = locationType.getSelectedItem().toString();
            Log.d(TAG, "It is not the LT");
            //may need to do some validation around addresses?
            String incidentAddress = address.getText().toString();

            String incidentCity = city.getText().toString();


            double incidentLatitude;
            double incidentLongitude;
            int incidentZip;
            try {
                incidentLatitude = Double.parseDouble(latitude.getText().toString());
                incidentLongitude = Double.parseDouble(longitude.getText().toString());
                incidentZip = Integer.parseInt(zip.getText().toString());
            } catch(Exception e) {
                return;
            }
            //note that the default value is the date time at which the object is created
            // so this is fine
            Date createdDate = new Date();

            createRatSighting(incidentLocationType, incidentAddress, incidentCity,
                    incidentZip, incidentLongitude, incidentLatitude, createdDate);
        } else {
            return;
        }


    }

    private void createRatSighting(String incidentLocationType,
                                   String incidentAddress,
                                   String incidentCity,
                                   int incidentZip,
                                   double incidentLongitude,
                                   double incidentLatitude,
                                   Date createdDate) {
        //eli here is where we need to query firebase to determine the unique key and then add the new rat sighting to the database
    }

    private boolean isValid() {

        if (locationType.getSelectedItem() == null) {
            return false;
        } else if (TextUtils.isEmpty(address.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(city.getText().toString())) {
            return false;
        } else if(boroughSpinner.getSelectedItem() == null) {
            return false;
        } else if (TextUtils.isEmpty(latitude.getText().toString())) {
            return false;
        } else if(TextUtils.isEmpty(longitude.getText().toString())) {
            return false;
        } else if (TextUtils.isEmpty(zip.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }
}
