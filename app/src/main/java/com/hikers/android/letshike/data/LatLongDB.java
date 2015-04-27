package com.hikers.android.letshike.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Vismay on 4/25/2015.
 */
public class LatLongDB extends SQLiteOpenHelper implements BaseColumns{

    private static final String DATABASE_NAME = "trip_db";
    private static final int DATABASE_VERSION = 2;

    public LatLongDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String TABLE_LOCATION = "location";
    private static final String TABLE_PERSON = "person";
    //Column names for location table
    private static final String COLUMN_LOCATION_NAME = "name";
    private static final String COLUMN_LOCATION_ADDR = "address";
    private static final String COLUMN_LOCATION_LAT = "latitude";
    private static final String COLUMN_LOCATION_LNG = "longitude";

    //Column names for person table
    private static final String COLUMN_PERSON_NAME = "name";
    private static final String COLUMN_PERSON_NUMBER = "phone_no";
    private static final String COLUMN_PERSON_ID = "contact_id";
    private static final String COLUMN_PERSON_LOCATION = "location_id";
    private static final String COLUMN_TRIP_ID = "trip_id";
    private static final String TAG = "LatLongDB";

    @Override
    public void onCreate(SQLiteDatabase db) {
        ArrayList<String> columnNames = new ArrayList<String>();
        ArrayList<String> columnTypes = new ArrayList<String>();

        columnNames.add(_ID);
        columnNames.add(COLUMN_LOCATION_NAME);
        columnNames.add(COLUMN_LOCATION_ADDR);
        columnNames.add(COLUMN_LOCATION_LAT);
        columnNames.add(COLUMN_LOCATION_LNG);

        columnTypes.add("INTEGER PRIMARY KEY");
        columnTypes.add("TEXT NOT NULL");
        columnTypes.add("TEXT");
        columnTypes.add("TEXT");
        columnTypes.add("TEXT");

        createTable(TABLE_LOCATION, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();

        columnNames.add(COLUMN_PERSON_ID);
        columnNames.add(COLUMN_TRIP_ID);
        columnTypes.add("INTEGER");
        columnTypes.add("INTEGER");

        createTable(TABLE_PERSON, columnNames, columnTypes, db);
        columnNames.clear();
        columnTypes.clear();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
    }
    private void createTable(String tableName, ArrayList<String> columnNames,
                             ArrayList<String> columnTypes, SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+ tableName + "(");
        for(Iterator<String> i = columnNames.iterator(), j = columnTypes.iterator();
            i.hasNext() && j.hasNext();){
            sb.append(i.next()+ " " + j.next()+",");
        }
        if(sb.length()>0 && sb.charAt(sb.length()-1)==','){
            sb.deleteCharAt(sb.length() -1);
        }
        sb.append(")");
        try{
            db.execSQL(sb.toString());
        }
        catch(SQLException e){
            Log.e(tableName, e.getMessage());
        }
    }
    public long insertLocation(String name, String lat,String lng ) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_NAME, name);
        //cv.put(COLUMN_LOCATION_ADDR, addr);
        cv.put(COLUMN_LOCATION_LAT, lat);
        cv.put(COLUMN_LOCATION_LNG, lng);
        Log.d(TAG,lat+""+lng);
        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }
//    public long insertPerson(Person p){
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_PERSON_ID,"1");
//        cv.put(COLUMN_PERSON_NAME, "vis");
//        cv.put(COLUMN_PERSON_NUMBER, "123");
//        cv.put(COLUMN_PERSON_LOCATION, "somewhere");
//
//        return getWritableDatabase().insertWithOnConflict(TABLE_PERSON,
//                null, cv,
//                SQLiteDatabase.CONFLICT_IGNORE);
//    }

}
