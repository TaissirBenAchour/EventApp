package com.example.cassio.Graduation_Project.AccountFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SingleEventPostActivity;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.ParentList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;


public class AvailableEventFragment extends Fragment {
    DatabaseReference parentReference;
    private FirebaseAuth mAuth;
    private String my_id;
    private Bundle bundle = new Bundle();
    private RecyclerView recycler_view;
    private View mView;
    private EditText searchtxt;
    private ImageButton searchbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_available_event, container, false);

        recycler_view = (RecyclerView) mView.findViewById(R.id.list_events_id);
        searchtxt = (EditText) mView.findViewById(R.id.search_id);
        searchbtn = (ImageButton) mView.findViewById(R.id.search_event_button);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(layoutManager);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();

        parentReference = database.getReference().child("Events");
        final DatabaseReference parentReferenceusers = database.getReference().child("Users");
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_txt = searchtxt.getText().toString();
                if (TextUtils.isEmpty(search_txt)) {
                    Toast.makeText(getContext(), "search field is empty ", Toast.LENGTH_SHORT).show();
                } else {
                    searchEvent(search_txt);

                }

            }
        });
        parentReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ParentList> Parent = new ArrayList<>();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String parents = snapshot.getKey();

                    parentReference.child(parents).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final List<EventClass> Child = new ArrayList<>();
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                parentReference.child(parents).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {

                                        String desc = dataSnapshot.child("description").getValue().toString();
                                        String date = dataSnapshot.child("date").getValue().toString();
                                        String eventId = dataSnapshot.child("eventId").getValue().toString();
                                        String pushId = dataSnapshot.child("pushId").getValue().toString();
                                        String month = dataSnapshot.child("month").getValue().toString();
                                        String image = dataSnapshot.child("imageEvent").getValue().toString();


                                        bundle.putString("push_id", snapshot.getKey());
                                        bundle.putString("event_id", parents);
                                        bundle.putString("month", month);
                                        bundle.putString("month", dataSnapshot.child("title").getValue().toString());

                                        Child.add(new EventClass(dataSnapshot.child("title").getValue().toString(), desc, date, eventId, pushId, month, image));


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

                                    if (!parents.equals(my_id)) {
                                        Parent.add(new ParentList(name, Child));
                                        DocExpandableRecyclerAdapter adapter = new DocExpandableRecyclerAdapter(Parent);
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
        return mView;

    }

    private void searchEvent(final String search_txt) {

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Query query = ref.child("Events").child(dataSnapshot.getKey()).orderByChild("title").startAt(search_txt);
                    FirebaseRecyclerAdapter<EventClass, eventViewHolder> firebaseRecyclerAdapter
                            = new FirebaseRecyclerAdapter<EventClass, eventViewHolder>
                            (
                                    EventClass.class,
                                    R.layout.post_layout,
                                    eventViewHolder.class,
                                    query
                            ) {
                        @Override
                        protected void populateViewHolder(eventViewHolder viewHolder, EventClass model, final int position) {
                            String test = getRef(position).getKey();
                            viewHolder.setTitle(test);
                        }


                    };
                    recycler_view.setAdapter(firebaseRecyclerAdapter);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static class eventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public eventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView _title = (TextView) mView.findViewById(R.id.post_txt);
            _title.setText(title);
        }
    }

    public class DocExpandableRecyclerAdapter extends ExpandableRecyclerViewAdapter<MyParentViewHolder, MyChildViewHolder> {


        public DocExpandableRecyclerAdapter(List<ParentList> groups) {
            super(groups);
        }

        @Override
        public MyParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_events_item_parent, parent, false);
            return new MyParentViewHolder(view);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_event_layout, parent, false);

            return new MyChildViewHolder(view);
        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {

            final EventClass childItem = ((ParentList) group).getItems().get(childIndex);

            holder.onBind(childItem.getTitle(), childItem.getDescription(), childItem.getDate(), childItem.getImageEvent(), getContext());
            //   Toast.makeText(getContext(), childItem.getTitle(), Toast.LENGTH_SHORT).show();

            final String pushId = childItem.getEventId();
            final String eventId = childItem.getPushId();
            final String month = childItem.getMonth();
            final String title = childItem.getTitle();
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    parentReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                bundle.putString("push_id", pushId);
                                bundle.putString("event_id", eventId);
                                bundle.putString("month", month);
                                bundle.putString("title", title);


                                Intent intent = new Intent(getContext(), SingleEventPostActivity.class);
                                intent.putExtras(bundle);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            });

        }

        @Override
        public void onBindGroupViewHolder(MyParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
            holder.setParentTitle(group);


            if (group.getItems() == null) {
                holder.listGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        }


    }

    public class MyChildViewHolder extends ChildViewHolder {

        TextView listChild, desc, date;
        ImageView _image;

        public MyChildViewHolder(View itemView) {
            super(itemView);
            listChild = (TextView) itemView.findViewById(R.id.listChild);
            desc = (TextView) itemView.findViewById(R.id.desc_field_id);
            date = (TextView) itemView.findViewById(R.id.time_field_id);
            _image = (ImageView) itemView.findViewById(R.id.event_image_holder);


        }

        public void onBind(String Sousdoc, String _desc, String _date, String image, Context ctx) {
            listChild.setText(Sousdoc);
            desc.setText(_desc);
            date.setText(_date);
            Picasso.with(ctx).load(image).placeholder(R.drawable.profile_pic).into(_image);


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