package com.example.cassio.Graduation_Project.profileFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project._profileFragments.MainProfileContentFragment;
import com.example.cassio.Graduation_Project.friendsProfileActivity;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {

View view ;
DatabaseReference eventRef,rateRef;
RecyclerView listOfposts;
TextView txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        listOfposts = (RecyclerView) view.findViewById(R.id.posts_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listOfposts.setLayoutManager(layoutManager);
        listOfposts.setHasFixedSize(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        friendsProfileActivity activity = (friendsProfileActivity) getActivity();
        String myDataFromActivity = activity.getMyData();
        txt = (TextView) view.findViewById(R.id.txt_id);

        eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(myDataFromActivity);
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()!= 0){
                    txt.setVisibility(View.INVISIBLE);
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
                        eventRef)
        {
            @Override
            protected void populateViewHolder(final profileFragment.PostsViewHolder viewHolder, final EventClass model, final int position)
            {




            }
    };
    listOfposts.setAdapter(firebaseRecyclerAdapter);}


    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        CardView cardevent;

        public PostsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardevent = (CardView) mView.findViewById(R.id.card_view);

        }}
}
