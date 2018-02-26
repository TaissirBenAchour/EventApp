package com.example.cassio.Graduation_Project.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ParentCommitteeList extends ExpandableGroup<Committee> {



    public ParentCommitteeList(String test, List<Committee> items) {
        super(test, items);
    }

}