package com.example.familymap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String personID = intent.getStringExtra(String.valueOf(R.string.personID_intent));
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

                // Get sorted life events for person
                List<Event> events = client.getLifeEvents(personID);

                // Get direct family of person
                Filter filter = new Filter();
                List<Person> children = filter.getPersonsChildren(client.getUsersFamily(), personID);
                Person father = client.getUserFamilyDict().get(person.getFatherID());
                Person mother = client.getUserFamilyDict().get(person.getMotherID());
                Person spouse = client.getUserFamilyDict().get(person.getSpouseID());

                // Add all family to one list and create a dictionary with indices mapping to titles
                List<Person> directFamily = new ArrayList<Person>();
                HashMap<Integer, String> familyTitles = new HashMap<Integer, String>();
                int counter = 0;
                if (father != null) {
                    directFamily.add(father);
                    familyTitles.put(counter, "Father");
                    counter++;
                }
                if (mother != null) {
                    directFamily.add(mother);
                    familyTitles.put(counter, "Mother");
                    counter++;
                }
                if (spouse != null) {
                    directFamily.add(spouse);
                    familyTitles.put(counter, "Spouse");
                    counter++;
                }

                for (int i = 0; i < children.size(); i++) {
                    directFamily.add(children.get(i));
                    familyTitles.put(i + counter, "Child");
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
            // Connect with view and update fields
            TextView eventTypeText = eventsView.findViewById(R.id.eventType);
            String eventTypeString = events.get(childPosition).getEventType() + ": ";
            eventTypeText.setText(eventTypeString);

            TextView eventInfoText = eventsView.findViewById(R.id.eventInfo);
            String eventInfoString = events.get(childPosition).getCity() + ", " + events.get(childPosition).getCountry() +
                    " (" + events.get(childPosition).getYear() + ")";
            eventInfoText.setText(eventInfoString);

            TextView nameText = eventsView.findViewById(R.id.eventPersonName);
            Person person = client.getUserFamilyDict().get(events.get(childPosition).getPersonID());
            if (person != null) {
                String nameString = person.getFirstName() + " " + person.getLastName();
                nameText.setText(nameString);
            }

            eventsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickEvent(events.get(childPosition).getEventID());
                }
            });
        }

        private void initializeDirectFamilyView(View directFamilyView, final int childPosition) {
            // Connect with view and update fields
            TextView nameText = directFamilyView.findViewById(R.id.directFamilyName);
            String nameString = directFamily.get(childPosition).getFirstName() + " " + directFamily.get(childPosition).getLastName();
            nameText.setText(nameString);

            TextView titleText = directFamilyView.findViewById(R.id.directFamilyTitle);
            String titleString = familyTitles.get(childPosition);
            titleText.setText(titleString);

            ImageView imageView = (ImageView) directFamilyView.findViewById(R.id.genderImg);
            if ("m".equals(directFamily.get(childPosition).getGender())) {
                imageView.setBackgroundResource(R.drawable.male_icon);
            }
            else {
                imageView.setBackgroundResource(R.drawable.female_icon);
            }

            directFamilyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickFamilyMember(directFamily.get(childPosition).getPersonID());
                }
            });
        }

        private void clickFamilyMember(String personID) {
            // Open this Person's information in a new Person Activity
            Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
            intent.putExtra(String.valueOf(R.string.personID_intent), personID);
            PersonActivity.this.startActivity(intent);
        }

        private void clickEvent(String eventID) {
            // Open event activity, which is the same Map Fragment in the main activity.
            Intent intent = new Intent(PersonActivity.this, MainActivity.class);
            intent.putExtra(String.valueOf(R.string.eventID_intent), eventID);
            PersonActivity.this.startActivity(intent);

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
