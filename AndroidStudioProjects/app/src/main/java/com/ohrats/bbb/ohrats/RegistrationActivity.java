package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //FireBase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Firebase Realtime DataBase
    private DatabaseReference mDatabase;


    //Keep track of the FireBase attempts
    private static final String TAG = "RegistrationActivity";

    //User information
    private String _level = "User";

    // UI references.
    private EditText rEmailView;
    private EditText rPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner levelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Grab the elements from the UI
        rEmailView = (EditText) findViewById(R.id.remail);
        rPasswordView = (EditText) findViewById(R.id.rpassword);
        levelSpinner = (Spinner) findViewById(R.id.level_spinner);

        //Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level_array, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        levelSpinner.setAdapter(adapter);

        //Registration Button
        Button rRegisterButton = (Button) findViewById(R.id.register_button);
        rRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        //Firebase initialization
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

        //initialize database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // database event listener
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "User is: " + user);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                mDatabase.removeEventListener(this);
            }
        });
    }

    // User or Admin spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        _level = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        _level = "NA";
    }

    /*
    Firebase RealTime Database Setup Below
     */

    /**
     * Creates a new user and adds that user to the database
     * @param email The user's email
     * @param password The user's password
     * @param level Whether user is "User" or "Admin"
     * @param userId Their firebase id
     */
    private void writeNewUser(String email, String password, String level, String userId) {
        User user = new User(email, password, level);
        //Add the POJO to the database
        mDatabase.child("users").child(userId).setValue(user);
        Log.d(TAG, "writeNewUser:success");
    }

    /**
     * This method validates the users entries on the register screen then calls create account
     * Upon creating an account, the users Uid is procured and that info is used to writeNewUser
     * This user object is added to the database. This object stores whether users is User or Admin
     */
    private void register() {
        // Reset errors.
        rEmailView.setError(null);
        rPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = rEmailView.getText().toString();
        String password = rPasswordView.getText().toString();
        String level = (String) levelSpinner.getSelectedItem();

        if (!validateForm()) {
            return;
        }

        createAccount(email, password);


        FirebaseUser user = mAuth.getCurrentUser();

        //getUid may throw a NullPointerException
        try {
            String uid = user.getUid();
            Log.d(TAG, "attempting to access Uid");
            writeNewUser(email, password, level, uid);
        } catch (NullPointerException npe) {
            npe.getMessage();
            npe.getCause();
        }

        //Navigate Back to the sign-in page
        Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(in);
    }

    /**
     * Creates an account with firebase authentication
     * @param email The email from the input line
     * @param password The password from the input line
     */
    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    /**
     * Ensures that the email and password are not empty and are formatted correctly
     * @return boolean true if formatted correctly
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = rEmailView.getText().toString();
        if (TextUtils.isEmpty(email)) {
            rEmailView.setError("Required.");
            valid = false;
        } else {
            rEmailView.setError(null);
        }

        String password = rPasswordView.getText().toString();
        if (TextUtils.isEmpty(password)) {
            rPasswordView.setError("Required.");
            valid = false;
        } else {
            rPasswordView.setError(null);
        }
        return valid;
    }
}
