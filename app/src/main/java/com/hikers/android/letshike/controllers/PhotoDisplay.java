package com.hikers.android.letshike.controllers;

import android.graphics.Color;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.hikers.android.letshike.data.Photos;
import com.hikers.android.letshike.data.Photos_database;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Neha on 5/11/2015.
 */
/**
 * Created by sherlyn on 5/10/2015.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hikers.android.letshike.R;
import com.hikers.android.letshike.data.Coordinates;
import com.hikers.android.letshike.data.Coordinates_Database;
import com.hikers.android.letshike.data.Photos;
import com.hikers.android.letshike.data.Photos_database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sherlyn on 5/10/2015.
 */
public class PhotoDisplay extends FragmentActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "PhotoActivity";
    private static final long INTERVAL = 1000 * 60 * 5; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 5; // 1 minute
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroy","destroyed");
        //      Coordinates_Database.onUpgrade
    }

    Location mCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_photo_layout);
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        googleMap = fm.getMap();
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
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
        //  Coordinates_Database cd = new Coordinates_Database(this);
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;

        //Change
        Log.v("**** LATITUDE ****", Double.toString(mCurrentLocation.getLatitude()));
        Log.v("**** LONGITUDE ****", Double.toString(mCurrentLocation.getLongitude()));

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        addMarker();
    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Gors.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info

        Photos_database cd=new Photos_database(this);
        ArrayList<Photos> photo_cord=new ArrayList<>(cd.photo_Coords());
        //     coordinates.addAll(cd.Coords());
        int count = 1 , countline = 1;
        for(Photos c : photo_cord)
        {
            LatLng currentLatLng = new LatLng(c.get_latitude(), c.get_longitude());
            // count++;
            Log.v("long photo", "" + c.get_latitude() + c.get_longitude());
            options.position(currentLatLng);
            Marker mapMarker = googleMap.addMarker(options);
            // byte[] image = c.get_Image();
            //  ByteArrayInputStream imagestream =  new ByteArrayInputStream(image);

            //Bitmap theimage = BitmapFactory.decodeStream(imagestream);
//
//
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = 2;
            Bitmap theimage  = BitmapFactory.decodeFile(c.get_path(),op);
            Bitmap bhalfsize=Bitmap.createScaledBitmap(theimage, theimage.getWidth()/10,theimage.getHeight()/10, false);
            // mapMarker.setIcon(BitmapFactory.decodeByteArray(c.get_Image(),0,c.get_Image().length));

            //     Drawable drawable = Drawable.createFromPath(c.get_path());
            mapMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bhalfsize));
            mapMarker.setTitle(""+c.get_latitude()+" "+ c.get_longitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
            Polyline line = googleMap.addPolyline(new PolylineOptions().add(new LatLng(c.get_latitude(),c.get_longitude())));
        }
//        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//            //
//            @Override
//            public View getInfoContents(Marker marker) {
//                View v = getLayoutInflater().inflate(R.layout.geotag_info_window, null);
////                ImageView iv = (ImageView) findViewById(R.id.badge);
////                iv.setImageResource(R.drawable.flower);
//                // Getting the position from the marker
//                // LatLng latLng = marker.getPosition();
//                // Getting reference to the TextView to set latitude
//                // TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
//
//                // Getting reference to the TextView to set longitude
//                //TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
//
//                // Setting the latitude
//                // tvLat.setText("Latitude:" + latLng.latitude);
//
//                // Setting the longitude
//                // tvLng.setText("Longitude:"+ latLng.longitude);
//                return v;
////                return null;
//            }
//        });



        //for geotag photos
        // getLatLongPhotos();
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        //     mapMarker.setTitle(Double.toString(mCurrentLocation.getLatitude()));
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
    }

}
