package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.models.EventClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.intrusoft.sectionedrecyclerview.Section;
import com.intrusoft.sectionedrecyclerview.SectionRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class testCalendar extends AppCompatActivity {
    DatabaseReference parentReference, parentReferenc_events;
    AdapterSectionRecycler adapterRecycler;
    private FirebaseAuth mAuth;
    private String my_id;
    private Bundle bundle = new Bundle();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_layout);
        recyclerView = (RecyclerView) findViewById(R.id.listofsavedevents);


        parentReference = FirebaseDatabase.getInstance().getReference().child("SavedEvents");
        parentReferenc_events = FirebaseDatabase.getInstance().getReference().child("Events");
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        parentReference.addValueEventListener(new ValueEventListener() {
            List<SectionHeader> sections = new ArrayList<>();


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(my_id).exists()) {
                    final List<EventClass>[] childList = new List[]{new ArrayList<>()};


                    if (dataSnapshot.child(my_id).child("January").exists()) {
                        parentReference.child(my_id).child("January").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    String string = snapshot.child("title").getValue().toString();

                                    childList[0].add(new EventClass(string));


                                }

                                sections.add(new SectionHeader(childList[0], "January"));
                                adapterRecycler = new AdapterSectionRecycler(testCalendar.this, sections);
                                recyclerView.setAdapter(adapterRecycler);

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                    if (dataSnapshot.child(my_id).child("March").exists()) {
                        parentReference.child(my_id).child("March").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    childList[0] = new ArrayList<>();
                                    String string = snapshot.child("title").getValue().toString();

                                    childList[0].add(new EventClass(string));

                                }

                                sections.add(new SectionHeader(childList[0], "March"));

                                adapterRecycler = new AdapterSectionRecycler(testCalendar.this, sections);
                                recyclerView.setAdapter(adapterRecycler);

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }
                    if (dataSnapshot.child(my_id).child("February").exists()) {
                        parentReference.child(my_id).child("February").addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    childList[0] = new ArrayList<>();

                                    String string = snapshot.getKey();
                                    childList[0].add(new EventClass(string));

                                }

                                sections.add(new SectionHeader(childList[0], "C"));

                                adapterRecycler = new AdapterSectionRecycler(testCalendar.this, sections);
                                recyclerView.setAdapter(adapterRecycler);

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


    }


    public class AdapterSectionRecycler extends SectionRecyclerViewAdapter<SectionHeader, EventClass, SectionViewHolder, ChildViewHolder> {

        Context context;

        public AdapterSectionRecycler(Context context, List<SectionHeader> sectionItemList) {
            super(context, sectionItemList);
            this.context = context;
        }

        @Override
        public SectionViewHolder onCreateSectionViewHolder(ViewGroup sectionViewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_agenda_layout_parent, sectionViewGroup, false);
            return new SectionViewHolder(view);
        }

        @Override
        public ChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_agenda_layout_child, childViewGroup, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindSectionViewHolder(SectionViewHolder sectionViewHolder, int sectionPosition, SectionHeader section) {
            sectionViewHolder.name.setText(section.sectionText);
        }

        @Override
        public void onBindChildViewHolder(ChildViewHolder childViewHolder, int sectionPosition, int childPosition, EventClass child) {
            childViewHolder.name.setText(child.getTitle());
        }
    }

    public class SectionHeader implements Section<EventClass> {

        List<EventClass> childList;
        String sectionText;

        public SectionHeader(List<EventClass> childList, String sectionText) {
            this.childList = childList;
            this.sectionText = sectionText;
        }

        @Override
        public List<EventClass> getChildItems() {
            return childList;
        }

        public String getSectionText() {
            return sectionText;
        }
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public SectionViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.listparent_id);
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ChildViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title_event_id);
        }
    }

}
