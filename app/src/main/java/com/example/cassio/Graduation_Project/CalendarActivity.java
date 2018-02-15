package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.ParentList;
import com.example.cassio.Graduation_Project.models.parent_months_list;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cassio on 21/01/18.
 */

public class CalendarActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String my_id;
    private  Bundle bundle = new Bundle();
    DatabaseReference parentReference,parentReferenc_events;



    private RecyclerView recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);

        recycler_view = (RecyclerView) findViewById(R.id.listofsavedevents);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        my_id=mAuth.getCurrentUser().getUid();
        parentReference = database.getReference().child("SavedEvents");
        parentReferenc_events = database.getReference().child("Events");



        parentReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<parent_months_list> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String parents =snapshot.getKey();
                   // Toast.makeText(CalendarActivity.this, parents, Toast.LENGTH_SHORT).show();
                    if (my_id.equals(parents)){
                        parentReference.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final List<EventClass> Child = new ArrayList<>();

                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String months = snapshot.getKey();
                                Parent.add(new parent_months_list(months, Child));
                                DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                                recycler_view.setAdapter(adapter);
                                parentReference.child(parents).child(months).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {

                                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            final String  saved_event_id = snapshot.getKey();
                                          parentReference.child(parents).child(months).child(saved_event_id).addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                  final String  month_in_saved_event = dataSnapshot.child("month").getValue().toString();
                                                  parentReferenc_events.addValueEventListener(new ValueEventListener() {
                                                      @Override
                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                          for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                              final String events = snapshot.getKey();
                                                              parentReferenc_events.child(events).addValueEventListener(new ValueEventListener() {
                                                                  @Override
                                                                  public void onDataChange(DataSnapshot dataSnapshot) {
                                                                      for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                          final String events_id = snapshot.getKey();
                                                                          //         Toast.makeText(CalendarActivity.this, events_id, Toast.LENGTH_SHORT).show();

                                                                          parentReferenc_events.child(events).child(events_id).addValueEventListener(new ValueEventListener() {
                                                                              @Override
                                                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                  String month = dataSnapshot.child("month").getValue().toString();
                                                                                  String title = dataSnapshot.child("title").getValue().toString();

                                                                                  while (saved_event_id.equals(events_id)) {
                                                                                      Toast.makeText(CalendarActivity.this, events_id, Toast.LENGTH_SHORT).show();
                                                                                      if (month_in_saved_event.equals(month)){

                                                                                              Toast.makeText(CalendarActivity.this, month, Toast.LENGTH_SHORT).show();
                                                                                          Child.add(new EventClass(title));

                                                                                      }


                                                                                  }




                                                                              }

                                                                              @Override
                                                                              public void onCancelled(DatabaseError databaseError) {

                                                                              }
                                                                          });



                                                                      }
                                                                  }

                                                                  @Override
                                                                  public void onCancelled(DatabaseError databaseError) {

                                                                  }
                                                              });
                                                          }
                                                      }

                                                      @Override
                                                      public void onCancelled(DatabaseError databaseError) {

                                                      }
                                                  });


                                              }

                                              @Override
                                              public void onCancelled(DatabaseError databaseError) {

                                              }
                                          });



                                        }



                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {


                                    }
                                });


                            }











                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });}





                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<parent_months_list> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_layout_parent, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_layout_child, parent, false);

            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final EventClass childItem = ((parent_months_list) group).getItems().get(childIndex);

            holder.onBind(childItem.getTitle());
          //  Toast.makeText(CalendarActivity.this, childItem.getTitle(), Toast.LENGTH_SHORT).show();

            holder._title.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {





                }

            });

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.listGroup.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });

            }
        }


    }

    public class MyChildViewHolder extends ChildViewHolder {

        public TextView _title;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            _title = (TextView) itemView.findViewById(R.id.title_event_id);




        }

        public void onBind(String title) {
            _title.setText(title);


        }



    }
    public class MyParentViewHolder extends GroupViewHolder {

        public TextView listGroup;

        public MyParentViewHolder(View itemView) {
            super(itemView);
            listGroup = (TextView) itemView.findViewById(R.id.listparent_id);
        }

        public void setParentTitle(ExpandableGroup group) {
            listGroup.setText(group.getTitle());
        }


    }




}