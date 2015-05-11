package com.hikers.android.letshike.controllers;

import com.hikers.android.letshike.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

//import com.hikers.android.letshike.controllers.SelectedTrail;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {
    private Button mSignUpButton;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    public static final String TAG = MainActivity.class.getSimpleName();
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Trip.class);
        Parse.initialize(this, "Hw4MULqo65R0NHElRKs8ZMIEJjo8jHx8jUE3U31a", "zEMPRCWyMtGVhk477CKHz71rd2DtNlBTYciIiIXt");
//        ParseUser.enableRevocableSessionInBackground();
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.put("longitude", 72.00033);

//        testObject.saveInBackground();

        mSignUpButton = (Button)findViewById(R.id.SignUpButton);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        mUsername = (EditText)findViewById(R.id.userName);
        mPassword = (EditText)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        if (saveLogin == true) {
            mUsername.setText(loginPreferences.getString("username", ""));
            mPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        ParseAnalytics.trackAppOpened(getIntent());
        Log.d(TAG,"MAIn");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Log.i(TAG,currentUser.getUsername());
            Intent intent = new Intent(this,Home.class);
            startActivity(intent);
        }
        //else {
//            // show the signup or login screen
//            Intent intent = new Intent(this,MainActivity.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = mUsername.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.errorSignInMessage));
                    builder.setTitle(getString(R.string.errorSignInTitle));
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {

                    setSupportProgressBarIndeterminateVisibility(true);

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);
                            Log.d(TAG, "mLoginButton Clicked" + user);
                            if (user != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mUsername.getWindowToken(), 0);


                                if (saveLoginCheckBox.isChecked()) {
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("username", username);
                                    loginPrefsEditor.putString("password", password);
                                    loginPrefsEditor.commit();
                                } else {
                                    loginPrefsEditor.clear();
                                    loginPrefsEditor.commit();
                                }


                                // Hooray! The user is logged in.
                                ParseAnalytics.trackAppOpened(getIntent());
                                Log.d(TAG, "user is not null");
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                // Signup failed. Look at the ParseException to see what happened.


                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                builder.setMessage(e.getMessage());
                                builder.setTitle("Error in SignIn!");
                                builder.setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id==R.id.action_map){
            Intent intent = new Intent(this,OfflineMainActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

}




