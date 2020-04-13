package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.familymap.R;
import com.example.familymap.client.*;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class SettingsActivity extends AppCompatActivity {
    final Client client = Client.getInstance();
    final Filter filter = new Filter();
    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        settings = Settings.getInstance();

        // Connect with elements in the view
        Switch lifeStoryLinesSwitch = (Switch) findViewById(R.id.lifeStoryLinesSwitch);
        Switch familyTreeLinesSwitch = (Switch) findViewById(R.id.familyTreeLinesSwitch);
        Switch spouseLinesSwitch = (Switch) findViewById(R.id.spouseLinesSwitch);
        Switch fatherSideSwitch = (Switch) findViewById(R.id.fatherSideSwitch);
        Switch motherSideSwitch = (Switch) findViewById(R.id.motherSideSwitch);
        Switch maleEventSwitch = (Switch) findViewById(R.id.maleEventSwitch);
        Switch femaleEventSwitch = (Switch) findViewById(R.id.femaleEventSwitch);
        RelativeLayout logoutButton = (RelativeLayout) findViewById(R.id.logoutButton);

        // Check switches as according to Settings
        if (settings != null) {
            lifeStoryLinesSwitch.setChecked(settings.getLifeStoryLinesSwitchStatus());
            familyTreeLinesSwitch.setChecked(settings.getFamilyTreeLinesSwitchStatus());
            spouseLinesSwitch.setChecked(settings.getSpouseLinesSwitchStatus());
            fatherSideSwitch.setChecked(settings.getFatherSideSwitchStatus());
            motherSideSwitch.setChecked(settings.getMotherSideSwitchStatus());
            maleEventSwitch.setChecked(settings.getMaleEventSwitchStatus());
            femaleEventSwitch.setChecked(settings.getFemaleEventSwitchStatus());
        }

        // Switch listeners
        lifeStoryLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeStoryLinesSwitchStatus(isChecked);
            }
        });
        familyTreeLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTreeLinesSwitchStatus(isChecked);
            }
        });
        spouseLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLinesSwitchStatus(isChecked);
            }
        });
        fatherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFatherSideSwitchStatus(isChecked);
            }
        });
        motherSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setMotherSideSwitchStatus(isChecked);
            }
        });
        maleEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setMaleEventSwitchStatus(isChecked);
            }
        });
        femaleEventSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFemaleEventSwitchStatus(isChecked);
            }
        });

        // Logout listener
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.reset();     // Set null the instance of client
                settings.reset();   // Set null the instance of settings

                System.out.println("CLICKED LOGOUT!!!!!!!");

                // Go to Main Activity
                finishAffinity();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Filter when user clicks the up button to return to map page
        int id = item.getItemId();
        if (id == android.R.id.home){
            filter.filterEvents(this);      // Filter events based off of switches
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
