package com.example.familymap.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.List;


public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private Client client;

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map2, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        client = Client.getInstance();
        float color;

        // Make the auto-focus marker the birth event of the user person
//        Person person = client.getPerson();
//        List<Event> eventList = client.getUserEventDict().get(person.getPersonID());
//        if (eventList != null) {
//            for (Event event : eventList) {
//                if ("birth".equals(event.getEventType())) {
//                    // Add the event to the map as a marker
//                    color = getEventMarkerColor(event.getEventType());
//                    LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
//                    Marker marker = map.addMarker(new MarkerOptions()
//                            .position(latLng)
//                            .icon(BitmapDescriptorFactory.defaultMarker(color))
//                            .title(event.getEventType()));
//                    marker.setTag(event);
//                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//                    break;
//                }
//            }
//        }

        // Add a marker in Park City and move the camera
//        LatLng parkCity = new LatLng(40.6461, -111.4980);
//        map.addMarker(new MarkerOptions().position(parkCity).title("Park City"));
//        map.animateCamera(CameraUpdateFactory.newLatLng(parkCity));

        Event[] familyEvents = client.getFamilyEvents();

        for (Event event : familyEvents) {
            color = getEventMarkerColor(event.getEventType());

            // Add the event to the map as a marker
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(new LatLng(event.getLatitude(), event.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .title(event.getEventType()));
            marker.setTag(event);
        }

        // TODO: MAKE EACH EVENT CLICKABLE AND INTERACTIVE
        // Set a listener for marker click.
        map.setOnMarkerClickListener(this);

    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    private float getEventMarkerColor(String eventType) {
        if ("birth".equals(eventType)) {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
        else if ("christening".equals(eventType)) {
            return BitmapDescriptorFactory.HUE_CYAN;
        }
        else if ("marriage".equals(eventType)) {
            return BitmapDescriptorFactory.HUE_ROSE;
        }
        else if ("death".equals(eventType)) {
            return BitmapDescriptorFactory.HUE_BLUE;
        }
        else {
            return BitmapDescriptorFactory.HUE_YELLOW;
        }
    }

}

