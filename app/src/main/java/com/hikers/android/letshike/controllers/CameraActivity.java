package com.hikers.android.letshike.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.hikers.android.letshike.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends ActionBarActivity {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    // private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    // public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Lets Hike!";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    //private VideoView videoPreview;
    private Button btnCapturePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

       // imgPreview = (ImageView) findViewById(R.id.imgPreview);
        //videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        // btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        /**
         * Record video button click event
         */
//        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
//
//           @Override
//           public void onClick(View v) {
////                // record video
////                //recordVideo();
//                printLatLong();
//          }
//        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }
    /**
     * Print lat long values of the picture
     */
//    private void printLatLong(){
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                IMAGE_DIRECTORY_NAME);
//        File sd = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_20150426_171439.jpg");
//        try {
//            ExifInterface exif = new ExifInterface(sd.toString()); // create the ExifInterface file
//
//
//            /** get the attribute. rest of the attributes are the same. i will add convertToDegree on the bottom (not required) **/
//            float log = convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
//
//            if (!exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).equals("E")) { // deal with the negative degrees
//                log = -log;
//            }
//            float lat = convertToDegree(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
//            if (!exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("N")) {
//                lat = -lat;
//            }
//            String latString = Float.toString(log);
//            String longStrig = Float.toString(lat);
//
//            Log.v("Latitude",latString);
//            Log.v("Longitude",longStrig);
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }

    /**
     * convert lat long to format
     */

//    private Float convertToDegree(String stringDMS){
//        Float result = null;
//        String[] DMS = stringDMS.split(",", 3);
//
//        String[] stringD = DMS[0].split("/", 2);
//        Double D0 = new Double(stringD[0]);
//        Double D1 = new Double(stringD[1]);
//        Double FloatD = D0/D1;
//
//        String[] stringM = DMS[1].split("/", 2);
//        Double M0 = new Double(stringM[0]);
//        Double M1 = new Double(stringM[1]);
//        Double FloatM = M0/M1;
//
//        String[] stringS = DMS[2].split("/", 2);
//        Double S0 = new Double(stringS[0]);
//        Double S1 = new Double(stringS[1]);
//        Double FloatS = S0/S1;
//
//        result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
//
//        return result;
//
//    }


    /**
     * Checking device has camera hardware or not
     * */
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

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
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

    /**
     * Recording video
     */
//    private void recordVideo() {
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
//
//        // set video quality
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
//        // name
//
//        // start the video capture Intent
//        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
//    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
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
//        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // video successfully recorded
//                // preview the recorded video
//                previewVideo();
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled recording
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled video recording", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to record video
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
//                        .show();
//            }
        }
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            imgPreview.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Previewing recorded video
     */
//    private void previewVideo() {
//        try {
//            // hide image preview
//            imgPreview.setVisibility(View.GONE);
//
//            videoPreview.setVisibility(View.VISIBLE);
//            videoPreview.setVideoPath(fileUri.getPath());
//            // start playing
//            videoPreview.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * ------------ Helper Methods ----------------------
     * */

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

}
