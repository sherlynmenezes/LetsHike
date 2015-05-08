package com.hikers.android.letshike.data;

import android.support.v7.app.ActionBarActivity;


public class Coordinates extends ActionBarActivity {
    Double longitude = null;
    Double latitude = null;
    public Coordinates(Double d,Double dd){
        latitude=d;
        longitude=dd;
    }



    public void set_Longitude(Double longitude)
    {
        this.longitude =longitude;
    }

   public Double get_longitude()
    {
        return longitude;
    }

    public void set_Latitude(Double latitude)
    {
        this.latitude =latitude;
    }

  public  Double get_latitude()
    {
        return latitude;
    }
}
