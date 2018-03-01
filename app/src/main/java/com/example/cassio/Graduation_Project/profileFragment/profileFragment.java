package com.example.cassio.Graduation_Project.profileFragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.FriendsProfileActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SingleEventPostActivity;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {

    View view;
    DatabaseReference eventRef, rateRef, postRef;
    RecyclerView listOfposts, listofappeals;
    TextView txt, nopost;
    String myDataFromActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        listOfposts = (RecyclerView) view.findViewById(R.id.posts_id);
        listofappeals = (RecyclerView) view.findViewById(R.id.postappeals_id);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        listOfposts.setLayoutManager(layoutManager);
        listOfposts.setHasFixedSize(true);
        listofappeals.setLayoutManager(layoutManager1);
        listofappeals.setHasFixedSize(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        FriendsProfileActivity activity = (FriendsProfileActivity) getActivity();
        myDataFromActivity = activity.getMyData();
        txt = (TextView) view.findViewById(R.id.txt_id);
        nopost = (TextView) view.findViewById(R.id.noposts_id);

        eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(myDataFromActivity);
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");
        postRef = FirebaseDatabase.getInstance().getReference().child("Post").child(myDataFromActivity);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    txt.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerAdapter<EventClass, profileFragment.PostsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EventClass, profileFragment.PostsViewHolder>
                (
                        EventClass.class,
                        R.layout.profile_items,
                        profileFragment.PostsViewHolder.class,
                        eventRef) {
            @Override
            protected void populateViewHolder(final profileFragment.PostsViewHolder viewHolder, final EventClass model, final int position) {
                String x = getRef(position).getKey();
                viewHolder.setImageEvent(model.getImageEvent(), getContext());


            }
        };
        listOfposts.setAdapter(firebaseRecyclerAdapter);
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
                            if (dataSnapshot.getChildrenCount() != 0) {
                                TextView txt = (TextView) view.findViewById(R.id.noposts_id);
                                txt.setVisibility(View.INVISIBLE);
                            }
                            String post = dataSnapshot.child("post").getValue().toString();
                            String time = dataSnapshot.child("time").getValue().toString();


                            viewHolder.setTitle(post);
                            viewHolder.setTime(time);
                            eventRef.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {

                                    String image = dataSnapshot.child("imageEvent").getValue().toString();

//
                                    viewHolder.setImage(image, getContext());
                                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String eventId = dataSnapshot.child("eventId").getValue().toString();
                                            String pushId = dataSnapshot.child("pushId").getValue().toString();
                                            String month = dataSnapshot.child("month").getValue().toString();
                                            Bundle bundle = new Bundle();

                                            bundle.putString("push_id", pushId);
                                            bundle.putString("event_id", eventId);
                                            bundle.putString("month", month);
                                            bundle.putString("title", dataSnapshot.child("title").getValue().toString());
                                            Intent intent = new Intent(getContext(), SingleEventPostActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


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
        listofappeals.setAdapter(firebaseRecyclerAdapter1);


        return view;
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setTime(String time) {
            TextView txt = (TextView) mView.findViewById(R.id.timepost_id);
            txt.setText(time);

        }

        public void setTitle(String time) {
            TextView txt = (TextView) mView.findViewById(R.id.post_txt);
            txt.setText(time);

        }


        public void setImageEvent(String imageEvent, Context cxt) {
            CircleImageView _image = (CircleImageView) mView.findViewById(R.id.event_image_id);
            Picasso.with(cxt).load(imageEvent).placeholder(R.drawable.profile_pic).into(_image);


        }

        public void setImage(String imageEvent, Context cxt) {
            ImageView _image = (ImageView) mView.findViewById(R.id.postimage_id);
            Picasso.with(cxt).load(imageEvent).placeholder(R.drawable.profile_pic).into(_image);


        }


    }


}
