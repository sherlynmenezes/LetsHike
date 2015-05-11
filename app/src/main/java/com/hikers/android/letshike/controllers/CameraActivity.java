package com.hikers.android.letshike.controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.hikers.android.letshike.R;

public class CameraActivity extends ActionBarActivity {


    private Trip mTrip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mTrip = new Trip();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        // Begin with main data entry view,
        // NewMealFragment
        setContentView(R.layout.activity_new_meal);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new CameraFragment();
            manager.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
//
//    public Meal getCurrentMeal() {
//        return meal;
//    }
}
