package com.example.cassio.Graduation_Project.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SingleEventPostActivity;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<HomeListPost> homeListPostList;
    private Context context;
    private FirebaseAuth mAuth;
    private String myId;
    private boolean saveProcess;
    private List<String> listofmonths = new ArrayList<String>();


    public HomeAdapter(List<HomeListPost> homeListPostList) {
        this.homeListPostList = homeListPostList;
    }

    public HomeAdapter(List<HomeListPost> homeListPostList, Context ctx) {
        this.homeListPostList = homeListPostList;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homelist_item, parent, false);
        listofmonths.add("January");
        listofmonths.add("February");
        listofmonths.add("March");
        listofmonths.add("April");
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final HomeListPost homeListPost = homeListPostList.get(position);
        holder.title.setText(homeListPost.getTitle());
        holder.name.setText(homeListPost.getName());
        holder.time.setText(homeListPost.getTime());


        Picasso.with(context).load(homeListPost.getuserImage())
                .placeholder(R.drawable.profile_pic).
                into(holder.image);
        Picasso.with(context).load(homeListPost.getEventImage())
                .placeholder(R.drawable.profile_pic).
                into(holder.eventImage);
        holder.savedtxt.setVisibility(View.INVISIBLE);
        holder.interestbtn.setText("interested");


        mAuth = FirebaseAuth.getInstance();
        myId = mAuth.getCurrentUser().getUid();
        final DatabaseReference savedRef = FirebaseDatabase.getInstance().getReference().child("SavedEvents").child(myId);
        final String idEvent = homeListPost.getIdEvent();
        savedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (snapshot1.getKey().equals(idEvent)) {
                            holder.savedtxt.setVisibility(View.VISIBLE);
                            holder.interestbtn.setText("saved !");


                        } else {

                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.interestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveProcess = true;

                savedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String month = homeListPost.getMonth();
                        String saveCurrentDate = SingleEventPostActivity.date();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (saveProcess) {
                                    if (snapshot1.getKey().equals(idEvent)) {
                                        String i = month;
                                        int j = Integer.parseInt(i);
                                        String searchedMonth = listofmonths.get(j);

                                        savedRef.child(searchedMonth).child(idEvent).removeValue();
                                        holder.interestbtn.setText("interested");
                                        holder.savedtxt.setVisibility(View.INVISIBLE);

                                        saveProcess = false;
                                    } else {
                                        holder.savedtxt.setVisibility(View.INVISIBLE);

                                        if (month.equals("3"))

                                        {
                                            savedRef.child("April").child(idEvent).child("savedIn").setValue(saveCurrentDate);
                                            savedRef.child("April").child(idEvent).child("month").setValue(month);
                                            savedRef.child("April").child(idEvent).child("title").setValue(homeListPost.getTitle());
                                            holder.interestbtn.setText("saved !");


                                        }
                                        else if (month.equals("1"))

                                        {
                                            savedRef.child("February").child(idEvent).child("savedIn").setValue(saveCurrentDate);
                                            savedRef.child("February").child(idEvent).child("month").setValue(month);
                                            savedRef.child("February").child(idEvent).child("title").setValue(homeListPost.getTitle());
                                            holder.interestbtn.setText("saved !");


                                        }
                                        saveProcess = false;
                                    }
                                }
                            }
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
    public int getItemCount() {
        return homeListPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, name, time, interestbtn, savedtxt;
        public CircleImageView image;
        private ImageView eventImage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.post_id);
            name = (TextView) view.findViewById(R.id.name_id);
            image = (CircleImageView) view.findViewById(R.id.image_id);
            eventImage = (ImageView) view.findViewById(R.id.eventimage_id);
            time = (TextView) view.findViewById(R.id.time_post);
            interestbtn = (TextView) view.findViewById(R.id.interested_id);
            savedtxt = (TextView) view.findViewById(R.id.savedpost_txt);

        }


    }
}