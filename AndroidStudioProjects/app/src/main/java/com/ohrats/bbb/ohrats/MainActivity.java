package com.ohrats.bbb.ohrats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mTextMessage;

    //Keep track of the FireBase attempts
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Firebase initialization and listener creation
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

        Button mViewReportListButton = (Button) findViewById(R.id.view_report_list);
        mViewReportListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewListMapComboPage();
            }
        });

        Button mAddRatSightingButton = (Button) findViewById(R.id.add_rat_sighting);
        mAddRatSightingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRatSightingPage();
            }
        });

        Button mChartsButton = (Button) findViewById(R.id.historical_charts);
        mChartsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChartsActivity();
            }
        });
    }

    private void toChartsActivity() {
        Intent inView = new Intent(MainActivity.this, ChartsActivity.class);
        Log.d(TAG, "New intent to ChartsActivity");
        startActivity(inView);
    }

    private void viewListMapComboPage() {
        Intent inView = new Intent(MainActivity.this, ListMapComboActivity.class);
        startActivity(inView);
    }

    /**
     * Switches to AddSightingActivity
      */
    private void addRatSightingPage() {
        Intent inView = new Intent(MainActivity.this, AddSightingActivity.class);
        startActivity(inView);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                signOut();

                //navigates back to the logn screen
                Intent in = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(in);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Signs a user out with firebase
     */
    private void signOut() {
        mAuth.signOut();
    }

}
