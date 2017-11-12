package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Keep a log for debugging
    private static final String TAG = "LoginActivity";


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    //private View mProgressView;
    //private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //Button for signing in
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //Button for registration
        Button mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        // Cancels the login attempt Matthew K.
        Button mLoginCancelButton = (Button) findViewById(R.id.login_cancel_button);
        mLoginCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear login attempt method call here
                clearLoginAttempt();
            }
        });

        //mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);

        //Firebase initialization and lister creation
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


    }

    /**
     * Attaches the listener to mAuth for FireBase
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Removes the listener from mAuth
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    /**
     * Ensures that the email and password are not empty and are formatted correctly
     * @return boolean true if formatted correctly
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Required.");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        String password = mPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Required.");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        //Check to ensure the user has input valid information
        if (!validateForm()) {
            return;
        }

        //attempt to sign in the users
        signIn(email, password);
    }

    /**
     * This method sends users to the registration activity
     */
    private void attemptRegistration() {
        //this should navigate to the new registration screen
        //and have more firebase
        Intent in = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(in);

    }

    /**
     * Clears the email and password fields
     */
    private void clearLoginAttempt() {
       mEmailView.setText("");
       mPasswordView.setText("");
    }

    /**
     * Takes in an email and password and authenticates them with Firebase authentication
     * If valid, the user is logged-in and taken to the main application screen
     * @param email The users email
     * @param password The users password
     * REQUIRED by Firebase
     */
    @SuppressWarnings("unused")
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // Calls upon the Firebase Gods to login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @SuppressWarnings("unused")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Navigate to the main application
                            Intent in = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(in);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            mPasswordView.setError(getString(R.string.error_invalid_login));
                        }
                    }
                });
    }

    /**
     * Signs a user out from Firebase
     * REQUIRED by Firebase
     */
    @SuppressWarnings("unused")
    private void signOut() {
        mAuth.signOut();
    }

    /**
     * Disables back button
     */
    @Override
    public void onBackPressed(){
        //
    }
}

