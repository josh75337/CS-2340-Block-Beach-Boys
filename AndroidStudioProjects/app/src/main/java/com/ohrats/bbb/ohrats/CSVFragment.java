package com.ohrats.bbb.ohrats;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * fragment that handles uploading csvs
 * Created by Matt on 10/6/2017.
 */

public class CSVFragment extends Fragment {

    private DatabaseReference mDatabase;

    private static final String TAG = "CSVFragment";

    private Button addCSV;
    int count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csv, container, false);
        addCSV = (Button) view.findViewById(R.id.raddcsv);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                writeSightingCSV();
            }
        });

        return view;
    }

    private void writeNewSighting(String key, String date, String locationType, String zip, String address,
                                  String city, String borough, double latitude, double longitude) {
        RatSighting sighting = new RatSighting(key, date, locationType, zip, address,
                city, borough, latitude, longitude);
        /*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Long priority = 0l;
        try {
            priority = sdf.parse(date).getTime();
        } catch (Exception e) {
            e.getMessage();
            e.getCause();
        }*/
        mDatabase.child("sightings2").child(key).setValue(sighting);
        count++;
        Log.d(TAG, "Count is " + count);
        //mDatabase.child("sightings2").child(key).setPriority(priority);
    }

    private void writeSightingCSV() {
        // Still need to manually give app permission to read and edit files:
        // https://stackoverflow.com/a/38578137
        Log.d(TAG, "writeSightingCSV called");
        String csvFileName = "Rat_Sightings.csv";
        File dataFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), csvFileName);
        Log.d(TAG, "dataFolder path: " + dataFolder.getPath());
//        for (String aFileName : dataFolder.list()) {
//            Log.d(TAG, "dataFolder Contents: " + aFileName);
//        }
        File csvFile = new File(dataFolder, csvFileName);
        //String csvFile = Environmentat.getExternalStorageDirectory().getPath().concat("/Android/data/org.krupczak.matthew/Rat_Sightings.csv");
        Log.d(TAG, "does Rat_Sightings.csv exist?: " + csvFile.exists());
        Log.d(TAG, ".canRead() Rat_Sightings.csv ?: " + csvFile.canRead());
        Log.d(TAG, ".canWrite() Rat_Sightings.csv ?: " + csvFile.canWrite());
        Log.d(TAG, "writeSightingCSV with path: " + csvFile.getPath());
        BufferedReader br = null;
        String line = "";
        String splitBy = ",";

        try {
            FileInputStream fis = new FileInputStream(csvFile);
            Log.d(TAG, "writeSightingCSV FileInputStream instantiated");
            br = new BufferedReader(new InputStreamReader(fis));
            Log.d(TAG, "writeSightingCSV BufferedReader instantiated");
            line = br.readLine();
            String[] sighting = line.split(splitBy);
            int[] fieldIndex = new int[9];
            for (int count = 0; count < sighting.length; count++) {
                switch (sighting[count]) {
                    case "Unique Key":
                        fieldIndex[0] = count;
                        break;
                    case "Created Date":
                        fieldIndex[1] = count;
                        break;
                    case "Location Type":
                        fieldIndex[2] = count;
                        break;
                    case "Incident Zip":
                        fieldIndex[3] = count;
                        break;
                    case "Incident Address":
                        fieldIndex[4] = count;
                        break;
                    case "City":
                        fieldIndex[5] = count;
                        break;
                    case "Borough":
                        fieldIndex[6] = count;
                        break;
                    case "Latitude":
                        fieldIndex[7] = count;
                        break;
                    case "Longitude":
                        fieldIndex[8] = count;
                        break;
                    default:
                        break;
                }
            }
            // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
            while ((line = br.readLine()) != null) {
                sighting = line.split(splitBy);
                int sightingLength = sighting.length;
                String key = (fieldIndex[0] < sightingLength) ? sighting[fieldIndex[0]] : null;
                String date = (fieldIndex[1] < sightingLength) ? sighting[fieldIndex[1]] : null;
                String locationType = (fieldIndex[2] < sightingLength) ? sighting[fieldIndex[2]] : null;
                String zip = (fieldIndex[3] < sightingLength) ? sighting[fieldIndex[3]] : null;
                String address = (fieldIndex[4] < sightingLength) ? sighting[fieldIndex[4]] : null;
                String city = (fieldIndex[5] < sightingLength) ? sighting[fieldIndex[5]] : null;
                String borough = (fieldIndex[6] < sightingLength) ? sighting[fieldIndex[6]] : null;
                double latitude = (fieldIndex[7] < sightingLength) ? Double.parseDouble(sighting[fieldIndex[7]]) : 0;
                double longitude = (fieldIndex[8] < sightingLength) ? Double.parseDouble(sighting[fieldIndex[8]]) : 0;
                writeNewSighting(key, date, locationType, zip, address, city, borough, latitude, longitude);
            }
        } catch (Exception e) {
            e.getMessage();
            e.getCause();
        }

    }
}
