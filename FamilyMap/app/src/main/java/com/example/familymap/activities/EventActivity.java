package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.ui.MapFragment;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        MapFragment mapFragment = new MapFragment();
        FragmentManager fm = this.getSupportFragmentManager();
        Intent intent = getIntent();

        if (intent.hasExtra(String.valueOf(R.string.eventID_intent))) {
            // Get the eventID from the intent
            String eventID = intent.getStringExtra(String.valueOf(R.string.eventID_intent));

            // Create the bundle with the eventID as a value
            Bundle bundle = new Bundle();
            bundle.putString(String.valueOf(R.string.eventID_bundle), eventID);
            mapFragment.setArguments(bundle);
        }
        fm.beginTransaction().add(R.id.event_map_fragment, mapFragment, String.valueOf(R.string.map_fragment_tag)).commit();
    }
}
