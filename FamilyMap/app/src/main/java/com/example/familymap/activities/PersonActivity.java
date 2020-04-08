package com.example.familymap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familymap.R;
import com.example.familymap.client.Client;
import com.example.familymap.client.Filter;
import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonActivity extends AppCompatActivity {
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra("personID");
        client = Client.getInstance();

        try {
            Person person = client.getUserFamilyDict().get(personID);

            if (person != null) {
                TextView firstNameText = (TextView) findViewById(R.id.personFirstName);
                firstNameText.setText(person.getFirstName());
                TextView lastNameText = (TextView) findViewById(R.id.personLastName);
                lastNameText.setText(person.getLastName());
                TextView genderText = (TextView) findViewById(R.id.personGender);
                String genderString;
                if ("m".equals(person.getGender())) {
                    genderString = "Male";
                }
                else {
                    genderString = "Female";
                }
                genderText.setText(genderString);

                ExpandableListView expandableListView = findViewById(R.id.expandableListView);

                List<Event> events = client.getUserEventDict().get(personID);   // Get event for person

                // Get direct family of person
                Filter filter = new Filter();
                List<Person> children = filter.getPersonsChildren(client.getUsersFamily(), personID);
                Person father = client.getUserFamilyDict().get(person.getFatherID());
                Person mother = client.getUserFamilyDict().get(person.getMotherID());
                Person spouse = client.getUserFamilyDict().get(person.getSpouseID());

                List<Person> directFamily = new ArrayList<Person>();
                HashMap<Integer, String> familyTitles = new HashMap<Integer, String>();
                directFamily.add(father);
                familyTitles.put(0, "Father");
                directFamily.add(mother);
                familyTitles.put(1, "Mother");
                directFamily.add(spouse);
                familyTitles.put(2, "Spouse");
                for (int i = 0; i < children.size(); i++) {
                    directFamily.add(children.get(i));
                    familyTitles.put(i + directFamily.size(), "Child");
                }

                expandableListView.setAdapter(new ExpandableListAdapter(events, directFamily, familyTitles));

            } else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final List<Event> events;
        private final List<Person> directFamily;
        private final HashMap<Integer, String> familyTitles;

        ExpandableListAdapter(List<Event> events, List<Person> directFamily, HashMap<Integer, String> familyTitles) {
            this.events = events;
            this.directFamily = directFamily;
            this.familyTitles = familyTitles;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENTS_GROUP_POSITION:
                    return events.size();
                case FAMILY_GROUP_POSITION:
                    return directFamily.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENTS_GROUP_POSITION:
                    return getString(R.string.life_event_list);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.direct_family_list);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENTS_GROUP_POSITION:
                    return events.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return directFamily.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENTS_GROUP_POSITION:
                    titleView.setText(R.string.life_event_list);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.direct_family_list);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventsView(itemView, childPosition);
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.direct_family_item, parent, false);
                    initializeDirectFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventsView(View eventsView, final int childPosition) {
            TextView eventTypeText = eventsView.findViewById(R.id.eventType);
            String eventTypeString = events.get(childPosition).getEventType() + ": ";
            eventTypeText.setText(eventTypeString);

            TextView eventInfoText = eventsView.findViewById(R.id.eventInfo);
            String eventInfoString = events.get(childPosition).getCity() + ", " + events.get(childPosition).getCountry() +
                    " (" + events.get(childPosition).getYear() + ")";
            eventInfoText.setText(eventInfoString);

            TextView nameText = eventsView.findViewById(R.id.eventPersonName);
            String nameString = client.getUserFamilyDict().get(events.get(childPosition).getPersonID()).getFirstName() + " " +
                    client.getUserFamilyDict().get(events.get(childPosition).getPersonID()).getLastName();
            nameText.setText(nameString);

            eventsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Open Event Activity
                }
            });
        }

        private void initializeDirectFamilyView(View directFamilyView, final int childPosition) {
            TextView nameText = directFamilyView.findViewById(R.id.directFamilyName);
            String nameString = directFamily.get(childPosition).getFirstName() + " " + directFamily.get(childPosition).getLastName();
            nameText.setText(nameString);

            TextView titleText = directFamilyView.findViewById(R.id.directFamilyTitle);
            String titleString = familyTitles.get(childPosition);
            titleText.setText(titleString);

            directFamilyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Open this Person's information
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
