package com.hikers.android.letshike.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hikers.android.letshike.R;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Home extends ActionBarActivity implements Camera.PictureCallback{
    private static final String TAG = Home.class.getSimpleName();
    public ParseFile photoFile;
    ListView trail_list;
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    // private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Lets Hike!";

    private Uri fileUri; // file url to store image/video

    ArrayAdapter<String> adapter = null;

    Button historyButton ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        trail_list=(ListView)findViewById(R.id.listView2);
        ArrayList<String> trails=new ArrayList<String>();
        ArrayList<String> mTripTitles=new ArrayList<>();
        mTripTitles.add("NYU Poly");
        mTripTitles.add("Bayridge");
        mTripTitles.add("Queens");
        mTripTitles.add("Manhattan");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTripTitles);
        trail_list.setAdapter(adapter);
        trail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             Intent intent = new Intent(Home.this,Trail_Details.class);
                startActivity(intent);

            }
        });

        historyButton=(Button)findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pastTrails(v);
            }
        });
//        ParseAnalytics.trackAppOpened(getIntent());
        Log.d(TAG,"MAIn");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Log.i(TAG,currentUser.getUsername());
        } else {
            // show the signup or login screen
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if(id == R.id.action_map){
            Log.d("map","insideactionmap");
            Intent intent = new Intent(this,OfflineMainActivity.class);
            startActivity(intent);
            //openPreferredMapLocation();
        }
        else if (id == R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(id == R.id.action_camera)
        {
            Intent intent = new Intent(this,CameraActivity.class);
            startActivity(intent);
            //captureImage();
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

    //******Camera part

    private void captureImage() {




        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */

    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
//        } else if (type == MEDIA_TYPE_VIDEO) {
//            mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                    + "VID_" + timeStamp + ".mp4");
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = (Location) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            geoTag(mediaFile.getAbsolutePath(),latitude,longitude);
        }
        else {
            return null;
        }

        return mediaFile;
    }
    public void geoTag(String filename, double latitude, double longitude){
        ExifInterface exif;

        try {
            exif = new ExifInterface(filename);
            int num1Lat = (int)Math.floor(latitude);
            int num2Lat = (int)Math.floor((latitude - num1Lat) * 60);
            double num3Lat = (latitude - ((double)num1Lat+((double)num2Lat/60))) * 3600000;

            int num1Lon = (int)Math.floor(longitude);
            int num2Lon = (int)Math.floor((longitude - num1Lon) * 60);
            double num3Lon = (longitude - ((double)num1Lon+((double)num2Lon/60))) * 3600000;

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat+"/1,"+num2Lat+"/1,"+num3Lat+"/1000");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon+"/1,"+num2Lon+"/1,"+num3Lon+"/1000");


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }

            exif.saveAttributes();
        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }

    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view

                // previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
//            }
        }
    }
    private void saveScaledPhoto(byte[] data) {

        // Resize photo from camera byte array
        Bitmap mealImage = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mealImageScaled = Bitmap.createScaledBitmap(mealImage, 200, 200
                * mealImage.getHeight() / mealImage.getWidth(), false);

        // Override Android default landscape orientation and save portrait
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedScaledMealImage = Bitmap.createBitmap(mealImageScaled, 0,
                0, mealImageScaled.getWidth(), mealImageScaled.getHeight(),
                matrix, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedScaledMealImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        byte[] scaledData = bos.toByteArray();

        // Save the scaled image to Parse
        photoFile = new ParseFile("meal_photo.jpg", scaledData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(),
                            "Error saving: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    addPhotoToMealAndReturn(photoFile);
                }
            }
        });
    }

    private void addPhotoToMealAndReturn(ParseFile photoFile) {
        Trip trip = new Trip();
        trip.setPhotoFile(photoFile);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d("photo","inside onPicture");
        saveScaledPhoto(data);
    }
}
