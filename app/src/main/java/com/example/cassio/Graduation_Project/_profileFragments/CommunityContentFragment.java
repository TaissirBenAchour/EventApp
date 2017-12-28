package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.ChatActivity;
import com.example.cassio.Graduation_Project.CommunityListClass;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.friends_profile;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class  CommunityContentFragment extends Fragment {

    private RecyclerView listOfJoinedPersons;
    private View myListView;
    private DatabaseReference CommunityRef,UsersRef;
    private FirebaseAuth mAuth;
    String my_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myListView = inflater.inflate(R.layout.community_layout ,container,false);
        listOfJoinedPersons = (RecyclerView) myListView.findViewById(R.id.myListView_id);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        CommunityRef = FirebaseDatabase.getInstance().getReference().child("Community").child(my_id);
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        listOfJoinedPersons.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listOfJoinedPersons.setLayoutManager(layoutManager);
        return myListView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CommunityListClass,CommunityViewHolder > firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<CommunityListClass, CommunityViewHolder> (
                CommunityListClass.class,
                R.layout.dispaly_users_layout,
                CommunityViewHolder.class,
                CommunityRef)  {
            @Override
            protected void populateViewHolder(final CommunityViewHolder viewHolder, final CommunityListClass model, int position) {
                final String listCommunityUid=getRef(position).getKey();
                CommunityRef.child(listCommunityUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewHolder.setDate(model.getDate());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                UsersRef.child(listCommunityUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("userName").getValue().toString();
                        String image = dataSnapshot.child("userImage").getValue().toString();

                        viewHolder.setName(name);
                        viewHolder.setImage(image,getContext());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options [] = new CharSequence[]{
                                        name +"'s profile",
                                        "send message"

                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("select option");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                            if (i == 0){
                                                Intent goToprofile = new Intent(getContext(), friends_profile.class);
                                                goToprofile.putExtra("targed_person_id",listCommunityUid);
                                                startActivity(goToprofile);
                                            }
                                        if (i == 1){
                                            Intent goToChat = new Intent(getContext(), ChatActivity.class);
                                            goToChat.putExtra("targed_person_id",listCommunityUid);
                                            goToChat.putExtra("user_name" , name);
                                            startActivity(goToChat);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        };
        listOfJoinedPersons.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public CommunityViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setDate(String date){
            TextView datePlacement=(TextView) mView.findViewById(R.id.all_users_status);
            datePlacement.setText("friends since "+date );
        }
        public void setName(String user_name){
            TextView username=(TextView) mView.findViewById(R.id.all_users_name);
            username.setText(user_name);
        }
        public void setImage(final String user_image, final Context context){
            final CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_image);


            if (!image.equals("profile_pic")) {
// OFF LINE CASE  !!!!
                // I SHOULD O BACK TO USERS PROFILE IN CASE I WILL CREATE ONES , TO VERIFY THE OFFLINE MODE ,
                // DONT FORGET !
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