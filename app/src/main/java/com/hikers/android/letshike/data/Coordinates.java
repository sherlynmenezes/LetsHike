package com.hikers.android.letshike.data;

import android.support.v7.app.ActionBarActivity;


public class Coordinates extends ActionBarActivity {
    double longitude = 0.0;
    double latitude = 0.0;
    public Coordinates(double d,double dd){
        longitude=d;
        latitude=dd;
    }



    public void set_Longitude(Double longitude)
    {
        this.longitude =longitude;
    }

   public double get_longitude()
    {
        return longitude;
    }

    public void set_Latitude(Double latitude)
    {
        this.latitude =latitude;
    }

  public  double get_latitude()
    {
        return latitude;
    }
}
