package com.hikers.android.letshike.data;

import android.app.Activity;


public class Photos extends Activity {
    Double longitude = null;
    Double latitude = null;
    String path = null;
    public Photos(Double d, Double dd, String p){
        latitude=d;
        longitude=dd;
        path = p;
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

  public Double get_latitude()
    {
        return latitude;
    }

    public String get_path(){return path;}
    public void set_path(String path){this.path = path;}
}
