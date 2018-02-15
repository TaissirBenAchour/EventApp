
package com.example.cassio.Graduation_Project.models;

        import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

        import java.util.List;

public class parent_months_list extends ExpandableGroup<EventClass> {



    public parent_months_list(String month, List<EventClass> items) {
        super(month, items);
    }

}
