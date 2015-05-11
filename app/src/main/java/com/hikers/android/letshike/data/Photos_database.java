package com.hikers.android.letshike.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Neha on 5/6/2015.
 */
public class Photos_database extends SQLiteOpenHelper {
    private static final int DB_VERSION = 14;
    private static final String DB_NAME = "PhotosDB";

    private static final String PHOTOS = "Photos";
    private static final String ID = "_id";
    private static final String LONGITUDE = "Longitude";
    private static final String LATTIUDE = "Latitude";
    private static final String PATH = "Path";


    public Photos_database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PHOTOS + "("
                + ID + " integer primary key autoincrement, "
                + LATTIUDE + " double  , "
                + LONGITUDE + " double ,"
                + PATH + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PHOTOS);
        // create tables again
        onCreate(db);
    }

    public long insert_photos(Photos phts) {
        ContentValues contentval = new ContentValues();

        contentval.put(LATTIUDE, phts.get_latitude());
        contentval.put(LONGITUDE, phts.get_longitude());
        contentval.put(PATH, phts.get_path());

        Log.v("Trip Database Helper", Double.toString(phts.get_latitude()) + Double.toString(phts.get_longitude()));
        Log.v("trip data", "" + getWritableDatabase().insert(PHOTOS, null, contentval));
        return getWritableDatabase().insert(PHOTOS, null, contentval);
    }

    public ArrayList<Photos> photo_Coords()
    {	SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Photos> coordinatesObjList = new ArrayList<>();
        int count =0;
        Cursor cursor = db.rawQuery("select * from " + PHOTOS, null);
        String flag ="false";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            coordinatesObjList.add(new Photos(cursor.getDouble(1),cursor.getDouble(2),cursor.getString(3)));
            Log.v("say LONGITUDE", "" + (cursor.getDouble(1)));
            Log.v("say Latitude", ""+(cursor.getDouble(2)));
        }

        return coordinatesObjList;
    }


}
