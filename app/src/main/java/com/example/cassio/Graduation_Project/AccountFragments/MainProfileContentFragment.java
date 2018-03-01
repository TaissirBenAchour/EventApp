package com.example.cassio.Graduation_Project.AccountFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.AddEventActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SingleEventPostActivity;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainProfileContentFragment extends Fragment {

    private static final String TAG = "MainProfileContentFragm";
    float i = 0;
    CircleImageView imageProfile;
    private View view;
    private String my_id, eventPost;
    private RecyclerView listOfposts, listofeventposts;
    private DatabaseReference UsersRef, eventsRef, rateRef, postRef;
    private FirebaseAuth mAuth;
    private TextView username, userstatus, rated, numevents;
    private ImageButton addevent;
    private RatingBar ratingbar;
    private Bundle bundle = new Bundle();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_header_layout, null);


        username = (TextView) view.findViewById(R.id.username_id);
        userstatus = (TextView) view.findViewById(R.id.userstatus_id);
        addevent = (ImageButton) view.findViewById(R.id.addevent);
        rated = (TextView) view.findViewById(R.id.rated_id);
        ratingbar = (RatingBar) view.findViewById(R.id.ratebar);
        numevents = (TextView) view.findViewById(R.id.numevents);
        imageProfile = (CircleImageView) view.findViewById(R.id.circleImageView);


        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");
        postRef = FirebaseDatabase.getInstance().getReference().child("Post").child(my_id);
        UsersRef.keepSynced(true);
        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events").child(my_id);
        eventsRef.keepSynced(true);


        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshotevents : dataSnapshot.getChildren()) {
                    long num_events = dataSnapshot.getChildrenCount();
                    numevents.setText(String.valueOf(num_events));
                    rateRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshotrates : dataSnapshot.getChildren()) {

                                if (snapshotevents.getKey().equals(snapshotrates.getKey())) {

                                    rateRef.child(snapshotrates.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                long tuto = dataSnapshot.getChildrenCount();
                                                String rate = snapshot.child("rate").getValue().toString();
                                                float ratenumber = Float.valueOf(rate);
                                                i = Math.round((i + ratenumber) / 3);
                                                String j = String.valueOf(i);
                                                rated.setText(j);
                                                ratingbar.setRating(i);


                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {
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

        listOfposts = (RecyclerView) view.findViewById(R.id.posts_id);
        listofeventposts = (RecyclerView) view.findViewById(R.id.listofposts_id);
        LinearLayoutManager layoutManagerevents = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerposts = new LinearLayoutManager(getActivity());

        listofeventposts.setLayoutManager(layoutManagerposts);
        listOfposts.setLayoutManager(layoutManagerevents);
        layoutManagerevents.setReverseLayout(true);
        layoutManagerposts.setReverseLayout(true);
        layoutManagerevents.setStackFromEnd(true);


        listOfposts.setHasFixedSize(true);
        listofeventposts.setHasFixedSize(true);
        layoutManagerevents.setOrientation(LinearLayoutManager.HORIZONTAL);


        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEventList = new Intent(getContext(), AddEventActivity.class);
                startActivity(addEventList);
            }
        });


        layoutManagerevents.setAutoMeasureEnabled(true);

        FirebaseRecyclerAdapter<Post, PostsViewHolder> firebaseRecyclerAdapter1
                = new FirebaseRecyclerAdapter<Post, PostsViewHolder>
                (
                        Post.class,
                        R.layout.post_layout,
                        PostsViewHolder.class,
                        postRef
                ) {
            @Override
            protected void populateViewHolder(final PostsViewHolder viewHolder, Post model, int position) {
                String post_id = getRef(position).getKey();
                postRef.child(post_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String post = dataSnapshot.child("post").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();


                            viewHolder.setPost(post);
                            viewHolder.setTime(time);
                            eventsRef.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    String image = dataSnapshot.child("imageEvent").getValue().toString();

                                    viewHolder.setImage(image, getContext());
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String eventId = dataSnapshot.child("eventId").getValue().toString();
                                            String pushId = dataSnapshot.child("pushId").getValue().toString();
                                            String month = dataSnapshot.child("month").getValue().toString();


                                            bundle.putString("push_id", pushId);
                                            bundle.putString("event_id", eventId);
                                            bundle.putString("month", month);
                                            bundle.putString("title", dataSnapshot.child("title").getValue().toString());
                                            Intent intent = new Intent(getContext(), SingleEventPostActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);

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
        };
        listofeventposts.setAdapter(firebaseRecyclerAdapter1);


        return view;
    }

    @Override
    public void onStart() {


        FirebaseRecyclerAdapter<EventClass, PostsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EventClass, PostsViewHolder>
                (
                        EventClass.class,
                        R.layout.profile_items,
                        PostsViewHolder.class,
                        eventsRef) {
            @Override
            protected void populateViewHolder(final PostsViewHolder viewHolder, final EventClass model, final int position) {


                viewHolder.setImageEvent(model.getImageEvent(), getContext());
                String string = getRef(position).getKey();
                eventsRef.child(string).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String eventId = dataSnapshot.child("eventId").getValue().toString();
                                String pushId = dataSnapshot.child("pushId").getValue().toString();
                                String month = dataSnapshot.child("month").getValue().toString();


                                bundle.putString("push_id", pushId);
                                bundle.putString("event_id", eventId);
                                bundle.putString("month", month);
                                bundle.putString("title", dataSnapshot.child("title").getValue().toString());
                                Intent intent = new Intent(getContext(), SingleEventPostActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


        };


        listOfposts.setAdapter(firebaseRecyclerAdapter);


        UsersRef.child(my_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("userName").getValue().toString();
                username.setText(name);
                String status = dataSnapshot.child("userStatus").getValue().toString();
                userstatus.setText(status);
                String Image = dataSnapshot.child("userImage").getValue().toString();


                Picasso.with(getContext()).load(Image)
                        .placeholder(R.drawable.profile_pic).
                        into(imageProfile);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        super.onStart();
    }


    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setImage(String image, Context cxt) {
            ImageView _image = (ImageView) mView.findViewById(R.id.postimage_id);
            Picasso.with(cxt).load(image).placeholder(R.drawable.profile_pic).into(_image);

        }

        public void setPost(String post) {
            TextView txt = (TextView) mView.findViewById(R.id.post_txt);
            txt.setText(post);
        }

        public void setTime(String time) {
            TextView txt = (TextView) mView.findViewById(R.id.timepost_id);
            txt.setText(time);

        }


        public void setImageEvent(String imageEvent, Context cxt) {
            CircleImageView _image = (CircleImageView) mView.findViewById(R.id.event_image_id);
            Picasso.with(cxt).load(imageEvent).placeholder(R.drawable.profile_pic).into(_image);


        }


    }
}