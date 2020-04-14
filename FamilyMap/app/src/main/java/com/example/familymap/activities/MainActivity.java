package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.familymap.R;
import com.example.familymap.model.Client;
import com.example.familymap.ui.LoginFragment;
import com.example.familymap.ui.MapFragment;

public class MainActivity extends AppCompatActivity {
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = Client.getInstance();

        // Inflate the login fragment
        FragmentManager fm = this.getSupportFragmentManager();

        // If client is null, then the user has not logged in. Otherwise, reload the Map Fragment.
        if (client.getPerson() == null) {
            LoginFragment loginFragment = new LoginFragment();
            fm.beginTransaction().add(R.id.main_fragment, loginFragment, String.valueOf(R.string.login_fragment_tag)).commit();
        }
        else {
            MapFragment mapFragment = new MapFragment();

            // Create the bundle with the eventID as a none
            Bundle bundle = new Bundle();
            bundle.putString(String.valueOf(R.string.eventID_bundle), "none");
            mapFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.main_fragment, mapFragment, String.valueOf(R.string.map_fragment_tag)).commit();
        }
    }

    public void replaceLoginFragment() {
        try {
            // Get map fragment
            FragmentTransaction fmTransaction = this.getSupportFragmentManager().beginTransaction();
            MapFragment mapFragment = new MapFragment();

            // Create the bundle with the eventID as a none
            Bundle bundle = new Bundle();
            bundle.putString(String.valueOf(R.string.eventID_bundle), "none");
            mapFragment.setArguments(bundle);

            // Replace login fragment with map fragment
            fmTransaction.replace(R.id.main_fragment, mapFragment, String.valueOf(R.string.map_fragment_tag));
            fmTransaction.addToBackStack(null);
            fmTransaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Check that the login fragment is not displaying
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(R.string.login_fragment_tag));
        if (!(loginFragment != null && loginFragment.isVisible())) {
            // Inflate the menu and add items to the action bar
            getMenuInflater().inflate(R.menu.menu_icons, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(String.valueOf(R.string.map_fragment_tag));
        if (mapFragment != null) {
            switch (item.getItemId()) {
                case R.id.search_icon:
                    mapFragment.clickSearchButton();
                    return (true);
                case R.id.settings_icon:
                    mapFragment.clickSettingsButton();
                    return (true);
            }
        }
        return(super.onOptionsItemSelected(item));
    }
}
