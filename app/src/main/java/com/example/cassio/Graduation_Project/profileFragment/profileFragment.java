package com.example.cassio.Graduation_Project.profileFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.freindspostAdapter;
import com.example.cassio.Graduation_Project.FriendsProfileActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

    View view;
    DatabaseReference eventRef, rateRef, postRef;
    RecyclerView listOfposts, listofappeals;
    TextView txt;

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
        final String myDataFromActivity = activity.getMyData();
        txt = (TextView) view.findViewById(R.id.txt_id);

        eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(myDataFromActivity);
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");
        postRef = FirebaseDatabase.getInstance().getReference().child("Post");

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

        final List<HomeListPost> listPosts = new ArrayList();
        postRef.child(myDataFromActivity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView txt = (TextView) view.findViewById(R.id.noposts_id);
                txt.setVisibility(View.INVISIBLE);

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Toast.makeText(getContext(), dataSnapshot1.getKey(), Toast.LENGTH_SHORT).show();

                    final String title = dataSnapshot1.child("post").getValue().toString();

                    listPosts.add(new HomeListPost(title));
                    freindspostAdapter adapter = new freindspostAdapter(listPosts);
                    listofappeals.setAdapter(adapter);
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<EventClass, profileFragment.PostsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EventClass, profileFragment.PostsViewHolder>
                (
                        EventClass.class,
                        R.layout.profile_items,
                        profileFragment.PostsViewHolder.class,
                        eventRef) {
            @Override
            protected void populateViewHolder(final profileFragment.PostsViewHolder viewHolder, final EventClass model, final int position) {


            }
        };
        listOfposts.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        CardView cardevent;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardevent = (CardView) mView.findViewById(R.id.card_view);

        }
    }


}
