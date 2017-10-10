package com.ohrats.bbb.ohrats;

import java.io.Serializable;
import java.util.Date;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Class storing rat sighting info in Firebase
 * POJOs can be easily added to the database
 * Created by Matt on 10/3/2017.
 */

@IgnoreExtraProperties
public class RatSighting implements Serializable {

    private String key;                // unique key
    private Date date;              // created date
    private String locationType;    // location type
    private String zip;                // incident zip
    private String address;         // incident address
    private String city;            // city
    private String borough;         // borough
    private double latitude;        // latitude
    private double longitude;       // longitude

    // getters and setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        String sighting = String.format("%s | %s | %s | %s", (getKey() != null) ? getKey() : "N/A",
                                        (getDate() != null) ? getDate() : "N/A",
                                        (getLocationType() != null) ? getLocationType() : "N/A",
                                        (getAddress() != null) ? getAddress() : "N/A");
        return String.format("%.50s", sighting);
    }

    public RatSighting() {
        // From Google People @ Firebase
        // "Default constructor required for calls to DataSnapshot.getValue(User.class)"
    }

    public RatSighting(String key) {
        this.key = key;
    }

    /**
     * Creates a rat sighting with a unique key, created date, location type,
     * incident zip, incident address, city, borough, latitude, and longitude
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
    public RatSighting(String key, Date date, String locationType, String zip, String address,
                       String city, String borough, double latitude, double longitude) {
        this.key = key;
        this.date = date;
        this.locationType = locationType;
        this.zip = zip;
        this.address = address;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
