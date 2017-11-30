package com.ohrats.bbb.ohrats;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;



/**
 * fragment for the uploading of one rat sighting
 * Created by Matt on 10/6/2017.
 * Edited by Josh and Eli for m7
 */


@SuppressWarnings("CyclicClassDependency")
public class SingleFragment extends Fragment {
    //debugging log
    private static final String TAG = "SingleFragment";

    //Firebase (not typo) real-time database reference
    private DatabaseReference mDatabase;

    //UI references
    private Spinner locationType;
    private EditText address;
    private EditText zip;
    private EditText city;
    private Spinner boroughSpinner;
    private EditText latitude;
    private EditText longitude;
    private Switch mCurrentLoction;

    //RatSighting information
    /*
    * the queens is hardcoded as the default choice to prevent a null pointer exception
    * */
//    private String _borough = "Queens";
    private String _key;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single, container, false);
    }

    @SuppressWarnings("ChainedMethodCall")
    /*
    * see within the method for explanations
    * */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        locationType = (Spinner) view.findViewById(R.id.IncidentLocationType);
        address = (EditText) view.findViewById(R.id.IncidentAddress);
        zip = (EditText) view.findViewById(R.id.IncidentZip);
        city = (EditText) view.findViewById(R.id.IncidentCity);
        boroughSpinner = (Spinner) view.findViewById(R.id.boroughSpinner);
        latitude = (EditText) view.findViewById(R.id.IncidentLatitude);
        longitude = (EditText) view.findViewById(R.id.IncidentLongitude);

        ArrayAdapter<CharSequence> boroughAdapter = ArrayAdapter.createFromResource(
                this.getActivity(), R.array.boroughs_array,
                    android.R.layout.simple_spinner_item);
        boroughAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boroughSpinner.setAdapter(boroughAdapter);

        ArrayAdapter<LocationTypes> locationTypeAdapter
                = new ArrayAdapter<>(this.getActivity(),
                    android.R.layout.simple_spinner_item,
                        Arrays.asList(LocationTypes.values()));

        locationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationType.setAdapter(locationTypeAdapter);

        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(getActivity());

        mCurrentLoction = (Switch) view.findViewById(R.id.rcurrentlocation);
        mCurrentLoction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    latitude.setVisibility(View.GONE);
                    longitude.setVisibility(View.GONE);
                    if ((ActivityCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) &&
                            (ActivityCompat.checkSelfPermission(getActivity(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                        mCurrentLoction.setChecked(false);
                        return;
                    }

                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                mLastKnownLocation = (Location) task.getResult();
                            }
                        }
                    });
                } else {
                    latitude.setVisibility(View.VISIBLE);
                    longitude.setVisibility(View.VISIBLE);
                }
            }
        });

        Button submitButton = (Button) view.findViewById(R.id.submitSightingButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRatSighting();
            }
        });



        //initialize database reference
        FirebaseDatabase instance = FirebaseDatabase.getInstance();

        mDatabase = instance.getReference();

        //find last key
        /*
        having a chained method call here allows us to not
         have to send such a big query to Firebase (still not typo)
        * */
        Query lastQuery = mDatabase.child("sightings").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot != null) && (dataSnapshot.getValue() != null)) {
                    /*
                    * any chained method calls within here are safe because of the if statement
                    * */
                    Log.i(TAG, dataSnapshot.getValue().toString());
                    String lastSighting = dataSnapshot.getValue().toString();
                    String oldKey = lastSighting.substring(1, 9);
                    Log.i(TAG, oldKey);
                    _key = (Integer.parseInt(oldKey) + 1) + "";
                } else {
                    _key = "1";
                }

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
    @SuppressWarnings("ChainedMethodCall")
    /*
    * see internals of method for explanation
    * */
    private void submitRatSighting() {
        address.setError(null);
        city.setError(null);
        zip.setError(null);
        latitude.setError(null);
        longitude.setError(null);
        if(isValid()) {
            /*
            * we have already checked that all of these spinners have selected values
            * in the isValid method
            * */
            String incidentLocationType = locationType.getSelectedItem().toString();
            Log.d(TAG, "It is not the LT");
            //again checked in isValid
            String incidentAddress = address.getText().toString();

            String incidentCity = city.getText().toString();

            String incidentBorough = boroughSpinner.getSelectedItem().toString();

            final double incidentLatitude;
            final double incidentLongitude;
            int incidentZip;
            try {
                //these are try caught
                if (mCurrentLoction.isChecked()) {
                    Log.d(TAG, "It is not the switch");
                    if (mLastKnownLocation == null) {
                        // lat long of NYC
                        incidentLatitude = 40.7128;
                        incidentLongitude = -74.0060;
                    } else {
                        incidentLatitude = mLastKnownLocation.getLatitude();
                        incidentLongitude = mLastKnownLocation.getLongitude();
                    }
                } else {
                    incidentLatitude = Double.parseDouble(latitude.getText().toString());
                    incidentLongitude = Double.parseDouble(longitude.getText().toString());
                }
                incidentZip = Integer.parseInt(zip.getText().toString());
            } catch(Exception e) {
                return;
            }

            String isoCurrentDate = DateStandardsBuddy.getISO8601ESTStringForCurrentDate();

            createRatSighting(incidentLocationType, incidentAddress, incidentCity, incidentBorough,
                    incidentZip, incidentLongitude, incidentLatitude, isoCurrentDate);
        }


    }

    /**
     * Creates a rat sighting and adds it to the database
     * @param incidentLocationType - location type for rat sighting
     * @param incidentAddress - address of rat sighting
     * @param incidentCity - city of rat sighting
     * @param incidentZip - zip of rat sighting
     * @param incidentLongitude -longitude of rat sighting
     * @param incidentLatitude - latitude of rat sighting
     * @param createdDate - the date the rat sighting was created
     */
    @SuppressWarnings("MethodWithTooManyParameters")
    //this is necessary for the creation of the rat sighting object
    private void createRatSighting(String incidentLocationType,
                                   String incidentAddress,
                                   String incidentCity,
                                   String borough,
                                   int incidentZip,
                                   double incidentLongitude,
                                   double incidentLatitude,
                                   String createdDate) {
        /*
        * It needs this many parameters this is unavoidable
        * */
        //convert zip to string
        String zip = "" + incidentZip;
        String date = "" + createdDate;

        //validateKey(_key);

//        _borough = (String) boroughSpinner.getSelectedItem();

        //create new sighting
        RatSighting newSighting = new RatSighting(_key, date, incidentLocationType, zip,
                incidentAddress, incidentCity, borough, incidentLatitude, incidentLongitude);

        //add to the database
        //noinspection ChainedMethodCall
        //firebase convention
        //noinspection ChainedMethodCall
        mDatabase.child("sightings").child(_key).setValue(newSighting);

        Log.i(TAG, "New RatSighting created and added to the database");

        Intent in = new Intent(this.getActivity(), MainActivity.class);
        startActivity(in);
    }

    /*
     * Checks if the key already has data associated
     * @param _key The current key
     */
    //private void validateKey(String _key) {
        //while the key is defined move along
        /*
         * Bob said we didn't need to cover key collisions yet but this
         * method is here for if we want to ever implement it
         */
    //}

//    *
//     * Gets the selected borough from the spinner
//     * @param parent -parent view of the spinner
//     * @param view - the view of the spinner
//     * @param position - the position of the item selected
//     * @param id- id of the spinner
//
//
//    @SuppressWarnings({"unused", "ChainedMethodCall"})
//
//         * this method needs to arguments it has now because that is what
//         * android wants I think --> android convention
//
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        _borough = parent.getItemAtPosition(position).toString();
//    }

    /**
     * validates the rat sighting
     * @return - true if it is valid and false if it isn't
     */
    @SuppressWarnings("ChainedMethodCall")
    //
    private boolean isValid() {
        boolean valid = true;

        if (TextUtils.isEmpty(address.getText().toString())) {
            address.setError("Required");
            valid = false;
        } else {
            address.setError(null);
        }

        if (TextUtils.isEmpty(city.getText().toString())) {
            city.setError("Required");
            valid = false;
        } else {
            city.setError(null);
        }

        if (TextUtils.isEmpty(zip.getText().toString())) {
            zip.setError("Required");
            valid = false;
        } else {
            zip.setError(null);
        }

        if (!mCurrentLoction.isChecked()) {
            if (TextUtils.isEmpty(latitude.getText().toString())) {
                latitude.setError("Required");
                valid = false;
            } else{
                latitude.setError(null);
            }

            if (TextUtils.isEmpty(longitude.getText().toString())) {
                longitude.setError("Required");
                valid = false;
            } else {
                longitude.setError(null);
            }
        }

        return valid;

    }
}
