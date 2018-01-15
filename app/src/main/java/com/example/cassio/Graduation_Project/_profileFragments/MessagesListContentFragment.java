package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.MessagesActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.ListOfMessages;
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


public class MessagesListContentFragment extends Fragment {

    String my_id;
    private View mView;
    private RecyclerView messagesList;
    private DatabaseReference MessagesRef, UsersRef;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.chat_layout, null);
        messagesList = (RecyclerView) mView.findViewById(R.id.listofmessagesfragment_id);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Community").child(my_id);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        messagesList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        messagesList.setLayoutManager(layoutManager);

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ListOfMessages, ListmessagesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ListOfMessages, ListmessagesViewHolder>
                (
                        ListOfMessages.class,
                        R.layout.dispaly_users_layout,
                        MessagesListContentFragment.ListmessagesViewHolder.class,
                        MessagesRef

                ) {
            @Override
            protected void populateViewHolder(final ListmessagesViewHolder viewHolder, final ListOfMessages model, int position) {


                final String friends_id = getRef(position).getKey();
                UsersRef.child(friends_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String name = dataSnapshot.child("userName").getValue().toString();
                        String image = dataSnapshot.child("userImage").getValue().toString();
                        String status = dataSnapshot.child("userStatus").getValue().toString();
                        viewHolder.setUserStatus(model.getUserStatus());


                        viewHolder.setName(name);
                        viewHolder.setImage(image, getContext());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getContext(), MessagesActivity.class);
                                intent.putExtra("targed_person_id", friends_id);
                                intent.putExtra("user_name", name);
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
        messagesList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ListmessagesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public ListmessagesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUserStatus(String userStatus) {
            TextView user_Status = (TextView) mView.findViewById(R.id.all_users_status);
            user_Status.setText(userStatus);
        }

        public void setName(String user_name) {
            TextView username = (TextView) mView.findViewById(R.id.all_users_name);
            username.setText(user_name);
        }

        public void setImage(final String user_image, final Context context) {
            final CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_image);


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