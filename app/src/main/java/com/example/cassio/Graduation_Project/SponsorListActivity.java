package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.models.Sponsors;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SponsorListActivity extends Fragment {

    DatabaseReference sponsorsRef, userRef ;
    RecyclerView sponsor_list;

    View mView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_list_sponsors, container, false);

         sponsorsRef = FirebaseDatabase.getInstance().getReference().child("Sponsors");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        sponsor_list = (RecyclerView)mView.findViewById(R.id.sponsorlist_id);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        sponsor_list.setLayoutManager(layoutManager);
        sponsor_list.setHasFixedSize(true);


return mView;
    }
    public void onStart() {

        super.onStart();
        FirebaseRecyclerAdapter<Sponsors, SponsorsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Sponsors, SponsorsViewHolder>
                (
                        Sponsors.class,
                        R.layout.item_sponsor,
                        SponsorsViewHolder.class,
                        sponsorsRef
                ) {
            @Override
            protected void populateViewHolder(final SponsorsViewHolder viewHolder, Sponsors model, int position) {
                final String sponsor_id = getRef(position).getKey();
                userRef.child(sponsor_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("userName").getValue().toString();
                        String image = dataSnapshot.child("userImage").getValue().toString();
                        final String id = dataSnapshot.child("userId").getValue().toString();
                        sponsorsRef.child(sponsor_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final  String typebus= dataSnapshot.child("typeBusiness").getValue().toString();
                            viewHolder.setBusinessType(typebus);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        viewHolder.setName(name);
                        viewHolder.setImage(image,getContext());
                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(),FriendsProfileActivity.class);
                                intent.putExtra("targed_person_id",id);
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
        sponsor_list.setAdapter(firebaseRecyclerAdapter);
    }


            static class SponsorsViewHolder extends RecyclerView.ViewHolder{
        View mView;

                public SponsorsViewHolder(View itemView) {
                    super(itemView);
                    mView=itemView;
                }
                public void setName(String name){
                   TextView sponsor = (TextView) mView.findViewById(R.id.sponsor_id);
                   sponsor.setText(name);


                }
                public void setBusinessType(String businessType){
                    TextView sponsor = (TextView) mView.findViewById(R.id.typebusiness);
                    sponsor.setText(businessType);


                }
                public  void setImage (final String user_image, final Context context) {
                    final ImageView image = (ImageView) mView.findViewById(R.id.sponsorimage_id);


                    if (!image.equals("profile_pic")) {

                        Picasso.with(context).load(user_image).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profile_pic).into(image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(user_image).placeholder(R.drawable.profile_pic).into(image);
                            }
                        });

                    }
                }
            }
}
