package com.example.cassio.Graduation_Project;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.HomeAdapter;
import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.example.cassio.Graduation_Project.models.EventClass;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.example.cassio.Graduation_Project.models.ParentList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View mView;
    RecyclerView recyclerView , userslist;
    DatabaseReference communityRef, userRef, postRef;
    String my_id;
    FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        communityRef = FirebaseDatabase.getInstance().getReference().child("Community");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Post");

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        recyclerView = (RecyclerView) mView.findViewById(R.id.homelist_id);
        userslist = (RecyclerView) mView.findViewById(R.id.userslist_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        userslist.setLayoutManager(layoutManager1);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);



        communityRef.child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 final List<HomeListPost> homeListPostList = new ArrayList<>();
                 for (final DataSnapshot snapshot : dataSnapshot.getChildren()){

                     postRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             TextView txt = (TextView) mView.findViewById(R.id.noevent_id);
                             txt.setVisibility(View.INVISIBLE);
                             for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                 final String string = snapshot1.child("eventId").getValue().toString();

                                 userRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         String name = dataSnapshot.child("userName").getValue().toString();
                                         String Image = dataSnapshot.child("userImage").getValue().toString();


                                         homeListPostList.add(new HomeListPost(name,Image,string));
                                         HomeAdapter adapter = new HomeAdapter(homeListPostList,getContext());
                                         recyclerView.setAdapter(adapter);
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









            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        communityRef.child(my_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot1, String s) {

                final List<AllUsersClass> users = new ArrayList<>();

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                            {
                                if (!dataSnapshot1.getKey().equals(dataSnapshot2.getKey())){
                                    userRef.child(dataSnapshot2.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String name = dataSnapshot.child("userName").getValue().toString();
                                            String Image = dataSnapshot.child("userImage").getValue().toString();
                                            String userId =dataSnapshot.child("userId").getValue().toString();

                                            users.add(new AllUsersClass(name,Image,userId));
                                            uUsersAdapter adapter = new uUsersAdapter(users,getContext());
                                            userslist.setAdapter(adapter);

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




        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public CircleImageView image;

            public MyViewHolder(View view) {
                super(view);
                name = (TextView) view.findViewById(R.id.all_users_name);
                image = (CircleImageView) view.findViewById(R.id.all_users_image);

            }


        }


        public uUsersAdapter(List<AllUsersClass> usersList , Context ctx ) {
            this.usersList = usersList;
            this.context=ctx;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.users_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final AllUsersClass users = usersList.get(position);
            holder.name.setText(users.getUserName());
            Picasso.with(context).load(users.getUserImage())
                    .placeholder(R.drawable.profile_pic).
                    into(holder.image);

           holder.image.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   communityRef.child(my_id).addChildEventListener(new ChildEventListener() {
                       @Override
                       public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           userRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                       if(!dataSnapshot.getKey().equals(dataSnapshot1.getKey())){
                                           final String targed_person_id = users.getUserId();

//                           Toast.makeText(context, targed_person_id, Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(getContext(),friendsProfileActivity.class);
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

//                   userRef.addValueEventListener(new ValueEventListener() {
//                       @Override
//                       public void onDataChange(DataSnapshot dataSnapshot) {

//
//                       }
//
//                       @Override
//                       public void onCancelled(DatabaseError databaseError) {
//
//                       }
//                   });
               }

           });




        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

    }




}
