package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;



/**
 * fragment that handles uploading csvs
 * Created by Matt on 10/6/2017.
 * Edited by Josh and Eli for m7
 */


public class SingleFragment extends Fragment {
    //debugging log
    private static final String TAG = "SingleFragment";

    //Firebase realtime database reference
    private DatabaseReference mDatabase;

    //UI references
    private Spinner locationType;
    private EditText address;
    private EditText zip;
    private EditText city;
    private Spinner boroughSpinner;
    private EditText latitude;
    private EditText longitude;

    //RatSighting information
    private String _borough = "Queens";
    private String _key;


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

        //initialize database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //find last key
        Query lastQuery = mDatabase.child("sightings").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, dataSnapshot.getValue().toString());
                String lastSighting = dataSnapshot.getValue().toString();
                String oldKey = lastSighting.substring(1, 9);
                Log.i(TAG, oldKey);
                _key = (Integer.parseInt(oldKey) + 1) + "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getDetails());
                mDatabase.removeEventListener(this);
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

            String dateTime = createdDate.toString();


            TimeZone tz = TimeZone.getTimeZone("EST");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // Quoted "Z" to indicate UTC, no timezone offset
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());

            createRatSighting(incidentLocationType, incidentAddress, incidentCity,
                    incidentZip, incidentLongitude, incidentLatitude, nowAsISO);
        } else {
            return;
        }


    }

    /**
     * Creates a rat sighting and adds it to the database
     * @param incidentLocationType - location type for rat sighting
     * @param incidentAddress - address of rat sighting
     * @param incidentCity - city of rat sighting
     * @param incidentZip - zip of rat sighiting
     * @param incidentLongitude -longitude of rat sighitng
     * @param incidentLatitude - latitude of rat sighting
     * @param createdDate - the date the rat sighting was created
     */
    private void createRatSighting(String incidentLocationType,
                                   String incidentAddress,
                                   String incidentCity,
                                   int incidentZip,
                                   double incidentLongitude,
                                   double incidentLatitude,
                                   String createdDate) {
        //eli here is where we need to query firebase to determine the unique key and then add the new rat sighting to the database

        //convert zip to string
        String zip = "" + incidentZip;
        String date = "" + createdDate;

        validateKey(_key);

        //create new sighting
        RatSighting newSighting = new RatSighting(_key, date, incidentLocationType, zip,
                incidentAddress, incidentCity, _borough, incidentLatitude, incidentLongitude);

        //add to the database
        mDatabase.child("sightings").child(_key).setValue(newSighting);

        Log.i(TAG, "New RatSighting created and added to the database");

        Intent in = new Intent(this.getActivity(), MainActivity.class);
        startActivity(in);
    }

    /**
     * Checks if the key already has data associated
     * @param _key The current key
     */
    private void validateKey(String _key) {
        //while the key is defined move along
    }

    /**
     * Gets the selected borough from the spinner
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _borough = parent.getItemAtPosition(position).toString();
    }

    /**
     * validates the rat sighting
     * @return - true if it is valid and false if it isnt
     */
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
