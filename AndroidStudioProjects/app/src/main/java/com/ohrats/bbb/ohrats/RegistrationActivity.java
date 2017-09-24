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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Keep track of the FireBase attempts
    private static final String TAG = "RegistrationActivity";

    //User information
    private String _level = "NA";

    // UI references.
    private EditText rEmailView;
    private EditText rPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        rEmailView = (EditText) findViewById(R.id.remail);
        rPasswordView = (EditText) findViewById(R.id.rpassword);

        //Grab the user or admin dropdown
        Spinner spinner = (Spinner) findViewById(R.id.level_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.level_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Registration Button
        // Cancels the login attempt Matthew K.
        Button rRegisterButton = (Button) findViewById(R.id.register_button);
        rRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear login attempt method call here
                register();
            }
        });

        //Firebase initialization
        mAuth = FirebaseAuth.getInstance();

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

    private void register() {
        // Reset errors.
        rEmailView.setError(null);
        rPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = rEmailView.getText().toString();
        String password = rPasswordView.getText().toString();

        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Navigate Back to the sign-in page
                            Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(in);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

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
