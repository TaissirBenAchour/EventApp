package com.example.cassio.Graduation_Project.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ParentList extends ExpandableGroup<EventClass> {



    public ParentList(String title, List<EventClass> items) {
        super(title, items);
    }

}