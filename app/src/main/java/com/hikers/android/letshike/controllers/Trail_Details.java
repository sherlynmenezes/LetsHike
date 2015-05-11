package com.hikers.android.letshike.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hikers.android.letshike.R;

/**
 * Created by Neha on 5/10/2015.
 */
public class Trail_Details extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_details);
        Button visited = (Button) findViewById(R.id.visit_hike);
        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trail_Details.this, OfflineMainActivity.class);
                startActivity(intent);
            }
        });
    }
}