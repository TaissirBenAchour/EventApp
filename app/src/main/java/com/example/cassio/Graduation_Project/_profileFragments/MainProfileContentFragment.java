package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainProfileContentFragment extends Fragment {

    private View view;
    private ImageButton send_req;
    private ImageButton dec_req;
    private String my_id;
    private RecyclerView listOfposts;
    private DatabaseReference UsersRef, eventsRef;
    private FirebaseAuth mAuth;
    private TextView username, userstatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_profile, null);

        send_req = (ImageButton) view.findViewById(R.id.add_request);
        dec_req = (ImageButton) view.findViewById(R.id.clear_request);
        dec_req.setVisibility(View.INVISIBLE);
        send_req.setVisibility(View.INVISIBLE);
        username = (TextView) view.findViewById(R.id.username_id);
        userstatus = (TextView) view.findViewById(R.id.userstatus_id);


        listOfposts = (RecyclerView) view.findViewById(R.id.posts_id);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersRef.keepSynced(true);
        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events").child(my_id);
        eventsRef.keepSynced(true);


        listOfposts.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listOfposts.setLayoutManager(layoutManager);

        FirebaseRecyclerAdapter<EventClass, PostsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EventClass, PostsViewHolder>
                (
                        EventClass.class,
                        R.layout.profile_items,
                        PostsViewHolder.class,
                        eventsRef
                ) {
            @Override
            protected void populateViewHolder(final PostsViewHolder viewHolder, final EventClass model, int position) {

                UsersRef.child(my_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("userName").getValue().toString();
                        viewHolder.listusername.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                eventsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDate(model.getDate());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


        listOfposts.setAdapter(firebaseRecyclerAdapter);


        return view;
    }

    @Override
    public void onStart() {
        UsersRef.child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("userName").getValue().toString();
                username.setText(name);
                String status = dataSnapshot.child("userStatus").getValue().toString();
                userstatus.setText(status);


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

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            listusername = (TextView) mView.findViewById(R.id.name_id);

        }

        public void setDate(String date) {
            TextView dateofpost = (TextView) mView.findViewById(R.id.time_id);
            dateofpost.setText(date);

        }

        public void setTitle(String title) {
            TextView titleofpost = (TextView) mView.findViewById(R.id.namepostevent_id);
            titleofpost.setText(title);
        }
    }
}