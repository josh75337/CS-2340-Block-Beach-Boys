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

    /**
     * Static Nested class which contains info on whether or not
     * CSV fragment is currently uploading data
     */
    public static class UploadStateNestedClass {
        private static boolean isUploading = false;

        /**
         * Fetches the upload state of CSVFragment
         * @return boolean whether a CSV is currently being updated to the database
         */
        public static boolean getIsUploading() {
            return isUploading;
        }

        /**
         * Sets the upload state of CSVFragment
         * @param uploadingState
         */
        public static void setIsUploading(boolean uploadingState) {
            isUploading = uploadingState;
        }


    }

    private DatabaseReference mDatabase;

    private static final String TAG = "CSVFragment";

    private Button addCSV;

    final String CSV_FILE_NAME = "Rat_Sightings.csv";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csv, container, false);
        addCSV = (Button) view.findViewById(R.id.raddcsv);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        addCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSightingCSV();
            }
        });

        return view;
    }



    /**
     * Creates a RatSighting with the given data and uploads it to the database
     *
     * @param key
     * @param date
     * @param locationType
     * @param zip
     * @param address
     * @param city
     * @param borough
     * @param latitude
     * @param longitude
     */
    private void writeNewSighting(String key,
                                  String date,
                                  String locationType,
                                  String zip,
                                  String address,
                                  String city,
                                  String borough,
                                  double latitude,
                                  double longitude) {
        //-------------------------------------------

        RatSighting sighting = new RatSighting(key, date, locationType, zip, address,
                city, borough, latitude, longitude);
        mDatabase.child("sightings").child(key).setValue(sighting);
        // setPriority not entirely necessary with later versions of Firebase, but it may
        //     be useful in some scenarios
        mDatabase.child("sightings").child(key).setPriority(Integer.parseInt(key));
    }

    /**
     * Writes the data from the CSV file at the specified path and filename to the database
     *
     * User still needs to manually give app permission to read and edit files:
     * https://stackoverflow.com/a/38578137
     */
    private void writeSightingCSV() {
        Log.v(TAG, "writeSightingCSV called");
        CSVFragment.UploadStateNestedClass.setIsUploading(true);

        String csvFileName = new String(CSV_FILE_NAME);
        File dataFolder = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //--------------------------------------------------------------------------
        File csvFile = new File(dataFolder, csvFileName);

        Log.v(TAG, "dataFolder path: " + dataFolder.getPath());

        // Deprecated filepath
        //String csvFile = Environment.getExternalStorageDirectory().getPath().concat("/Android/data/org.krupczak.matthew/Rat_Sightings.csv");

        Log.v(TAG, "does " + csvFileName + " exist?: " + csvFile.exists());
        Log.v(TAG, ".canRead() " + csvFileName + "?: " + csvFile.canRead());
        Log.v(TAG, ".canWrite() " + csvFileName + "?: " + csvFile.canWrite());
        Log.v(TAG, "writeSightingCSV with path: " + csvFile.getPath());

        BufferedReader br = null;
        String line = "";
        String splitBy = ",";

        try {
            FileInputStream fis = new FileInputStream(csvFile);
            Log.v(TAG, "writeSightingCSV FileInputStream instantiated");
            br = new BufferedReader(new InputStreamReader(fis));
            Log.v(TAG, "writeSightingCSV BufferedReader instantiated");
            line = br.readLine();
            String[] sighting = line.split(splitBy);
            int[] fieldIndex = new int[9];
            // This first loop finds the column position of each type of data
            //     present in a rat sighting row
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
            // Deprecated date format conversion, could be useful later
            // SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");

            // This second loop grabs an individual row and uses the data at relevant indices
            //     to make a call to writeNewSighting to write a RatSighting to the database
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
        CSVFragment.UploadStateNestedClass.setIsUploading(false);
    }
}
