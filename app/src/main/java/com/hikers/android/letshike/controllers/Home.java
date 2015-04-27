package com.hikers.android.letshike.controllers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.hikers.android.letshike.R;
import com.hikers.android.letshike.SelectedTrail;
import com.hikers.android.letshike.TripHistory;


public class Home extends ActionBarActivity {

    Button historyButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        historyButton=(Button)findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pastTrails(v);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        else if(id == R.id.action_map){
            Log.d("map","insideactionmap");
            Intent intent = new Intent(this,SelectedTrail.class);
            startActivity(intent);
            //openPreferredMapLocation();
        }

        return super.onOptionsItemSelected(item);
    }
    private void openPreferredMapLocation(){
        Log.d("map", "insideMapMethod");
        String posLat="59.915494";
        String posLong="30.409456";
//        String posLat = c.getString(COL_COORD_LAT);
//        String posLong = c.getString(COL_COORD_LONG);
        Uri geoLocation = Uri.parse("geo:" + posLat + "," + posLong);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivity(intent);
        }
    }

    public void pastTrails(View view)
    {
        Intent i = new Intent(this, TripHistory.class);
        startActivity(i);
    }
}
