package com.hikers.android.letshike.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hikers.android.letshike.data.Photos_database;
import com.hikers.android.letshike.data.Photos;
import com.hikers.android.letshike.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PhotoSelectionActivity extends ActionBarActivity {
    private final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);
        Button select = (Button) findViewById(R.id.select_photos);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        Button upload = (Button)findViewById(R.id.upload_photos);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoSelectionActivity.this, PhotoDisplay.class);
                startActivity(intent);
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap=null;
                    //String filename;
                    try {
                        Uri selectedImage = imageReturnedIntent.getData();


                        //filename = getRealPathFromURI(imageReturnedIntent.getData());
                        if(bitmap !=null)
                            bitmap.recycle();
                        InputStream imageStream = getContentResolver().openInputStream(imageReturnedIntent.getData());
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        imageStream.close();

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                        byte[] imageInByte = stream.toByteArray();

                        //Cursor c;
//                        InputStream imageStream = getContentResolver().openInputStream(imageReturnedIntent.getData());
//                        bitmap = BitmapFactory.decodeStream(imageStream);
//                        imageStream.close();
//
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                        byte[] imageInByte = stream.toByteArray();


                        String tempPath = getPath(selectedImage);


                        TextView tv1 = (TextView) findViewById(R.id.textView1);
                        TextView tv2 = (TextView) findViewById(R.id.textView2);
                        final ExifInterface exif = new ExifInterface(tempPath);
//                        final int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                ExifInterface.ORIENTATION_NORMAL);
                        final String datestamp = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
                        final String gpsLatitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                        final String gpsLatitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
                        final String gpsLongitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                        final String gpsLongitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
                        double log = convertToDegree(gpsLongitude);

                        if (!exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).equals("E")) { // deal with the negative degrees
                            log = -log;}
                        double lat = convertToDegree(gpsLatitude);
                        if (!exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("N")) {
                            lat = -lat;
                        }
                        String latString = Double.toString(log);
                        String longStrig = Double.toString(lat);
                        String encodedImage = Base64.encodeToString(imageInByte, Base64.NO_WRAP);
                        //tv1.setText(encodedImage+"");
                        tv2.setText(longStrig+" "+latString);
                        Photos_database db = new Photos_database(this);
                        Photos ph = new Photos(lat,log,tempPath);
                        db.insert_photos(ph);
                        Toast.makeText(getApplicationContext(),
                                "One photo added",
                                Toast.LENGTH_LONG).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }



        }
    }

    public String getPath(Uri uri){
        if(uri==null)return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uri, projection, null, null, null);
        if(c!=null){
            int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            c.moveToFirst();
            return c.getString(column_index);
        }
        return uri.getPath();
    }

    private Double convertToDegree(String stringDMS) {
        Double result;
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

        result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;

    }


}
//public class PhotoSelectionActivity extends ActionBarActivity {
//    private final int PICK_IMAGE = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photo_selection);
//        Button select = (Button) findViewById(R.id.select_photos);
//        select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, PICK_IMAGE);
//            }
//        });
//
//        Button upload = (Button)findViewById(R.id.upload_photos);
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PhotoSelectionActivity.this, PhotoDisplay.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//
//        switch (requestCode) {
//            case PICK_IMAGE:
//                if (resultCode == RESULT_OK) {
//                    try {
//                        Uri selectedImage = imageReturnedIntent.getData();
//                        //Cursor c;
//                        // InputStream imageStream = getContentResolver().openInputStream(selectedImage);
//                        //Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                        String tempPath = getPath(selectedImage);
//                        TextView tv1 = (TextView) findViewById(R.id.textView1);
//                        TextView tv2 = (TextView) findViewById(R.id.textView2);
//                        final ExifInterface exif = new ExifInterface(tempPath);
////                        final int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
////                                ExifInterface.ORIENTATION_NORMAL);
//                        final String datestamp = exif.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
//                        final String gpsLatitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
//                        final String gpsLatitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
//                        final String gpsLongitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                        final String gpsLongitudeRef = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
//                        double log = convertToDegree(gpsLongitude);
//
//                        if (!exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF).equals("E")) { // deal with the negative degrees
//                            log = -log;}
//                        double lat = convertToDegree(gpsLatitude);
//                        if (!exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF).equals("N")) {
//                            lat = -lat;
//                        }
//                        String latString = Double.toString(log);
//                        String longStrig = Double.toString(lat);
//                        tv1.setText(tempPath);
//                        tv2.setText(longStrig+" "+latString);
//                        Photos_database db = new Photos_database(this);
//                        Photos ph = new Photos(lat,log,tempPath);
//                        db.insert_photos(ph);
//                        Toast.makeText(getApplicationContext(),
//                                "One photo added",
//                                Toast.LENGTH_LONG).show();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//
//        }
//    }
//
//    public String getPath(Uri uri){
//        if(uri==null)return null;
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor c = getContentResolver().query(uri, projection, null, null, null);
//        if(c!=null){
//            int column_index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            c.moveToFirst();
//            return c.getString(column_index);
//        }
//        return uri.getPath();
//    }
//
//    private Double convertToDegree(String stringDMS) {
//        Double result;
//        String[] DMS = stringDMS.split(",", 3);
//
//        String[] stringD = DMS[0].split("/", 2);
//        Double D0 = new Double(stringD[0]);
//        Double D1 = new Double(stringD[1]);
//        Double FloatD = D0 / D1;
//
//        String[] stringM = DMS[1].split("/", 2);
//        Double M0 = new Double(stringM[0]);
//        Double M1 = new Double(stringM[1]);
//        Double FloatM = M0 / M1;
//
//        String[] stringS = DMS[2].split("/", 2);
//        Double S0 = new Double(stringS[0]);
//        Double S1 = new Double(stringS[1]);
//        Double FloatS = S0 / S1;
//
//        result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));
//
//        return result;
//
//    }
//
//
//}
