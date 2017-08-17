package com.hypertrack.example_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.hypertrack.lib.HyperTrack;
import com.hypertrack.lib.HyperTrackMapAdapter;
import com.hypertrack.lib.HyperTrackMapFragment;
import com.hypertrack.lib.MapFragmentCallback;

import java.util.ArrayList;

/**
 * Created by piyush on 04/11/16.
 */
public class UserMapActivity extends AppCompatActivity {

    ArrayList<String> actions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_map);

        Intent intent = getIntent();
        if (intent != null) {
            actions = intent.getStringArrayListExtra("actions");
        }

        // Initialize Map Fragment added in Activity Layout to getMapAsync
        // Once map is created onMapReady callback will be fire with GoogleMap object
        HyperTrackMapFragment htMapFragment = (HyperTrackMapFragment) getSupportFragmentManager().findFragmentById(R.id.htMapfragment);

        /**
         * Call the method below to enable UI customizations for Live Tracking View, an instance of HyperTrackMapAdapter needs to be set as depicted below
         */
         htMapFragment.setHTMapAdapter(new MyMapAdapter(this));

        /*
         * Call the method below to register for any callbacks/updates on Live Tracking View/Map
         */
        htMapFragment.setMapFragmentCallback(mapFragmentCallback);
    }

    public class MyMapAdapter extends HyperTrackMapAdapter {
        public MyMapAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public boolean showHeroMarkerForActionID(HyperTrackMapFragment hyperTrackMapFragment, String actionID) {
            // check if my action to be tracked is pickup action or dropoff action
            // and disable hero marker for pickup action
            return super.showHeroMarkerForActionID(hyperTrackMapFragment, actionID);
        }
    }

    private MapFragmentCallback mapFragmentCallback = new MapFragmentCallback() {
        @Override
        public void onMapReadyCallback(HyperTrackMapFragment hyperTrackMapFragment, GoogleMap map) {
            // Handle onMapReady callback here
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HyperTrack.removeActions(actions);
    }
}
