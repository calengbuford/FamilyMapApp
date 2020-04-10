package com.example.familymap.client;

import com.example.shared.model_.Event;
import com.example.shared.model_.Person;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    public List<Person> getPersonsChildren(Person[] family, String personID) {
        List<Person> children = new ArrayList<Person>();

        // Check every member of the family to find children
        for (Person person : family) {
            if (personID.equals(person.getMotherID()) || personID.equals(person.getFatherID())) {
                children.add(person);
            }
        }
        return children;
    }



}
