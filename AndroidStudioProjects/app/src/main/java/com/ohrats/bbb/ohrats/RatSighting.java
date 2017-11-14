package com.ohrats.bbb.ohrats;

import java.io.Serializable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Class which stores rat sighting info and is used with Firebase
 * POJOs can be easily added to the database
 *
 * Created by Matt on 10/3/2017.
 */

@IgnoreExtraProperties
class RatSighting implements Serializable {

    private String key;                // unique key
    private String date;              // created date
    private String locationType;    // location type
    private String zip;                // incident zip
    private String address;         // incident address
    private String city;            // city
    private String borough;         // borough
    private double latitude;        // latitude
    private double longitude;       // longitude

    // getters and setters

    /**
     * gets sighting key
     * @return unique sighting key
     */
    public String getKey() {
        return key;
    }

    /**
     * sets sighting key
     * @param key unique key
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * gets sighting date
     * @return sighting date
     */
    public String getDate() {
        return date;
    }

    /**
     * sets sighting date
     * @param date new date to set for sighting
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * gets sighting location type
     * @return sighting location type
     */
    //Firebase expects a string
    @SuppressWarnings("TypeMayBeWeakened")
    public String getLocationType() {
        return locationType;
    }

    /**
     * sets sighting location type
     * @param locationType new location type set for sighting
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * gets sighting zip
     * @return sighting zip
     */
    //Firebase expects a string
    @SuppressWarnings("TypeMayBeWeakened")
    public String getZip() {
        return zip;
    }

    /**
     * sets sighting zip
     * @param zip new zip for sighting
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * gets sighting address
     * @return sighting address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets sighting address
     * @param address sighting new address
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * gets sighting city
     * @return sighting city
     */
    //Firebase expects a string
    @SuppressWarnings("TypeMayBeWeakened")
    public String getCity() {
        return city;
    }

    /**
     * sets sighting city
     * @param city sighting new set city
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * gets sighting borough
     * @return sighting borough
     */
    //Firebase expects a string
    @SuppressWarnings("TypeMayBeWeakened")
    public String getBorough() {
        return borough;
    }

    /**
     * sets sighting borough
     * @param borough sighting new set borough
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setBorough(String borough) {
        this.borough = borough;
    }

    /**
     * gets sighting latitude
     * @return sighting latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * sets sighting latitude
     * @param latitude sighting new latitude
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * gets sighting longitude
     * @return sighting longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * sets sighting longitude
     * @param longitude sighting new longitude
     */
    //suppressed because used in Firebase and POJOs
    @SuppressWarnings("unused")
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("%10s | %10.10s | %.20s",
                (!("").equals(getKey())) ? getKey() : "N/A",
                                        (!("").equals(getDate())) ? getDate() : "N/A",
                                        (!("").equals(getAddress())) ? getAddress() : "N/A");
    }

    /**
     * constructor for a new ratSighting
     */
    //suppressed because used in Firebase
    @SuppressWarnings("unused")
    public RatSighting() {
        // From Google People @ Firebase
        // "Default constructor required for calls to DataSnapshot.getValue(User.class)"
    }

    /**
     * constructor chaining for ratSighting given only key
     * @param key key for rat sighting
     */
    //suppressed because used in Firebase
    @SuppressWarnings("unused")
    public RatSighting(String key) {
        this.key = key;
    }

    /**
     * Creates a rat sighting with a unique key, created date, location type,
     * incident zip, incident address, city, borough, latitude, and longitude
     * @param key unique key
     * @param date unique date
     * @param locationType location of sighting
     * @param zip zip of sighting
     * @param address address of sighting
     * @param city city of sighting
     * @param borough borough of sighting
     * @param latitude latitude of sighting
     * @param longitude longitude of sighting
     */
    //suppressed because params are all key components of a complete sighting
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public RatSighting(String key, String date, String locationType, String zip, String address,
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
