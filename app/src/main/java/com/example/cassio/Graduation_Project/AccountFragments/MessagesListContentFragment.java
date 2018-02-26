package com.example.cassio.Graduation_Project.AccountFragments;

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
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.MessagesActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.example.cassio.Graduation_Project.models.messagesClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessagesListContentFragment extends Fragment {

    String my_id;
    private View mView;
    private RecyclerView messagesList;
    private DatabaseReference MessagesRef, UsersRef, lastMessageRef;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.messages_list_layout, null);
        messagesList = (RecyclerView) mView.findViewById(R.id.listofmessagesfragment_id);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        MessagesRef = FirebaseDatabase.getInstance().getReference().child("Community").child(my_id);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        lastMessageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        messagesList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        messagesList.setLayoutManager(layoutManager);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        lastMessageRef.child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<messagesClass> lastmessage = new ArrayList<>();
                final List<AllUsersClass> users = new ArrayList<>();

                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    lastMessageRef.child(my_id).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Query lastQuery = lastMessageRef.child(my_id).child(snapshot.getKey()).orderByKey().limitToLast(1);

                            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot1) {
                                    UsersRef.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                                                String message = snapshot1.child("message").getValue().toString();
                                                String time = snapshot1.child("msgtime").getValue().toString();
                                                String from = dataSnapshot.child("userName").getValue().toString();
                                                String idReciever = snapshot1.child("from").getValue().toString();
                                                String recieverName = dataSnapshot.child("userName").getValue().toString();
                                                String image = dataSnapshot.child("userImage").getValue().toString();
                                                lastmessage.add(new messagesClass(message, time, from, recieverName, idReciever, image));
                                                listOfMessagesAdapter adapter = new listOfMessagesAdapter(lastmessage, getContext());
                                                messagesList.setAdapter(adapter);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
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


        return mView;
    }


    public class listOfMessagesAdapter extends RecyclerView.Adapter<listOfMessagesAdapter.MyViewHolder> {

        private List<messagesClass> lastmessage;
        private Context ctx;


        public listOfMessagesAdapter(List<messagesClass> lastmessage, Context contex) {
            this.lastmessage = lastmessage;
            this.ctx = contex;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dispaly_users_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final messagesClass messagesClass = lastmessage.get(position);
            holder.message.setText(messagesClass.getmessage());
            holder.msgtime.setText(messagesClass.getmsgtime());
            holder.name.setText(messagesClass.getFrom());
            final String name = messagesClass.getFrom();


            Picasso.with(ctx).load(messagesClass.getImage())
                    .placeholder(R.drawable.profile_pic).
                    into(holder.image);
            holder.linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastMessageRef.child(my_id).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String targed_person_id = messagesClass.getIdReciever();

                            Bundle bundle = new Bundle();
                            bundle.putString("targed_person_id", targed_person_id);
                            bundle.putString("user_name", name);
                            Intent intent = new Intent(getContext(), MessagesActivity.class);
                            intent.putExtras(bundle);
                            Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);


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
            return lastmessage.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView message, msgtime, name;
            public LinearLayout linear;
            public CircleImageView image;

            public MyViewHolder(View view) {
                super(view);
                message = (TextView) view.findViewById(R.id.lastmessage_id);
                msgtime = (TextView) view.findViewById(R.id.messagetime_id);
                name = (TextView) view.findViewById(R.id.all_users_name);
                linear = (LinearLayout) view.findViewById(R.id.linear_message_id);
                image = (CircleImageView) view.findViewById(R.id.all_users_image);


            }


        }
    }
}