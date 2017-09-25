package com.ohrats.bbb.ohrats;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * A class to store user information in the Firebase realtime database
 * POJOs can easily be added!
 * Created by Elijah on 9/25/2017.
 */
@IgnoreExtraProperties
public class User {

    private String email;
    private String password;

    //Level -> User or Admin
    private String level;

    //Either getters and setters or public variables required by firebase database
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public User() {
        // From Google People @ Firebase
        // "Default constructor required for calls to DataSnapshot.getValue(User.class)"
    }

    public User(String email, String password) {
        this(email, password, "User");
    }

    public User(String email, String password, String level) {
        this.email = email;
        this.password = password;
        this.level = level;
    }

}

