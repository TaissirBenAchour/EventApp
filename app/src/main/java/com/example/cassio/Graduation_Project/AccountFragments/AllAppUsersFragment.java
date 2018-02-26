package com.example.cassio.Graduation_Project.AccountFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.FriendsProfileActivity;
import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllAppUsersFragment extends Fragment {


    String my_id;
    FirebaseAuth mAuth;
    View mView;
    private RecyclerView listUsers;
    private DatabaseReference allusersDBReference, communityRef, sponsorRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_all_ausers, container, false);
        allusersDBReference = FirebaseDatabase.getInstance().getReference().child("Users");
        communityRef = FirebaseDatabase.getInstance().getReference().child("Community");
        sponsorRef = FirebaseDatabase.getInstance().getReference().child("Sponsors");
        listUsers = (RecyclerView) mView.findViewById(R.id.recycle_id);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        listUsers.setHasFixedSize(true);
        listUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        allusersDBReference.keepSynced(true);


        communityRef.child(my_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot1, String s) {

                final List<AllUsersClass> users = new ArrayList<>();

                allusersDBReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            {


                                if (!dataSnapshot1.getKey().equals(dataSnapshot2.getKey())) {
                                    allusersDBReference.child(dataSnapshot2.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            final String name = dataSnapshot.child("userName").getValue().toString();
                                            final String Image = dataSnapshot.child("userImage").getValue().toString();
                                            final String userId = dataSnapshot.child("userId").getValue().toString();
                                            final String status = dataSnapshot.child("userStatus").getValue().toString();
                                            final String txt = dataSnapshot.child("plus").getValue().toString();
                                            users.add(new AllUsersClass(name, status, Image, userId, txt));
                                            uUsersAdapter adapter = new uUsersAdapter(users, getContext());
                                            listUsers.setAdapter(adapter);


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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

        return mView;
    }

    public class uUsersAdapter extends RecyclerView.Adapter<uUsersAdapter.MyViewHolder> {

        private List<AllUsersClass> usersList;
        private Context context;


        public uUsersAdapter(List<AllUsersClass> usersList, Context ctx) {
            this.usersList = usersList;
            this.context = ctx;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dispaly_users, parent, false);


            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final AllUsersClass users = usersList.get(position);
            holder.name.setText(users.getUserName());
            holder.status.setText(users.getUserStatus());
            holder.txt.setText(users.getTxt());
            if (!users.getTxt().equals("sponsor")) {
                holder.txt.setVisibility(View.INVISIBLE);
            }


            Picasso.with(context).load(users.getUserImage())
                    .placeholder(R.drawable.profile_pic).
                    into(holder.image);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    communityRef.child(my_id).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            allusersDBReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        if (!dataSnapshot.getKey().equals(dataSnapshot1.getKey())) {
                                            final String targed_person_id = users.getUserId();

//                           Toast.makeText(context, targed_person_id, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getContext(), FriendsProfileActivity.class);
                                            intent.putExtra("targed_person_id", targed_person_id);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            getActivity().finish();


                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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

            });


        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, status, txt;
            public CircleImageView image;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.all_users_name);
                image = (CircleImageView) view.findViewById(R.id.all_users_image);
                status = (TextView) view.findViewById(R.id.userstatus_id);
                txt = (TextView) view.findViewById(R.id.sponsors_id);


            }


        }

    }

}
