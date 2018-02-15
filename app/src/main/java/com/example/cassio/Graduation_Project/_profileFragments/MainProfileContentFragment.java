package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.AddEventActivity;
import com.example.cassio.Graduation_Project.R;
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
    ImageView imageProfile;
    private View view;
    private String my_id,eventPost;
    private RecyclerView listOfposts,listofeventposts;
    private DatabaseReference UsersRef, eventsRef, rateRef , postRef;
    private FirebaseAuth mAuth;
    private TextView username, userstatus, rated, numevents;
    private ImageButton addevent;
    private CardView cardView , appealsCard , calendarCard, logisticCard;
    private RatingBar ratingbar;
    private Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.collapsing_header_test, null);



        username = (TextView) view.findViewById(R.id.username_id);
        userstatus = (TextView) view.findViewById(R.id.userstatus_id);
        addevent = (ImageButton) view.findViewById(R.id.addevent);
//        cardView = (CardView) view.findViewById(R.id.available_events_id);
//        appealsCard = (CardView) view.findViewById(R.id.appealscard_id);
//        calendarCard = (CardView) view.findViewById(R.id.survey_id);
//        logisticCard = (CardView) view.findViewById(R.id.logistics_id);

        rated = (TextView) view.findViewById(R.id.rated_id);
        ratingbar = (RatingBar) view.findViewById(R.id.ratebar);
        numevents = (TextView) view.findViewById(R.id.numevents);
         imageProfile= (ImageView) view.findViewById(R.id.circleImageView);





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
                                                i = (i + ratenumber) / tuto;
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
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToEventList = new Intent(getContext(), AvailableEventActivity.class);
//                startActivity(goToEventList);
//            }
//        });
//        appealsCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToAppealsList = new Intent(getContext(), AppealsList.class);
//                startActivity(goToAppealsList);
//            }
//        });
//
//        calendarCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToCalendar = new Intent(getContext(), testCalendar.class);
//                startActivity(goToCalendar);
//            }
//        });
//        logisticCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent goToCalendar = new Intent(getContext(), LogisticsActivity.class);
//                startActivity(goToCalendar);
//            }
//        });

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
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            String post = snapshot.getValue().toString();
                            viewHolder.setPost(post);
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };listofeventposts.setAdapter(firebaseRecyclerAdapter1);


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
            protected void populateViewHolder(final PostsViewHolder viewHolder, final EventClass model, final int position)
            {



                viewHolder.setImageEvent(model.getImageEvent(),getContext());
//                Toast.makeText(getContext(), my_id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), eventsRef.toString(), Toast.LENGTH_SHORT).show();
//
//                eventsRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//
//
//                            final String ParentKey = snapshot.getKey().toString();
//                            Toast.makeText(getContext(), ParentKey, Toast.LENGTH_SHORT).show();
//                        }
//
////        String title = dataSnapshot.child("title").getValue().toString();
////        Toast.makeText(getContext(), title, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
////                viewHolder.setTitle(model.getTitle());
////                viewHolder.setDate(model.getDate());
////
////                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        String id_key = getRef(position).getKey();
////
////                        Toast.makeText(getContext(), my_id, Toast.LENGTH_SHORT).show();
////                        Bundle bundle = new Bundle();
////
////
////                        bundle.putString("push_id",id_key);
////                        bundle.putString("my_id",my_id);
////
////                        Intent intent = new Intent(getContext(),SingleEventPostView.class);
////
////                        intent.putExtras(bundle);
////                        startActivity(intent);
////                    }
////                });
//
////                UsersRef.child(my_id).addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
//////                        String name = dataSnapshot.child("userName").getValue().toString().trim();
//////                        viewHolder.listusername.setText(name);
////
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////
////                    }
////                });
//
//
//
//
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
        TextView listusername;
        TextView name;
        CardView cardevent;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //  listusername = (TextView) mView.findViewById(R.id.name_id);
            cardevent = (CardView) mView.findViewById(R.id.card_view);

        }
        public void setPost(String post) {
            TextView txt = (TextView) mView.findViewById(R.id.post_txt);
            txt.setText(post);
        }

        public void setDate(String date) {
            //TextView dateofpost = (TextView) mView.findViewById(R.id.time_id);
            // dateofpost.setText(date);

        }

        public void setImageEvent(String imageEvent,Context cxt){
            CircleImageView _image = (CircleImageView) mView.findViewById(R.id.event_image_id);
           Picasso.with(cxt).load(imageEvent).placeholder(R.drawable.profile_pic).into(_image);


        }

        public void setTitle(String title) {
            TextView titleofpost = (TextView) mView.findViewById(R.id.namepostevent_id);
            titleofpost.setText(title);
        }
    }
}