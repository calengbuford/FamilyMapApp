package com.example.familymap.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.familymap.R;

public class MapFragment extends Fragment {

    public MapFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Connect with view
//        serverHostTextEntry = (EditText) v.findViewById(R.id.serverHostTextEntry);
//        serverPortTextEntry = (EditText) v.findViewById(R.id.serverPortTextEntry);
//        usernameTextEntry = (EditText) v.findViewById(R.id.usernameTextEntry);

        return v;
    }

}
