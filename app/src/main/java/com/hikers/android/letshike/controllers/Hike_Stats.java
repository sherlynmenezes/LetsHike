package com.hikers.android.letshike.controllers;

/**
 * Created by Neha on 5/9/2015.
 */import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.hikers.android.letshike.R;


public class Hike_Stats extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hike_stats);
        Intent intent = getIntent();

        Bundle b = getIntent().getExtras();
        String distance = b.getString("distance");
        String step = b.getString("steps");
        String calories = b.getString("calories");
        String startTime = b.getString("startTime");
        String endTime = b.getString("endTime");
        TextView dist = (TextView)findViewById(R.id.distanceVal);
        dist.setText(distance);
        TextView stepVal = (TextView)findViewById(R.id.stepsVal);
        stepVal.setText(step);
        TextView calVal = (TextView)findViewById(R.id.caloriesVal);
        calVal.setText(calories);
        TextView starttimeVal =(TextView)findViewById(R.id.starttimeVal);
        starttimeVal.setText(startTime);
        TextView endtimeVal =(TextView)findViewById(R.id.endtimeVal);
        endtimeVal.setText(endTime);


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu., menu);
//        return true;
//    }

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
