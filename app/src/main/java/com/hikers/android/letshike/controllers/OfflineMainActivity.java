package com.hikers.android.letshike.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hikers.android.letshike.R;
import com.hikers.android.letshike.data.Coordinates;
import com.hikers.android.letshike.data.Coordinates_Database;
import com.parse.Parse;
import com.parse.ParseObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OfflineMainActivity extends FragmentActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,SensorEventListener {

    //Pedometer Fields
    private double sum = 0;
    private int steps;
    private int counter=0;
    private float distance;
    private double calories;
    Time startTime;
    Time endTime;
    Time duration;
    private TextView textView;
    private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;
    //****

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    // private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Lets Hike!";

    private Uri fileUri; // file url to store image/video

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 5; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 5; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    PedometerSensors pedo = new PedometerSensors();
    ParseObject test1Object=new ParseObject("hell");
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "Hw4MULqo65R0NHElRKs8ZMIEJjo8jHx8jUE3U31a", "zEMPRCWyMtGVhk477CKHz71rd2DtNlBTYciIiIXt");
//
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("latitude", 787.545);
        sum=0;
        setContentView(R.layout.activity_main_offline_gps);
        textView = (TextView) findViewById(R.id.steps_textview);
        Button bStop = (Button)findViewById(R.id.hike_stats);
        ParseObject tObject = new ParseObject("coordinates");
        tObject.put("sdsfds",454);
        startTime = new Time(Time.getCurrentTimezone());
        startTime.setToNow();

        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStats(v);
            }
        });
        Button photo = (Button)findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineMainActivity.this, PhotoSelectionActivity.class);
                startActivity(intent);
            }
        });


//
//        Button pedometer = (Button) findViewById(R.id.pedometer);
//        pedometer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //call_pedometer();
//                    Intent service = new Intent(getApplicationContext(), PedometerSensors.class);
//                startService(service);
//                 }
//            });

        Button camera = (Button) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


   //     Button hike_stats = (Button) findViewById(R.id.hike_stats);

        Log.d(TAG, "onCreate ...............................");
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            textView.setText("Step Counter Detected : " + counter++);
        }
//        else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//            // For test only. Only allowed value is 1.0 i.e. for step taken
//            textView.setText("Step Detector Detected : " + counter);
//        }
        steps = counter;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void startStats(View view) {

//        }
        View v = view.getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        String extr = Environment.getExternalStorageDirectory().toString();
        File myPath = new File(extr, "hike.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage( getContentResolver(), b,
                    "Screen", "screen");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int weight = 175;
        int stepForAMile = 2200;
        double caloriesPerMile, conversionFactor;
        caloriesPerMile = 0.57 * (double)weight;
        conversionFactor=caloriesPerMile/stepForAMile;
        calories = steps * conversionFactor;

        endTime = new Time(Time.getCurrentTimezone());
        endTime.setToNow();

        duration=new Time();
        // duration=startTime-endTime;


        Intent intent = new Intent(this, Hike_Stats.class);
        intent.putExtra("steps", steps+"");
        intent.putExtra("calories", calories+"");
        intent.putExtra("startTime",startTime.format("%k:%M:%S"));
        intent.putExtra("endTime",endTime.format("%k:%M:%S"));
        intent.putExtra("distance",sum+"");
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        Intent intent = new Intent(OfflineMainActivity.this,PedometerSensors.class);
        startService(intent);
        mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        mSensorManager.unregisterListener((SensorEventListener) this, mStepCounterSensor);
        mSensorManager.unregisterListener((SensorEventListener) this, mStepDetectorSensor);

    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Coordinates_Database cd = new Coordinates_Database(this);
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;

        //Change
        Log.v("**** LATITUDE ****", Double.toString(mCurrentLocation.getLatitude()));
        Log.v("**** LONGITUDE ****", Double.toString(mCurrentLocation.getLongitude()));
        Coordinates coord_latLong = new Coordinates(null,mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        cd.insert_coord(coord_latLong);
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        addMarker();
    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Gors.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info



        Coordinates_Database cd=new Coordinates_Database(this);
        ArrayList<Coordinates> coordinates=new ArrayList<>(cd.Coords());

        int count =1 , countline=1;
        double distance=0;
        Location locB=null,locA=null;
        for(Coordinates c : coordinates)
        {
            LatLng currentLatLng = new LatLng(c.get_latitude(), c.get_longitude());
            locA = new Location("LocA");
            locA.setLatitude(c.get_latitude());
            locA.setLongitude(c.get_longitude());

            test1Object.put("Coord_id", c.get_id());
            test1Object.put("latitude", c.get_latitude());
            test1Object.put("longitude", c.get_longitude());
            test1Object.saveInBackground();

            if(locB!=null)
                distance = locA.distanceTo(locB);
            sum=sum+distance;
           // count++;
            Log.v("long", ""+c.get_latitude()+c.get_longitude());
            options.position(currentLatLng);
            Marker mapMarker = googleMap.addMarker(options);
            mapMarker.setTitle(Double.toString(c.get_latitude()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 7));
            Polyline line = googleMap.addPolyline(new PolylineOptions().add(new LatLng(c.get_latitude(),c.get_longitude())));
            locB = new Location("LocB");
            locB=locA;
        }

        for(int i=0;i<coordinates.size()-1;i++)
        {
            if(countline<coordinates.size())
            { Polyline line = googleMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(coordinates.get(i).get_latitude(), coordinates.get(i).get_longitude())
                    ,new LatLng(coordinates.get(countline).get_latitude(),coordinates.get(countline).get_longitude()))
            .width(5)
            .color(Color.RED));
                countline++;
            }

        }

        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
//        mapMarker.setTitle(Double.toString(mCurrentLocation.getLatitude()));
        Log.d(TAG, "Marker added.............................");

        Log.d(TAG, "Zoom done.............................");
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }


        mSensorManager.registerListener((SensorEventListener) this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener((SensorEventListener) this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

//        mSensorManager.registerListener(this, mStepCounterSensor,SensorManager.SENSOR_DELAY_FASTEST);
//        mSensorManager.registerListener(this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }


    //camera part

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

    //Pedometer

//    public void startStats(View view){
////        File mediaStorageDir = new File(
////                Environment
////                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
////                IMAGE_DIRECTORY_NAME);
////        OutputStream fout = null;
////
////        bitmap = screenShot(v);
////        try {
////            fout = new FileOutputStream(mediaStorageDir);
////            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
////            fout.flush();
////            fout.close();
////        }catch (FileNotFoundException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//        View v = view.getRootView();
//        v.setDrawingCacheEnabled(true);
//        Bitmap b = v.getDrawingCache();
//        String extr = Environment.getExternalStorageDirectory().toString();
//        File myPath = new File(extr, "hike.jpg");
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(myPath);
//            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            MediaStore.Images.Media.insertImage( getContentResolver(), b,
//                    "Screen", "screen");
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        int weight = 175;
//        int stepForAMile = 2200;
//        double caloriesPerMile, conversionFactor;
//        caloriesPerMile = 0.57 * (double)weight;
//        conversionFactor=caloriesPerMile/stepForAMile;
//        calories = steps * conversionFactor;
//
//        endTime = new Time(Time.getCurrentTimezone());
//        endTime.setToNow();
//
//        duration=new Time();
//        // duration=startTime-endTime;
//
//
//        Intent intent = new Intent(this, Hike_Stats.class);
//        intent.putExtra("steps", steps+"");
//        intent.putExtra("calories", calories+"");
//        intent.putExtra("startTime",startTime.format("%k:%M:%S"));
//        intent.putExtra("endTime",endTime.format("%k:%M:%S"));
//        startActivity(intent);
//    }
//
//    @Override
//    public void orChanged(SensorEvent event) {
//        Sensor sensor = event.sensor;
//        float[] values = event.values;
//        int value = -1;
//
//        if (values.length > 0) {
//            value = (int) values[0];
//        }
//
//        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            textView.setText("Step Counter Detected : " + value);
//        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//            // For test only. Only allowed value is 1.0 i.e. for step taken
//            textView.setText("Step Detector Detected : " + value);
//        }
//        steps = value;
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }

//    protected void onResume() {
//
//        super.onResume();
//
//        mSensorManager.registerListener(this, mStepCounterSensor,
//
//                SensorManager.SENSOR_DELAY_FASTEST);
//        mSensorManager.registerListener(this, mStepDetectorSensor,
//
//                SensorManager.SENSOR_DELAY_FASTEST);
//
//    }


//    public void sensros()
//    {
//
//        startTime = new Time(Time.getCurrentTimezone());
//        startTime.setToNow();
//
//        mSensorManager = (SensorManager)
//                getSystemService(Context.SENSOR_SERVICE);
//        mStepCounterSensor = mSensorManager
//                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        mStepDetectorSensor = mSensorManager
//                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroy","destroyed");
        //      Coordinates_Database.onUpgrade
    }

}