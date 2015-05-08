package com.hikers.android.letshike.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hikers.android.letshike.R;

import java.util.ArrayList;

/**
 * Created by Neha on 4/6/2015.
 */
public class TripHistory extends ActionBarActivity{
        String a;
        ArrayAdapter<String> adapter = null;
        ListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_own_trails);
        mListView=(ListView)findViewById(R.id.listView);
        ArrayList<String> mTripTitles=new ArrayList<>();
        mTripTitles.add("California trail");
        mTripTitles.add("bayridge");
        mTripTitles.add("queens");
        mTripTitles.add("somethinsg");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTripTitles);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewOfflineGPSmap();

            }
        });
    }

    private void viewOfflineGPSmap() {
        Intent intent = new Intent(this, OfflineMainActivity.class);
        startActivity(intent);
    }
}



