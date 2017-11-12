package com.ohrats.bbb.ohrats;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.LinkedList;

/**
 * Displays rat sighting data using GoogleMaps API
 * Created by Matt on 10/23/2017.
 */

public class ViewMapFragment extends Fragment {

//    private static final String TAG = "MapFragment";

    private LinkedList<RatSighting> sightingList = new LinkedList<>();
    private final LinkedList<Marker> markerList = new LinkedList<>();

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        MapView mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.getMessage();
            e.getCause();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent myIntent = new Intent(getActivity(), ViewSightingActivity.class);
                        myIntent.putExtra("RAT_SIGHTING", (RatSighting) marker.getTag());
                        startActivity(myIntent);
                    }
                });
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
                updateMapMarkers();

            }

        });
        return view;
    }


    /**
     * sets sighting list
     */
    public void setSightingList(LinkedList<RatSighting> list) {
        sightingList = list;
    }

    /**
     * public method called by combo activity that calls the updateMapMarkers method
     */
    public void update() {
        updateMapMarkers();
    }

    /**
     * updates markers on map
     */
    private void updateMapMarkers() {
        for (Marker m : markerList) {
            m.remove();
        }
        for (RatSighting r : sightingList) {
            String rDate = (!r.getDate().equals("")) ? r.getDate() : "N/A";
            String rAddress = (!r.getAddress().equals("")) ? r.getAddress() : "N/A";
            double rLat = (r.getLatitude() != 0) ? r.getLatitude() : 40.7128;
            double rLong = (r.getLongitude() != 0) ? r.getLongitude() : -74.0060;

            // @TODO Somehow a null pointer happened here?
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(rLat, rLong))
                    .title(r.getKey())
                    .snippet(String.format("Date: %10.10s | Address: %.20s", rDate, rAddress)));
            m.setTag(r);
            markerList.add(m);
        }
    }

    /**
     * gets the location of the device and focuses camera on that location
     */
    private void getDeviceLocation() {

//     Get the best and most recent location of the device, which may be null in rare
//     cases when a location is not available.

        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = (Location) task.getResult();
                        if (mLastKnownLocation == null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(40.7128, -74.0060), 12));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 12));
                        }
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
