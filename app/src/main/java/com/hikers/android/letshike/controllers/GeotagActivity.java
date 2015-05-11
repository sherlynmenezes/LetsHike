package com.hikers.android.letshike.controllers;

import android.media.ExifInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hikers.android.letshike.R;

import java.io.File;
import java.io.IOException;

public class GeotagActivity extends ActionBarActivity {

    private static final String IMAGE_DIRECTORY_NAME = "Lets Hike!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.show);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLatLong();
            }
        });


    }

    private void printLatLong() {
//       File mediaStorageDir = new File(
//               Environment                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//               IMAGE_DIRECTORY_NAME);
//       File sd = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_20150426_171439.jpg");

        File sd = new File("/storage/extSdCard/DCIM/Camera/20150508_181529.jpg");
        try {
            ExifInterface exif = new ExifInterface(sd.toString()); // create the ExifInterface file


            /** get the attribute. rest of the attributes are the same. i will add convertToDegree on the bottom (not required) **/
            float log = convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));

            if (!exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).equals("E")) { // deal with the negative degrees
                log = -log;
            }
            float lat = convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            if (!exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("N")) {
                lat = -lat;
            }
            String latString = Float.toString(log);
            String longStrig = Float.toString(lat);
            Log.v("Latitude", latString);
            Log.v("Longitude", longStrig);
            TextView t1 = (TextView) findViewById(R.id.lat);
            TextView t2 = (TextView) findViewById(R.id.textView2);
            t1.setText("Latitude" + latString);
            t2.setText("Longitude" + longStrig);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Float convertToDegree(String stringDMS) {
        Float result;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}