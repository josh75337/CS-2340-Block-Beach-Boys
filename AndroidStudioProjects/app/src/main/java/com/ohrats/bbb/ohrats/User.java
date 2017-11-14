package com.ohrats.bbb.ohrats;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * A class to store user information in the Firebase realtime database
 * POJOs can easily be added to the database
 * Created by Elijah on 9/25/2017.
 */
@SuppressWarnings("unused") //Getters and setters not used in the codebase but required by Firebase
@IgnoreExtraProperties
class User {

    private String email;
    private String password;

    //Level -> User or Admin
    private String level;

    /* Getters and setters required by firebase database */

    /**
     * Getter for the user's email
     * REQUIRED by Firebase
     * @return String containing the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the users email
     * REQUIRED by Firebase
     * @param email The new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the user's password
     * REQUIRED by Firebase
     * @return A string containing the users password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for password
     * REQUIRED by Firebase
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for the users level (User or Admin)
     * REQUIRED by Firebase
     * @return A string of the users level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Setter for level
     * REQUIRED by Firebase
     * @param level the new level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Constructor
     * REQUIRED by Firebase
     */
    public User() {
        // From Google People @ Firebase
        // "Default constructor required for calls to DataSnapshot.getValue(User.class)"
    }

    /**
     * Constructor with email and password
     * @param email The user's email
     * @param password The user's password
     */
    public User(String email, String password) {
        this(email, password, "User");
    }

    /**
     * Creates a user with an email, password, and level.
     * @param email The users email
     * @param password The password the user chose to using in app
     * @param level Their "clearance" either user or admin
     */
    public User(String email, String password, String level) {
        this.email = email;
        this.password = password;
        this.level = level;
    }

}

