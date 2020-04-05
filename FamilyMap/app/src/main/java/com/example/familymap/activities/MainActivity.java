package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.ui.LoginFragment;
import com.example.familymap.ui.MapFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inflate the login fragment
        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragment_login);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            fm.beginTransaction().add(R.id.main_fragment, loginFragment).commit();
        }
    }

    public void replaceLoginFragment() {
        try {
            // Get map fragment
            FragmentTransaction fmTransaction = this.getSupportFragmentManager().beginTransaction();
            MapFragment mapFragment = new MapFragment();

            // Replace login fragment with map fragment
            fmTransaction.replace(R.id.main_fragment, mapFragment, mapFragment.toString());
            fmTransaction.addToBackStack(null);
            fmTransaction.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
