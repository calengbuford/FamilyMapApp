package com.example.familymap.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.activities.EventActivity;
import com.example.familymap.activities.MainActivity;
import com.example.familymap.activities.PersonActivity;
import com.example.familymap.activities.SearchActivity;
import com.example.familymap.activities.SettingsActivity;
import com.example.familymap.model.Client;
import com.example.familymap.model.Settings;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private Client client;
    private Settings settings;
    private View view;
    private String eventIDBundle = null;
    private Activity parent;
    private List<Polyline> allPolylines = new ArrayList<Polyline>();

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);

        // Check if the bundle has arguments
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventIDBundle = bundle.getString(String.valueOf(R.string.eventID_bundle));

            // Set menu options if NOT in an Event activity
            if ("none".equals(eventIDBundle)) {
                System.out.println("In MAIN activity!!!!!!!!!!!!!!!");
                setHasOptionsMenu(true);
                parent = (MainActivity) getActivity();
            }
            // Set the up button if in an Event activity
            else {
                System.out.println("In EVENT activity!!!!!!!!!!!!!!");
                parent = (EventActivity) getActivity();
                EventActivity parent = (EventActivity) getActivity();
                if (parent != null && parent.getSupportActionBar() != null) {
                    parent.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                if (parent != null && parent.getActionBar() != null) {
                    parent.getActionBar().setDisplayHomeAsUpEnabled(true);
                }
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
            settings = Settings.getInstance();
            float color = 0;

            Event curEventViewed = client.getCurEventViewed();
            List<Event> familyEvents = client.getFilteredFamilyEvents();

            for (Event event : familyEvents) {
                color = client.getEventColors().get(event.getEventType().toLowerCase());

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

            // Draw any lines according to filters
            drawPolylines(event);

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

    private void drawPolylines(Event markerEvent) {
        clearPolylines();   // Remove all lines from graph

        // Add lines according to settings switches
        if (settings.getLifeStoryLinesSwitchStatus()) {
            List<Event> events = getLifeStoryLineEvents(markerEvent.getPersonID());

            if (events != null) {
                // Add points to ArrayList
                ArrayList<LatLng> coordinateList = new ArrayList<LatLng>();
                for (int i = 0; i < events.size(); i++) {
                    coordinateList.add(new LatLng(events.get(i).getLatitude(), events.get(i).getLongitude()));
                }

                // Create polyline options
                PolylineOptions polylineOptions = new PolylineOptions();    // Initialize polyline to be drawn
                polylineOptions.addAll(coordinateList);
                polylineOptions.color(Color.BLUE);

                // Add multiple plot lines to map and to the total list of polylines
                allPolylines.add(map.addPolyline(polylineOptions));
            }
        }
        if (settings.getFamilyTreeLinesSwitchStatus()) {
            drawFamilyTree(markerEvent, markerEvent.getPersonID(), 0);
        }
        if (settings.getSpouseLinesSwitchStatus()) {
            Event event = getSpouseLineEvent(markerEvent.getPersonID());

            if (event != null) {
                // Create polyline options
                PolylineOptions polylineOptions = new PolylineOptions();    // Initialize polyline to be drawn
                polylineOptions.add(new LatLng(markerEvent.getLatitude(), markerEvent.getLongitude()),
                        new LatLng(event.getLatitude(), event.getLongitude()));
                polylineOptions.color(Color.RED);

                // Add multiple plot lines to map
                allPolylines.add(map.addPolyline(polylineOptions));
            }
        }

    }

    private List<Event> getLifeStoryLineEvents(String personID) {
        return client.getLifeEvents(personID);
    }

    private void drawFamilyTree(Event childEvent, String personID, Integer genFromPerson) {
        // Call recursion on ancestors
        Person person = client.getUserFamilyDict().get(personID);
        float lineWidth = (float)(25 / (1 + genFromPerson));
        if (lineWidth < 1) {
            lineWidth = (float) 1;
        }

        if (person != null && person.getFatherID() != null) {
            List<Event> parentEvents = client.getLifeEvents(person.getFatherID());  // Father's life events
            if (parentEvents != null && parentEvents.size() > 0) {
                addOnePolylineToMap(childEvent, parentEvents.get(0), lineWidth);
                drawFamilyTree(parentEvents.get(0), person.getFatherID(), genFromPerson + 1);
            }
            else {
                drawFamilyTree(childEvent, person.getFatherID(), genFromPerson + 1);
            }
        }
        if (person != null && person.getMotherID() != null) {
            List<Event> parentEvents = client.getLifeEvents(person.getMotherID());  // Mother's life events
            if (parentEvents != null && parentEvents.size() > 0) {
                addOnePolylineToMap(childEvent, parentEvents.get(0), lineWidth);
                drawFamilyTree(parentEvents.get(0), person.getMotherID(), genFromPerson + 1);
            }
            else {
                drawFamilyTree(childEvent, person.getMotherID(), genFromPerson + 1);
            }
        }
    }

    private Event getSpouseLineEvent(String personID) {
        Person person = client.getUserFamilyDict().get(personID);   // Get person from the client
        if (person != null && person.getSpouseID() != null) {
            if (client.getLifeEvents(person.getSpouseID()).size() > 0) {
                return client.getLifeEvents(person.getSpouseID()).get(0);   // Get the first event of their spouse
            }
        }
        return null;
    }

    private void addOnePolylineToMap(Event event1, Event event2, float lineWidth) {
        // Create polyline options
        PolylineOptions polylineOptions = new PolylineOptions();    // Initialize polyline to be drawn
        polylineOptions.add(new LatLng(event1.getLatitude(), event1.getLongitude()),
                new LatLng(event2.getLatitude(), event2.getLongitude()));
        polylineOptions
                .width(lineWidth)
                .color(Color.MAGENTA);

        // Add multiple plot lines to map
        allPolylines.add(map.addPolyline(polylineOptions));
    }

    private void clearPolylines() {
        // Remove all polylines from the map
        for(Polyline line : allPolylines)
        {
            line.remove();
        }
        allPolylines.clear();
    }

    private void clickEventInfoLayout(Event event) {
        Intent myIntent = new Intent(parent, PersonActivity.class);
        myIntent.putExtra(String.valueOf(R.string.personID_intent), event.getPersonID());
        client.setCurEventViewed(event);
        if (parent != null) {
            parent.startActivity(myIntent);
        }
    }

    public void clickSearchButton() {
        Intent intent = new Intent(parent, SearchActivity.class);
        if (parent != null) {
            parent.startActivity(intent);
        }
    }

    public void clickSettingsButton() {
        Intent intent = new Intent(parent, SettingsActivity.class);
        if (parent != null) {
            parent.startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.clear();
            onMapReady(map);
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

