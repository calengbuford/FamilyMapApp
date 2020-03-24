package com.example.familymap.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.familymap.R;
import com.example.familymap.ui.LoginFragment;

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
            fm.beginTransaction().add(R.id.login_frame_layout, loginFragment).commit();
        }
    }

    public void replaceLoginFragment() {
        // clear login fragment and inflate map fragment
    }
}
