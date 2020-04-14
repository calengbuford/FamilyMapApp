package com.example.familymap.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymap.R;
import com.example.familymap.model.Client;
import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        client = Client.getInstance();

        // Connect with view
        final EditText searchQuery = (EditText) findViewById(R.id.searchQuery);
        ImageView xIcon = (ImageView) findViewById(R.id.xIcon);


        // xIcon Listener
        xIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchQuery.setText("");
            }
        });

        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // Get subset of lists
                List<Person> subsetPersons = getSubsetPersons(s);
                List<Event> subsetEvent = getSubsetEvents(s);

                // Set up recyclerView
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                // Set adaptor
                PersonEventAdapter adapter = new PersonEventAdapter(subsetPersons, subsetEvent);
                recyclerView.setAdapter(adapter);
            }

            private List<Person> getSubsetPersons(CharSequence substring) {
                // Look for substring in each persons first and last name
                List<Person> subsetPersons = new ArrayList<Person>();

                // If substring is the empty string, return an empty list
                if (!"".contentEquals(substring)) {
                    Person[] userFamily = client.getUsersFamily();
                    for (Person person : userFamily) {
                        if (person.getFirstName().toLowerCase().contains(substring) ||
                                person.getLastName().toLowerCase().contains(substring)) {
                            subsetPersons.add(person);
                        }
                    }
                }
                return subsetPersons;
            }

            private List<Event> getSubsetEvents(CharSequence substring) {
                // Look for substring in each events country, city, event type, and year
                List<Event> subsetEvent = new ArrayList<Event>();

                // If substring is the empty string, return an empty list
                if (!"".contentEquals(substring)) {
                    List<Event> familyEvents = client.getFilteredFamilyEvents();
                    for (Event event : familyEvents) {
                        if (event.getCountry().toLowerCase().contains(substring) ||
                                event.getCity().toLowerCase().contains(substring) ||
                                event.getEventType().toLowerCase().contains(substring) ||
                                Integer.toString(event.getYear()).toLowerCase().contains(substring)) {
                            subsetEvent.add(event);
                        }
                    }
                }
                return subsetEvent;
            }
        });

    }

    private class PersonEventAdapter extends RecyclerView.Adapter<PersonEventViewHolder> {
        private final List<Person> usersFamily;
        private final List<Event> familyEvents;


        PersonEventAdapter( List<Person> usersFamily, List<Event> familyEvents) {
            this.usersFamily = usersFamily;
            this.familyEvents = familyEvents;
        }

        @Override
        public int getItemViewType(int position) {
            return position < usersFamily.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public PersonEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new PersonEventViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull PersonEventViewHolder holder, int position) {
            if(position < usersFamily.size()) {
                holder.bind(usersFamily.get(position));
            } else {
                holder.bind(familyEvents.get(position - usersFamily.size()));
            }
        }

        @Override
        public int getItemCount() {
            return usersFamily.size() + familyEvents.size();
        }
    }

    private class PersonEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView primaryText;
        private final TextView infoText;
        private final ImageView imageView;
        private final TextView subText;

        private final int viewType;
        private Person person;
        private Event event;

        PersonEventViewHolder(View view, int viewType) {
            // Connect with view
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                primaryText = itemView.findViewById(R.id.personName);
                infoText = itemView.findViewById(R.id.personTitle);
                subText = null;
            } else {
                primaryText = itemView.findViewById(R.id.eventType);
                infoText = itemView.findViewById(R.id.eventInfo);
                subText = itemView.findViewById(R.id.eventPersonName);
            }
            imageView = itemView.findViewById(R.id.genderImg);
        }

        private void bind(Person person) {
            // Update fields in view
            this.person = person;

            // Set name
            String nameString = person.getFirstName() + " " + person.getLastName();
            primaryText.setText(nameString);

            // Set gender icon
            if ("m".equals(person.getGender())) {
                imageView.setBackgroundResource(R.drawable.male_icon);
            }
            else {
                imageView.setBackgroundResource(R.drawable.female_icon);
            }
        }

        private void bind(Event event) {
            // Update fields in view
            this.event = event;

            // Set event type
            String eventTypeString = event.getEventType() + ": ";
            primaryText.setText(eventTypeString);

            // Set event info
            String eventInfoString = event.getCity() + ", " + event.getCountry() +
                    " (" + event.getYear() + ")";
            infoText.setText(eventInfoString);

            // Set the name of the person associated with the event
            Person person = client.getUserFamilyDict().get(event.getPersonID());
            if (person != null) {
                String nameString = person.getFirstName() + " " + person.getLastName();
                subText.setText(nameString);
            }
        }

        @Override
        public void onClick(View view) {
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                // Open this Person's information in a new Person Activity
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(String.valueOf(R.string.personID_intent), person.getPersonID());
                SearchActivity.this.startActivity(intent);
            } else {
                // Open event activity
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(String.valueOf(R.string.eventID_intent), event.getEventID());
                SearchActivity.this.startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Filter when user clicks the up button to return to map page
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}