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

import com.example.cassio.Graduation_Project.models.Appeals;
import com.example.cassio.Graduation_Project.models.Committee;
import com.example.cassio.Graduation_Project.models.ParentList;
import com.example.cassio.Graduation_Project.models.ParentList2;
import com.example.cassio.Graduation_Project.models.ParentList3;
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

public class AppealsList extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String my_id;
    private  Bundle bundle = new Bundle();
    DatabaseReference parentReference,parentReference_com;

    private RecyclerView recycler_view_event,recycler_view_committee ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeals_list);


        //Define recycleview
        recycler_view_event = (RecyclerView) findViewById(R.id.listAppeals_id);
        recycler_view_committee = (RecyclerView) findViewById(R.id.listcommitteeAppeal_id);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);

       recycler_view_event.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        recycler_view_committee.setLayoutManager(layoutManager2);

        //Initialize your Firebase app
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        my_id=mAuth.getCurrentUser().getUid();

        // Reference to your entire Firebase database
        parentReference = database.getReference().child("Appeals");
        parentReference_com = database.getReference().child("CommitteeAppeal");
        final DatabaseReference parentReferenceusers = database.getReference().child("Users");

        //reading data from firebase
        parentReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList2> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String parents =snapshot.getKey();

                    parentReference.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<Appeals> Child = new ArrayList<>();
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                parentReference.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                        final String ChildValue =  dataSnapshot.getValue().toString();



                                        String vision = dataSnapshot.child("vision").getValue().toString();
                                        String content = dataSnapshot.child("content").getValue().toString();
                                        String address = dataSnapshot.child("address").getValue().toString();

                                        Child.add(new Appeals(vision,content,address));

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
                                        Parent.add(new ParentList2(name, Child));
                                        DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);

                                        Toast.makeText(AppealsList.this,dataSnapshot.getChildren().toString() , Toast.LENGTH_SHORT).show();




                                        recycler_view_event.setAdapter(adapter);



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
        //reading data from firebase
        parentReference_com.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList3> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String parents =snapshot.getKey();
                    Toast.makeText(AppealsList.this, parents, Toast.LENGTH_SHORT).show();

                    parentReference_com.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<Committee> Child = new ArrayList<>();
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                parentReference_com.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {



                                        String test = dataSnapshot.child("test").getValue().toString();
                                        Toast.makeText(AppealsList.this,test, Toast.LENGTH_SHORT).show();


                                      Child.add(new Committee(test));








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
                                    Toast.makeText(AppealsList.this, name, Toast.LENGTH_SHORT).show();

                                    //   Toast.makeText(testlistevents.this, name, Toast.LENGTH_SHORT).show();
                                    if (!parents.equals(my_id)){
                                        Parent.add(new ParentList3(name, Child));
                                        DocExpandableRecyclerAdapter_com adapter1 = new DocExpandableRecyclerAdapter_com(Parent);

                                        Toast.makeText(AppealsList.this,dataSnapshot.getChildren().toString() , Toast.LENGTH_SHORT).show();




                                        recycler_view_committee.setAdapter(adapter1);



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


        public DocExpandableRecyclerAdapter(List<ParentList2> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent2_item, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal, parent, false);

            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final Appeals childItem = ((ParentList2) group).getItems().get(childIndex);

            holder.onBind(childItem.getVision(),childItem.getContent(),childItem.getAddress());

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.title.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }


    }
    public class DocExpandableRecyclerAdapter_com extends ExpandableRecyclerViewAdapter<MyParentcomViewHolder,MyChildcomViewHolder> {


        public DocExpandableRecyclerAdapter_com(List<ParentList3> groups) {
            super(groups);
        }

        @Override
        public MyParentcomViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent3_item, parent, false);
            return new MyParentcomViewHolder(view);
        }

        @Override
        public MyChildcomViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_com, parent, false);

            return new MyChildcomViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildcomViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final Committee childItem = ((ParentList3) group).getItems().get(childIndex);

            holder.onBind(childItem.getTest());

        }

        @Override
        public void onBindGroupViewHolder(MyParentcomViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if(group.getItems()==null)
            {
                holder.title.setOnClickListener(  new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }


    }


    public class MyChildViewHolder extends ChildViewHolder {

        public TextView vision,content,address;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            vision = (TextView) itemView.findViewById(R.id.txtvision_id);
            content = (TextView) itemView.findViewById(R.id.txtcontent_id);
            address = (TextView) itemView.findViewById(R.id.txtaddress_id);




        }

        public void onBind(String Sousdoc , String _content , String _address) {
            vision.setText(Sousdoc);
            content.setText(_content);
            address.setText(_address);



        }



    }
    public class MyChildcomViewHolder extends ChildViewHolder {

        public TextView test;

        public MyChildcomViewHolder(View itemView) {
            super(itemView);
            test = (TextView) itemView.findViewById(R.id.txt_id);





        }

        public void onBind(String Sousdoc ) {
            test.setText(Sousdoc);




        }



    }
    public class MyParentViewHolder extends GroupViewHolder {

        public TextView title;

        public MyParentViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listParent);


        }

        public void setParentTitle(ExpandableGroup group) {
            title.setText(group.getTitle());
        }


    }

    public class MyParentcomViewHolder extends GroupViewHolder {

        public TextView title;

        public MyParentcomViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listParent1);


        }

        public void setParentTitle(ExpandableGroup group) {
            title.setText(group.getTitle());
        }


    }




}