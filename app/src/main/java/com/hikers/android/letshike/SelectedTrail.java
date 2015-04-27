package com.hikers.android.letshike;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.hikers.android.letshike.data.LatLongDB;
//import com.hikers.android.letshike.Models.DatabaseManager;
//import com.hikers.android.letshike.Models.DatabaseObject;


public class SelectedTrail extends FragmentActivity implements OnMapReadyCallback,LocationListener{
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Lets Hike!";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;

    private Button btnCapturePicture;
    private GoogleMap mMap;
    private Location mCurrentLocation;
    private MarkerOptions mMarkerOptions ;
    private MapFragment mMapFragment;
    LocationManager lm;
    private TextView lt, ln;
    GoogleMap googleMap;
    String provider;
    Location l;
   ;
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
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }//           googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//               @Override
//               public void onMapClick(LatLng latLng) {
//                   // Drawing marker on the map
//                   drawMarker(latLng);
//                   insertLoc(latLng);
//
//
//                   Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
//               }
//           });
//        }


        //get location service
        lm=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        Criteria c=new Criteria();
        //criteria object will select best service based on
        //Accuracy, power consumption, response, bearing and monetary cost
        //set false to use best service otherwise it will select the default Sim network
        //and give the location based on sim network
        //now it will first check satellite than Internet than Sim network location
        provider=lm.getBestProvider(c, false);
        //now you have best provider
        //get location
        l=lm.getLastKnownLocation(provider);
        double lng=0.0;
        double lat=0.0;
//        if(l!=null)
//        {
//            //get latitude and longitude of the location
//             lng=l.getLongitude();
//             lat=l.getLatitude();
//            //display on text view
//            ln.setText(""+lng);
//            lt.setText(""+lat);
//        }
//        else
//        {
//            ln.setText("No Provider");
//            lt.setText("No Provider");
//        }

//        findDistance(lat,lng);
//        findAddress(lat,lng);

    }
    void insertLoc(LatLng latlng){
        LatLongDB tdh = new LatLongDB(getApplicationContext());
        tdh.insertLocation("MSG",latlng.latitude+"",latlng.longitude+"");
    }
    private void drawMarker(LatLng point){
//        // Creating an instance of MarkerOptions
//        MarkerOptions markerOptions = new MarkerOptions();
//
//        // Setting latitude and longitude for the marker
//        markerOptions.position(point);
//
//        // Adding marker on the Google Map
//        googleMap.addMarker(markerOptions);

    }
    private void findAddress(double lat, double lng) {
        Geocoder geo=new Geocoder(getApplicationContext(), Locale.getDefault());

        String mylocation;
        if(Geocoder.isPresent())
        {
            try {
                List<Address> addresses = geo.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0)
                {
                    Address address = addresses.get(0);
                    String addressText = String.format("%s, %s, %s",
                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?address.getAddressLine(0) : "",
                            // Locality is usually a city
                            address.getLocality(),
                            // The country of the address
                            address.getCountryName());
                    mylocation="Lattitude: "+l.getLatitude()+" Longitude: "+l.getLongitude()+"\nAddress: "+addressText;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findDistance(double lat,double lng) {
        Location l1=new Location("One");
        l1.setLatitude(lat);
        l1.setLongitude(lng);

//        Location l2=new Location("Two");
//        l2.setLatitude(Double.parseDouble(frnd_lat));
//        l2.setLongitude(Double.parseDouble(frnd_longi));

//        float distance_bw_one_and_two=l1.distanceTo(l2);
    }


    @Override
    public void onMapReady(final GoogleMap map) {
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(37.35,-122.0));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(10);

        map.moveCamera(center);
        map.animateCamera(zoom);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //drawMarker(latLng);
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.hiking)));
                   insertLoc(latLng);
                Log.d("coordinates",latLng.latitude+""+latLng.longitude);
            }
        });
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
        map.addMarker(new MarkerOptions()
                .position(new LatLng(40.7127, 74.0059))
                .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.treking1)));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(37.35, -122.2))
                .title("Hello world").icon(BitmapDescriptorFactory.fromResource(R.drawable.treking1)));


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
        else if(id==R.id.camera){

            captureImage();
            Log.d("cameramenu","cameramen");
//            btnCapturePicture.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    // capture picture
//                    captureImage();
//                }
//            });



        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isDeviceSupportCamera() {
        Log.d("cameramenu","isdevicecam");

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void captureImage() {
        Log.d("cameramenu","captureImage");

        Toast t= Toast.makeText(this,"captureImage",Toast.LENGTH_LONG);
        t.show();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        Toast t1= Toast.makeText(this,"captureImage2",Toast.LENGTH_LONG);
        t1.show();
    }




    /**
     * Creating file uri to store image/video
     */
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



    //If you want location on changing place also than use below method
    //otherwise remove all below methods and don't implement location listener
    @Override
    public void onLocationChanged(Location arg0)
    {
        double lng=l.getLongitude();
        double lat=l.getLatitude();
        ln.setText(""+lng);
        lt.setText(""+lat);
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
    }
//    public class Request_Update extends AsyncTask<Location, Void, Location> {
//        DocumentsContract doc;
//        @Override
//        protected void onPreExecute()
//        {
//            //Toast.makeText(getApplicationContext(), "onPreExecute()!", Toast.LENGTH_SHORT).show();
//        }
//        @Override
//        protected Location doInBackground(Location... location) {
//            // TODO Auto-generated method stub
//
//            String url = "http://maps.googleapis.com/maps/api/directions/xml?"
//                    + "origin=" + location[0].getLatitude() + "," + location[0].getLongitude()
//                    + "&destination=" + frnd_lat + "," + frnd_longi
//                    + "&sensor=false&units=metric&mode="+direction; //direction="walking" or "driving"
//
//
//            try {
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpContext localContext = new BasicHttpContext();
//                HttpPost httpPost = new HttpPost(url);
//                HttpResponse response = httpClient.execute(httpPost, localContext);
//                InputStream in = response.getEntity().getContent();
//                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//                doc = builder.parse(in);
//            } catch (Exception e) {
//            }
//
//            return location[0];
//        }
//
//        @Override
//        protected void onPostExecute(Location location)
//        {
//            if(doc!=null)
//            {
//                directionPoint=getDirection(doc);
//                int ii = 0;
//                size_of_latlong=directionPoint.size();
//                for( ; ii <size_of_latlong ; ii++) {
//                    if(ii==0)
//                    {
//                        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
//                        rectLine.add(my_latlong,directionPoint.get(ii));
//                        Polyline polyline=map.addPolyline(rectLine);
//                        polylines.add(polyline);
//                    }
//                    else
//                    {
//                        PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
//                        rectLine.add(directionPoint.get(ii-1),directionPoint.get(ii));
//                        Polyline polyline=map.addPolyline(rectLine);
//                        polylines.add(polyline);
//                    }
//                }
//                PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.RED);
//                rectLine.add(frnd_latlong,directionPoint.get(ii-1));
//                Polyline polyline=map.addPolyline(rectLine);
//                polylines.add(polyline);
//                //map.addPolyline(rectLine);
//            }
//        }
//    }
//
//    public ArrayList<LatLng> getDirection(DocumentsContract.Document doc) {
//        NodeList nl1, nl2, nl3;
//        ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
//        nl1 = doc.getElementsByTagName("step");
//        if (nl1.getLength() > 0) for (int i = 0; i < nl1.getLength(); i++) {
//            Node node1 = nl1.item(i);
//            nl2 = node1.getChildNodes();
//
//            Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
//            nl3 = locationNode.getChildNodes();
//            Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
//            double lat = Double.parseDouble(latNode.getTextContent());
//            Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
//            double lng = Double.parseDouble(lngNode.getTextContent());
//            listGeopoints.add(new LatLng(lat, lng));
//
//            locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
//            nl3 = locationNode.getChildNodes();
//            latNode = nl3.item(getNodeIndex(nl3, "points"));
//            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
//            for (int j = 0; j < arr.size(); j++) {
//                listGeopoints.add(new LatLng(arr.get(j).latitude, arr.get(j).longitude));
//            }
//
//            locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
//            nl3 = locationNode.getChildNodes();
//            latNode = nl3.item(getNodeIndex(nl3, "lat"));
//            lat = Double.parseDouble(latNode.getTextContent());
//            lngNode = nl3.item(getNodeIndex(nl3, "lng"));
//            lng = Double.parseDouble(lngNode.getTextContent());
//            listGeopoints.add(new LatLng(lat, lng));
//        }
//        return listGeopoints;
//    }
//
//    private int getNodeIndex(NodeList nl, String nodename) {
//        for(int i = 0 ; i < nl.getLength() ; i++) {
//            if(nl.item(i).getNodeName().equals(nodename))
//                return i;
//        }
//        return -1;
//    }
//
//    private ArrayList<LatLng> decodePoly(String encoded) {
//        ArrayList<LatLng> poly = new ArrayList<LatLng>();
//        int index = 0, len = encoded.length();
//        int lat = 0, lng = 0;
//        while (index < len) {
//            int b, shift = 0, result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lat += dlat;
//            shift = 0;
//            result = 0;
//            do {
//                b = encoded.charAt(index++) - 63;
//                result |= (b & 0x1f) << shift;
//                shift += 5;
//            } while (b >= 0x20);
//            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//            lng += dlng;
//
//            LatLng position = new LatLng((double)lat / 1E5, (double)lng / 1E5);
//            poly.add(position);
//        }
//        return poly;
//    }
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
        Toast t= Toast.makeText(this,"onActivityRes",Toast.LENGTH_LONG);
        t.show();
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

        }
    }

}
