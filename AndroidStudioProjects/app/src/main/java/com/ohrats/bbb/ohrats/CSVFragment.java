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
import java.io.InputStreamReader;

/**
 * fragment that handles uploading CSVs
 * Created by Matt on 10/6/2017.
 */

public class CSVFragment extends Fragment {

    private DatabaseReference mDatabase;

    private static final String TAG = "CSVFragment";

    // easier to locate what buttons the activity has if not local
    @SuppressWarnings("FieldCanBeLocal")
    private Button addCSV;

    private int count;

    // helps locate and change file name if needed for debugging
    @SuppressWarnings("FieldCanBeLocal")
    private final String CSV_FILE_NAME = "Rat_Sightings.csv";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_csv, container, false);
        addCSV = (Button) view.findViewById(R.id.raddcsv);

        //necessary for firebase
        //noinspection ChainedMethodCall
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



    /**
     * Creates a RatSighting with the given data and uploads it to the database
     *
     * @param key           : Unique Key
     * @param date          : Created Date
     * @param locationType  : Location Type
     * @param zip           : Incident Zip
     * @param address       : Incident Address
     * @param city          : City
     * @param borough       : Boroughs
     * @param latitude      : Latitude
     * @param longitude     : Longitude
     */
    // necessary because rat sighting plain old java object has that many parameters 
    @SuppressWarnings("MethodWithTooManyParameters")
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
        // necessary for firebase
        //noinspection ChainedMethodCall,ChainedMethodCall
        mDatabase.child("sightings").child(key).setValue(sighting);
        count++;
        Log.d(TAG, "Count is " + count);
        // setPriority not entirely necessary with later versions of Firebase, but it may
        //     be useful in some scenarios
        // removed currently; causes lagging when uploading csv
        //mDatabase.child("sightings").child(key).setPriority(Integer.parseInt(key));
    }

    /**
     * Writes the data from the CSV file at the specified path and filename to the database
     *
     * User still needs to manually give app permission to read and edit files:
     * https://stackoverflow.com/a/38578137
     *
     */
    // method considered too complex to analyze but does not contain any bugs
    // switch statement inside for loop to find indices of fields followed by while to parse csv
    // code is long because it parses the csv and there a decent number of fields
    @SuppressWarnings({"ConstantConditions", "OverlyComplexMethod", "OverlyLongMethod"})
    private void writeSightingCSV() {
        Log.v(TAG, "writeSightingCSV called");

        String csvFileName = CSV_FILE_NAME;
        File dataFolder = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //--------------------------------------------------------------------------
        File csvFile = new File(dataFolder, csvFileName);

        Log.v(TAG, "dataFolder path: " + dataFolder.getPath());

        Log.v(TAG, "does " + csvFileName + " exist?: " + csvFile.exists());
        Log.v(TAG, ".canRead() " + csvFileName + "?: " + csvFile.canRead());
        Log.v(TAG, ".canWrite() " + csvFileName + "?: " + csvFile.canWrite());
        Log.v(TAG, "writeSightingCSV with path: " + csvFile.getPath());

        BufferedReader br;
        String line;
        String splitBy = ",";
        FileInputStream fis;
        try {
            fis = new FileInputStream(csvFile);
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

//            SimpleDateFormat americanGarbageDF = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
//            String americanGarbageString;

            // This second loop grabs an individual row and uses the data at relevant indices
            //     to make a call to writeNewSighting to write a RatSighting to the database
            // necessary to check whether there exists a next line while getting the line if exists
            //noinspection NestedAssignment
            while ((line = br.readLine()) != null) {
                sighting = line.split(splitBy);
                int sightingLength = sighting.length;
                String key = (fieldIndex[0] < sightingLength) ? sighting[fieldIndex[0]] : null;
                String date = (fieldIndex[1] < sightingLength) ?
                        DateStandardsBuddy
                                .garbageAmericanStringToISO8601ESTString(sighting[fieldIndex[1]]):
                                                                                            null;
                String locationType =
                        (fieldIndex[2] < sightingLength) ? sighting[fieldIndex[2]] : null;
                String zip = (fieldIndex[3] < sightingLength) ? sighting[fieldIndex[3]] : null;
                String address = (fieldIndex[4] < sightingLength) ? sighting[fieldIndex[4]] : null;
                String city = (fieldIndex[5] < sightingLength) ? sighting[fieldIndex[5]] : null;
                String borough = (fieldIndex[6] < sightingLength) ? sighting[fieldIndex[6]] : null;
                double latitude =
                        (fieldIndex[7] < sightingLength) ?
                                Double.parseDouble(sighting[fieldIndex[7]]) : 0;
                double longitude =
                        (fieldIndex[8] < sightingLength) ?
                                Double.parseDouble(sighting[fieldIndex[8]]) : 0;
                writeNewSighting(key, date, locationType, zip,address,
                        city, borough, latitude, longitude);
            }
            // close the FileInputStream since we're now done with it
            fis.close();
        } catch (Exception e) {
            e.getMessage();
            e.getCause();
        }
    }
}
