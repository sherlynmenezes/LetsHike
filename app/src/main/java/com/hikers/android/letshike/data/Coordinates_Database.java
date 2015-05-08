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
public class Coordinates_Database extends SQLiteOpenHelper {
    private static final int DB_VERSION = 14;
    private static final String DB_NAME = "Coordinates";

    private static final String COORDINATES = "coords";
    private static final String ID = "_id";
    private static final String LONGITUDE = "Longitude";
    private static final String LATTIUDE = "Latitude";

    public Coordinates_Database(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + COORDINATES + "(" + ID
                + " integer primary key autoincrement, " + LATTIUDE
                + " double  , " + LONGITUDE + " double)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COORDINATES);
        // create tables again
        onCreate(db);
    }

    public long insert_coord(Coordinates coor) {
        ContentValues contentval = new ContentValues();

        contentval.put(LATTIUDE, coor.get_latitude());
        contentval.put(LONGITUDE, coor.get_longitude());
        Log.v("Trip Database Helper", Double.toString(coor.get_latitude()) + Double.toString(coor.get_longitude()));
        //	contentval.put(), value);
        return getWritableDatabase().insert(COORDINATES, null, contentval);
    }

    public ArrayList<Coordinates> Coords()
    {	SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Coordinates> coordinatesObjList = new ArrayList<>();

        int count =0;
        Cursor cursor = db.rawQuery("select * from " + COORDINATES, null);
        String flag ="false";
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(0);
            coordinatesObjList.add(new Coordinates(cursor.getDouble(1),cursor.getDouble(2)));
            Log.v("LONGITUDE", "" + (cursor.getDouble(1)));
            Log.v("Latitude", ""+(cursor.getDouble(2)));
        }

//        Log.v("Count", ""+count);

          return coordinatesObjList;
    }


}
