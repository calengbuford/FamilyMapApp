package com.example.familymap.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.activities.MainActivity;
import com.example.familymap.activities.PersonActivity;
import com.example.familymap.activities.SearchActivity;
import com.example.familymap.activities.SettingsActivity;
import com.example.familymap.client.Client;
import com.example.shared.model_.Event;
import com.example.shared.model_.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.familymap.R;


public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private Client client;
    private View view;
    private String eventIDBundle = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);

        // Check if the bundle has arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventIDBundle = bundle.getString( String.valueOf(R.string.eventID_bundle));
            System.out.println("eventIDBundle" + eventIDBundle);
        }
        // If null, then we are not in an Event activity
        if (eventIDBundle == null) {
            // Set menu options if NOT in an Event activity
            setHasOptionsMenu(true);
        }
        else {
            // Set the up button if in an Event activity
            MainActivity parent = (MainActivity) getActivity();
            if (parent != null && parent.getSupportActionBar() != null) {
                parent.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map = googleMap;
            map.setOnMapLoadedCallback(this);

            client = Client.getInstance();
            float color = 0;

            Event curEventViewed = client.getCurEventViewed();
            Event[] familyEvents = client.getFamilyEvents();

            for (Event event : familyEvents) {
                color = client.getEventColors().get(event.getEventType());

                // Add the event to the map as a marker
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(event.getLatitude(), event.getLongitude()))
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                        .title(event.getEventType()));
                marker.setTag(event);

                // If eventIDBundle is set, then we are in an Event activity, and focus on that event
                // If an event viewed is set in the Client, focus and click that event upon Map Fragment reload
                if (eventIDBundle == null && curEventViewed != null) {
                    if (event.getEventID().equals(curEventViewed.getEventID())) {
                        LatLng latLng = new LatLng(curEventViewed.getLatitude(), curEventViewed.getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        marker.setZIndex(1);
                        onMarkerClick(marker);
                    }
                }
                if (eventIDBundle != null) {
                    if (event.getEventID().equals(eventIDBundle)) {
                        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        marker.setZIndex(1);
                        onMarkerClick(marker);
                    }
                }
            }

            // Set a listener for marker click.
            map.setOnMarkerClickListener(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        final Event event = (Event) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (event != null) {
            String personID = event.getPersonID();
            Person person = client.getUserFamilyDict().get(personID);

            // Set view text
            if (person != null) {
                String personNameText = person.getFirstName() + " " + person.getLastName();
                String eventInfoText = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() +
                        " (" + event.getYear() + ")";

                // Connect with view and update the text
                TextView textPersonName = (TextView) view.findViewById(R.id.mapTextPrimary);
                textPersonName.setText(personNameText);
                TextView textEventInfo = (TextView) view.findViewById(R.id.mapTextSecondary);
                textEventInfo.setText(eventInfoText);

                // Set gender icon
                ImageView imageView = (ImageView) view.findViewById(R.id.mapGenderImg);
                if ("m".equals(person.getGender())) {
                    imageView.setBackgroundResource(R.drawable.male_icon);
                } else {
                    imageView.setBackgroundResource(R.drawable.female_icon);
                }
            }

            // Click listener on event info layout
            View layout = (View) view.findViewById(R.id.mapInfoView);
            layout.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickEventInfoLayout(event);
                }
            });
        }
        return false;
    }

    private void clickEventInfoLayout(Event event) {
        MainActivity parent = (MainActivity) getActivity();
        Intent myIntent = new Intent(parent, PersonActivity.class);
        myIntent.putExtra(String.valueOf(R.string.personID_intent), event.getPersonID());
        client.setCurEventViewed(event);
        if (parent != null) {
            parent.startActivity(myIntent);
        }
    }

    public void clickSearchButton() {
        MainActivity parent = (MainActivity) getActivity();
        Intent intent = new Intent(parent, SearchActivity.class);
        if (parent != null) {
            parent.startActivity(intent);
        }
    }

    public void clickSettingsButton() {
        MainActivity parent = (MainActivity) getActivity();
        Intent intent = new Intent(parent, SettingsActivity.class);
        if (parent != null) {
            parent.startActivity(intent);
        }
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

}

