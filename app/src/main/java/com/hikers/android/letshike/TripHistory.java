package com.hikers.android.letshike;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by Neha on 4/6/2015.
 */
public class TripHistory extends ActionBarActivity {

    public void newTrails(View view) {
        Intent i = new Intent(this, Home.class);
        startActivityForResult(i, 1);
    }

}