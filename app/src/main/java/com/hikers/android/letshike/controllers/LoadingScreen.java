package com.hikers.android.letshike.controllers;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hikers.android.letshike.R;

public class LoadingScreen extends ActionBarActivity {
    private final int WAIT_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate(savedInstanceState);
        System.out.println("LoadingScreenActivity  screen started");
        setContentView(R.layout.activity_loading_screen);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Simulating a long running task
                System.out.println("Going to Profile Data");
	  /* Create an Intent that will start the ProfileData-Activity. */
                Intent mainIntent = new Intent(LoadingScreen.this, Home.class);
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LoadingScreen.this.startActivity(mainIntent);
                LoadingScreen.this.finish();
            }
        }, WAIT_TIME);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loading_screen, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
