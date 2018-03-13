package com.example.cassio.Graduation_Project.AppealFragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.MessagesActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.Appeals;
import com.example.cassio.Graduation_Project.models.Committee;
import com.example.cassio.Graduation_Project.models.ParentCommitteeList;
import com.example.cassio.Graduation_Project.models.ParentEventList;
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

public class AppealsList extends android.support.v4.app.Fragment {
    DatabaseReference parentReference, parentReference_com, parentReferenceusers;
    View mView;
    private FirebaseAuth mAuth;
    private String my_id;
    private Bundle bundle = new Bundle();
    private RecyclerView recycler_view_event, recycler_view_committee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_appeals_list, container, false);


        recycler_view_event = (RecyclerView) mView.findViewById(R.id.listAppeals_id);
        recycler_view_committee = (RecyclerView) mView.findViewById(R.id.listcommitteeAppeal_id);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recycler_view_event.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recycler_view_committee.setLayoutManager(layoutManager2);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        parentReference = database.getReference().child("Appeals");
        parentReference_com = database.getReference().child("AppealsCommittee");
        parentReferenceusers = database.getReference().child("Users");
        parentReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentEventList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String parents = snapshot.getKey();

                    parentReference.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot2) {
                            final List<Appeals> Child = new ArrayList<>();
                            parentReferenceusers.child(parents).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String name = dataSnapshot.child("userName").getValue().toString();
                                    final String id = dataSnapshot.child("userId").getValue().toString();


                                    if (!parents.equals(my_id)) {
                                        Parent.add(new ParentEventList(name, Child));
                                        DocExpandableRecyclerAdapter adapter1 = new DocExpandableRecyclerAdapter(Parent);


                                        for (final DataSnapshot snapshot : dataSnapshot2.getChildren()) {
                                            parentReference.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(final DataSnapshot dataSnapshot1) {


                                                    String vision = dataSnapshot1.child("vision").getValue().toString();
                                                    String content = dataSnapshot1.child("content").getValue().toString();
                                                    String address = dataSnapshot1.child("address").getValue().toString();

                                                    Child.add(new Appeals(vision, content, address, name,id));


                                                }


                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {


                                                }
                                            });


                                        }
                                        recycler_view_event.setAdapter(adapter1);


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
        parentReference_com.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentCommitteeList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String parents = snapshot.getKey();

                    parentReference_com.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot1) {
                            final List<Committee> Child = new ArrayList<>();
                            parentReferenceusers.child(parents).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String name = dataSnapshot.child("userName").getValue().toString();
                                    final String id = dataSnapshot.child("userId").getValue().toString();

                                    for (final DataSnapshot snapshot : dataSnapshot1.getChildren()) {
                                parentReference_com.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {


                                        String idea = dataSnapshot.child("idea").getValue().toString();
                                        String topic = dataSnapshot.child("topic").getValue().toString();
                                        String where = dataSnapshot.child("where").getValue().toString();
                                        String money = dataSnapshot.child("money").getValue().toString();
                                        String profile = dataSnapshot.child("profile").getValue().toString();


                                        Child.add(new Committee(topic, idea, where, money, profile,name,id));


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {


                                    }
                                });


                            }

                                    if (!parents.equals(my_id)) {
                                        Parent.add(new ParentCommitteeList(name, Child));
                                        DocExpandableRecyclerAdapter_com adapter1 = new DocExpandableRecyclerAdapter_com(Parent);


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

        return mView;
    }


    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder, MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<ParentEventList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_appeal_parent_item, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_event, parent, false);

            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final Appeals childItem = ((ParentEventList) group).getItems().get(childIndex);

            holder.onBind(childItem.getVision(), childItem.getContent(), childItem.getAddress());
             final String name = childItem.getName();
             final String id = childItem.getId();
            holder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                    bundle.putString("user_name", name);
                    bundle.putString("targed_person_id", id);


                    Intent intent = new Intent(getContext(), MessagesActivity.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if (group.getItems() == null) {
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }


    }

    public class MyChildViewHolder extends ChildViewHolder {

        public TextView text;
        public ImageButton message;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.txt_id);
            message = (ImageButton) itemView.findViewById(R.id.message_appeal_event);
        }

        public void onBind(String vision, String _content, String _address) {
            String paragraph = "This is an appeal for organizing a " + vision + " event , precisely " + _content + " in " + _address;
            Spannable spannable = new SpannableString(paragraph);

            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background)), ("This is an appeal for organizing a ").length(), ("This is an appeal for organizing a " + vision).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.layout1)), ("This is an appeal for organizing a " + vision + " event , precisely ").length(), ("This is an appeal for organizing a " + vision + " event , precisely " + _content).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dot_dark_screen2)), ("This is an appeal for organizing a " + vision + " event , precisely " + _content + " in ").length(), (paragraph).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(spannable, TextView.BufferType.SPANNABLE);
            message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentReference.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String parents = snapshot.getKey();
                                parentReferenceusers.child(parents).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("userName").getValue().toString();
                                        if (!parents.equals(my_id)) {
                                            Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();

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
            });


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


    public class DocExpandableRecyclerAdapter_com extends ExpandableRecyclerViewAdapter<MyParentcomViewHolder, MyChildcomViewHolder> {


        public DocExpandableRecyclerAdapter_com(List<ParentCommitteeList> groups) {
            super(groups);
        }

        @Override
        public MyParentcomViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.committee_appeal_parent_item, parent, false);
            return new MyParentcomViewHolder(view);
        }

        @Override
        public MyChildcomViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal_com, parent, false);

            return new MyChildcomViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildcomViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final Committee childItem = ((ParentCommitteeList) group).getItems().get(childIndex);

            holder.onBind(childItem.getTopic(), childItem.getIdea(), childItem.getWhere(), childItem.getMoney(), childItem.getProfile());
            final String name = childItem.getName();
            final String id = childItem.getId();
            holder.message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                    bundle.putString("user_name", name);
                    bundle.putString("targed_person_id", id);


                    Intent intent = new Intent(getContext(), MessagesActivity.class);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        @Override
        public void onBindGroupViewHolder(MyParentcomViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);

            if (group.getItems() == null) {
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }


    }

    public class MyChildcomViewHolder extends ChildViewHolder {

        public TextView test;
        public ImageButton message;

        public MyChildcomViewHolder(View itemView) {
            super(itemView);
            test = (TextView) itemView.findViewById(R.id.txt_id);
            message = (ImageButton) itemView.findViewById(R.id.message_com_id);



        }

        public void onBind(String topic, String idea, String where, String money, String profile) {
            String paragraph = "I plan for " + topic + " event , mainly " + idea + " in " + where + " , " + money + " with the profile of " + profile;
            Spannable spannable = new SpannableString(paragraph);

            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.background)), "I plan for ".length(), ("I plan for " + topic).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)), ("I plan for " + topic + "event, mainly ").length(), ("I plan for " + topic + " event, mainly " + idea).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dot_dark_screen2)), ("I plan for " + topic + " event, mainly " + idea + " in ").length(), ("I plan for " + topic + " event, mainly " + idea + " in " + where).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.layout1)), ("I plan for " + topic + " event, mainly " + idea + " in " + where + ", ").length(), ("I plan for " + topic + " event, mainly " + idea + " in " + where + ", " + money).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.layout7)), ("I plan for " + topic + " event, mainly " + idea + " in " + where + ", " + money + " with the profile of ").length(), (paragraph).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            test.setText(spannable, TextView.BufferType.SPANNABLE);

        }


    }

    public class MyParentcomViewHolder extends GroupViewHolder {

        public TextView name;
        private Typeface newType;

        public MyParentcomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.listParent1);
        }

        public void setParentTitle(ExpandableGroup group) {
            name.setText(group.getTitle());
        }


    }


}