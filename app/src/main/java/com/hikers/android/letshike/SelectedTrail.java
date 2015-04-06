package com.hikers.android.letshike;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.hikers.android.letshike.Models.DatabaseManager;
//import com.hikers.android.letshike.Models.DatabaseObject;


public class SelectedTrail extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private Location mCurrentLocation;
    private MarkerOptions mMarkerOptions ;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trail);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        DatabaseManager db = new DatabaseManager(this);
//        List<DatabaseObject> K = db.getAllDatabaseObject();
//        for (DatabaseObject cn : K) {
//            point = new GeoPoint((int)(cn.getlat()*E6), (int)(cn.getlng()*E6));
//            overlayitem = new OverlayItem(point, cn.getname(), cn.getinfo());
//            itemizedOverlay.addOverlay(overlayitem);
//            mapOverlays.add(itemizedOverlay);
//        }

    }



    @Override
    public void onMapReady(GoogleMap map) {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(37.35,-122.0));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(10);

        map.moveCamera(center);
        map.animateCamera(zoom);
//
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(10, 10))
//                .title("Hello world"));
//        Polyline line = map.addPolyline(new PolylineOptions()
//                .add(new LatLng(37.35, -122.0), new LatLng(37.45, -122.0))
//                .width(5)
//                .color(Color.RED));
        // Instantiates a new Polyline object and adds points to define a rectangle
        PolylineOptions rectOptions = new PolylineOptions()
                .add(new LatLng(37.35, -122.0))
                .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                .add(new LatLng(37.35, -122.0))
                .color(Color.RED); // Closes the polyline.

//Get back the mutable Polyline
        Polyline polyline = map.addPolyline(rectOptions);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.35, -122.0))
                .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.hiking)));
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(40.7127, 74.0059))
//                .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.treking1)));
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(37.35, -122.2))
//                .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.treking1)));


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selected_trail, menu);
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
        else if(id==R.id.action_map){
            Intent intent = new Intent(this,SelectedTrail.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
