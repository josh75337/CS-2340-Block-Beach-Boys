package com.ohrats.bbb.ohrats;

import android.Manifest;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.LinkedList;

/**
 * Created by Matt on 10/23/2017.
 */

public class ViewMapFragment extends Fragment {

    private static final String TAG = "MapFragment";

    private LinkedList<RatSighting> sightingList = new LinkedList<>();

    private MapView mMapView;
    private GoogleMap mMap;
//    FusedLocationProviderClient mFusedLocationProviderClient;
//    Location mLastKnownLocation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

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

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    return;
                }
                mMap.setMyLocationEnabled(true);
//                getDeviceLocation();

            }

        });
        return view;
    }

    public void setSightingList(LinkedList list) {
        sightingList = list;
    }

//    private void getDeviceLocation() {
//    /*
//     * Get the best and most recent location of the device, which may be null in rare
//     * cases when a location is not available.
//     */
//        try {
//            Task locationResult = mFusedLocationProviderClient.getLastLocation();
//            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
//                @Override
//                public void onComplete(@NonNull Task task) {
//                    if (task.isSuccessful()) {
//                        // Set the map's camera position to the current location of the device.
//                        mLastKnownLocation = (Location) task.getResult();
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                                new LatLng(mLastKnownLocation.getLatitude(),
//                                        mLastKnownLocation.getLongitude()), 12));
//                    }
//                }
//            });
//        } catch(SecurityException e)  {
//            Log.e("Exception: %s", e.getMessage());
//        }
//    }
}
