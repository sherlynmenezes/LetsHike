package com.hikers.android.letshike.controllers;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OfflineMainActivity extends FragmentActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationActivity";
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

        setContentView(R.layout.activity_main_offline_gps);
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
        Coordinates_Database cd = new Coordinates_Database(this);
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;

        //Change
        Log.v("**** LATITUDE ****", Double.toString(mCurrentLocation.getLatitude()));
        Log.v("**** LONGITUDE ****", Double.toString(mCurrentLocation.getLongitude()));
        Coordinates coord_latLong = new Coordinates(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        cd.insert_coord(coord_latLong);
        //dummy values
//        cd.insert_coord(new Coordinates(40.69430, -73.98530));
//        cd.insert_coord(new Coordinates(40.69435,-73.98535));
//        cd.insert_coord(new Coordinates(40.69440,-73.98540));
//        cd.insert_coord(new Coordinates(40.69445,-73.98545));
//        cd.insert_coord(new Coordinates(40.69450,-73.98550));
//        cd.insert_coord(new Coordinates(40.69455,-73.98555));
//        cd.insert_coord(new Coordinates(40.69460,-73.98560));
//        cd.insert_coord(new Coordinates(40.69465,-73.98565));
//        cd.insert_coord(new Coordinates(40.69470,-73.98570));
//        cd.insert_coord(new Coordinates(40.69475,-73.98575));



       // cd.insertFriends(id, ppl);


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
       // coordinates.addAll(cd.Coords());
        int count =1 , countline=1;
        for(Coordinates c : coordinates)
        {
            LatLng currentLatLng = new LatLng(c.get_latitude(), c.get_longitude());
           // count++;
            Log.v("long", ""+c.get_latitude()+c.get_longitude());
            options.position(currentLatLng);
            Marker mapMarker = googleMap.addMarker(options);
            mapMarker.setTitle(Double.toString(c.get_latitude()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 7));
            Polyline line = googleMap.addPolyline(new PolylineOptions().add(new LatLng(c.get_latitude(),c.get_longitude())));
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
//        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
//        rectLine.add(new LatLng(coordinates.get(0).get_latitude(), coordinates.get(0).get_longitude()));
//
//        for(Coordinates coordinates1:coordinates){
//                        rectLine.add(new LatLng(coordinates1.get_latitude(),coordinates1.get_longitude()));
//            googleMap.addPolyline(rectLine);
//        }

//        options.position(currentLatLng);
//        Marker mapMarker = googleMap.addMarker(options);
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
    }
}