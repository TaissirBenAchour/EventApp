package com.example.cassio.Graduation_Project.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ParentEventList extends ExpandableGroup<Appeals> {



    public ParentEventList(String title, List<Appeals> items) {
        super(title, items);
    }

}