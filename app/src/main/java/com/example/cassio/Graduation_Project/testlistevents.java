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

public class testlistevents extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String my_id;
    private  Bundle bundle = new Bundle();
     DatabaseReference parentReference;



    private RecyclerView recycler_view;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_available_event);


            //Define recycleview
            recycler_view = (RecyclerView) findViewById(R.id.list_events_id);
            recycler_view.setLayoutManager(new LinearLayoutManager(this));

            //Initialize your Firebase app
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            mAuth=FirebaseAuth.getInstance();
            my_id=mAuth.getCurrentUser().getUid();

            // Reference to your entire Firebase database
           parentReference = database.getReference().child("Events");
            final DatabaseReference parentReferenceusers = database.getReference().child("Users");

            //reading data from firebase
            parentReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final List<ParentList> Parent = new ArrayList<>();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final String parents =snapshot.getKey();

                        parentReference.child(parents).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final List<EventClass> Child = new ArrayList<>();
                                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    parentReference.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            final String ChildValue =  dataSnapshot.getValue().toString();



                                          //  Child.add(new EventClass(ChildValue));
                                            String desc = dataSnapshot.child("description").getValue().toString();
                                            String date = dataSnapshot.child("date").getValue().toString();
                                            String eventId = dataSnapshot.child("eventId").getValue().toString();
                                            String pushId = dataSnapshot.child("pushId").getValue().toString();
                                            String month =  dataSnapshot.child("month").getValue().toString();



                                            bundle.putString("push_id",snapshot.getKey());
                                            bundle.putString("event_id",parents);







//                                            Toast.makeText(testlistevents.this, desc+"desc", Toast.LENGTH_SHORT).show();
//
                                            Child.add(new EventClass(dataSnapshot.child("title").getValue().toString(),desc,date,eventId,pushId,month));








                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {


                                        }
                                    });


                                }
                                parentReferenceusers.child(parents).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("userName").getValue().toString();

                                     //   Toast.makeText(testlistevents.this, name, Toast.LENGTH_SHORT).show();
                                        if (!parents.equals(my_id)){
                                            Parent.add(new ParentList(name, Child));
                                            DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                                            Toast.makeText(testlistevents.this,dataSnapshot.getChildren().toString() , Toast.LENGTH_SHORT).show();




                                            recycler_view.setAdapter(adapter);



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



        public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder,MyChildViewHolder> {


            public DocExpandableRecyclerAdapter(List<ParentList> groups) {
                super(groups);
            }

            @Override
            public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent, parent, false);
                return new MyParentViewHolder(view);
            }

            @Override
            public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_event_layout, parent, false);

                return new MyChildViewHolder(view);
            }

            @Override
            public void onBindChildViewHolder(final MyChildViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

                final EventClass childItem = ((ParentList) group).getItems().get(childIndex);

                holder.onBind(childItem.getTitle(),childItem.getDescription(),childItem.getDate(),childItem.getMonth());




                final String pushId = childItem.getEventId();
                final String eventId = childItem.getPushId();
                final String month = childItem.getMonth();
                holder.listChild.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        parentReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    //Toast.makeText(testlistevents.this, pushId, Toast.LENGTH_SHORT).show();
                                  // Toast.makeText(testlistevents.this, month, Toast.LENGTH_SHORT).show();
                                    bundle.putString("push_id",pushId);
                                    bundle.putString("event_id",eventId);
                                    bundle.putString("month",month);

                                    Intent intent = new Intent(testlistevents.this, SingleEventPostView.class);
                                    intent.putExtras(bundle);
                                   startActivity(intent);




                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });





//                                Toast toast = Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT);
//                                toast.show();




                       // Toast.makeText(testlistevents.this, bundle.toString(), Toast.LENGTH_SHORT).show();



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

                          //  Toast toast = Toast.makeText(getApplicationContext(), group.toString(), Toast.LENGTH_SHORT);
                           // toast.show();
                        }
                    });

                }
            }


        }



    public class MyChildViewHolder extends ChildViewHolder {

        public TextView listChild,desc,date,_month;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            listChild = (TextView) itemView.findViewById(R.id.listChild);
            desc = (TextView) itemView.findViewById(R.id.desc_field_id);
            date = (TextView) itemView.findViewById(R.id.time_field_id);
            _month = (TextView) itemView.findViewById(R.id.month_field_id);



        }

        public void onBind(String Sousdoc,String _desc,String _date,String eventId) {
            listChild.setText(Sousdoc);
            desc.setText(_desc);
            date.setText(_date);
            _month.setText(eventId);


        }



    }
    public class MyParentViewHolder extends GroupViewHolder {

        public TextView listGroup;

        public MyParentViewHolder(View itemView) {
            super(itemView);
            listGroup = (TextView) itemView.findViewById(R.id.listParent);
        }

        public void setParentTitle(ExpandableGroup group) {
            listGroup.setText(group.getTitle());
        }


    }




    }