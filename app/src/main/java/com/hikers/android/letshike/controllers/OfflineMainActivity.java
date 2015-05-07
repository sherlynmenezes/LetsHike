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

        setContentView(R.layout.activity_main);
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
        cd.insert_coord(new Coordinates(40.70, -73.00));
        cd.insert_coord(new Coordinates(40.72,-73.02));
        cd.insert_coord(new Coordinates(40.74,-73.04));
        cd.insert_coord(new Coordinates(40.76,-73.06));
        cd.insert_coord(new Coordinates(40.78,-73.08));
        cd.insert_coord(new Coordinates(40.80,-73.10));
        cd.insert_coord(new Coordinates(40.82,-73.12));
        cd.insert_coord(new Coordinates(40.84,-73.14));
        cd.insert_coord(new Coordinates(40.85,-73.16));
        cd.insert_coord(new Coordinates(40.86,-73.18));



       // cd.insertFriends(id, ppl);


        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        addMarker();
    }

    private void addMarker() {
        MarkerOptions options = new MarkerOptions();

        // following four lines requires 'Gors.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info


        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        Coordinates_Database cd=new Coordinates_Database(this);
        ArrayList<Coordinates> coordinates=new ArrayList<>();
        coordinates.addAll(cd.Coords());
        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
        rectLine.add(new LatLng(coordinates.get(0).get_latitude(), coordinates.get(0).get_longitude()));

        for(Coordinates coordinates1:coordinates){
                        rectLine.add(new LatLng(coordinates1.get_latitude(),coordinates1.get_longitude()));
            googleMap.addPolyline(rectLine);
        }


//                .add(new LatLng(37.63, -121.99))
//                .add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.35, -122.0))
//                .add(new LatLng(37.35, -122.0))




//
//                        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
//                        rectLine.add(my_latlong,directionPoint.get(ii));
//                        Polyline polyline=map.addPolyline(rectLine);
//                        polylines.add(polyline);


             options.position(currentLatLng);
             Marker mapMarker = googleMap.addMarker(options);
        long atTime = mCurrentLocation.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        mapMarker.setTitle(Double.toString(mCurrentLocation.getLatitude()));
        Log.d(TAG, "Marker added.............................");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                13));
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