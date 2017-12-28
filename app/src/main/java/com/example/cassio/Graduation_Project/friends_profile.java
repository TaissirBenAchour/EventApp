package com.example.cassio.Graduation_Project;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class friends_profile extends AppCompatActivity {
    private Button send_req;
    private Button dec_req;
    private TextView username;
    private TextView desc;
    private DatabaseReference referenceTolistReqs, referenceToUsersList , communityDBReference ;
    private CircleImageView imageProfileFriend;
    private String relation_state ;
    private FirebaseAuth mAuth;
   private String my_current_id ,  targed_person_id;
    private DatabaseReference notificationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_profile);


        send_req = (Button) findViewById(R.id.add_request);
        dec_req = (Button) findViewById(R.id.clear_request);
        username = (TextView) findViewById(R.id.username_friend);
        desc = (TextView) findViewById(R.id.desc_friend);
        imageProfileFriend = (CircleImageView) findViewById(R.id.circleImageView);


        mAuth = FirebaseAuth.getInstance();
         my_current_id = mAuth.getCurrentUser().getUid();
         targed_person_id = getIntent().getExtras().get("targed_person_id").toString();

        referenceToUsersList = FirebaseDatabase.getInstance().getReference().child("Users");
        referenceTolistReqs = FirebaseDatabase.getInstance().getReference().child("join_Community_requests");
        communityDBReference = FirebaseDatabase.getInstance().getReference().child("Community");
        notificationReference =FirebaseDatabase.getInstance().getReference().child("Notification");
        notificationReference.keepSynced(true);
        relation_state = "unfriend";

        referenceToUsersList.child(targed_person_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("userName").getValue().toString();
                final String image = dataSnapshot.child("userImage").getValue().toString();
                String status = dataSnapshot.child("userStatus").getValue().toString();


                username.setText(name);
                desc.setText(status);
                Picasso.with(friends_profile.this).load(image).placeholder(R.drawable.profile_pic).into(imageProfileFriend);



                referenceTolistReqs.child(my_current_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(targed_person_id)){
                            String req_type = dataSnapshot.child(targed_person_id).child("request_type").getValue().toString();
                            if (req_type.equals("request_sent")){
                                relation_state="request_sent";
                                send_req.setText("cancel demand");
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }
                            else if (req_type.equals("request_recieved")){
                                relation_state="request_recieved";
                                send_req.setText("Accept");
                                dec_req.setVisibility(View.VISIBLE);
                                dec_req.setEnabled(true);
                                dec_req.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        refuseJoinCommunity();
                                    }
                                });
                            }

                        }
                    else { communityDBReference.child(my_current_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(targed_person_id)){
                                relation_state="friends";
                                send_req.setText("un-join this person");
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dec_req.setVisibility(View.INVISIBLE);
        dec_req.setEnabled(false);
if (! my_current_id.equals(targed_person_id)){


    send_req.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            send_req.setEnabled(false); // is set to false so i can add some validation , because it creates
            //random key and it may crash


            if (relation_state.equals("unfriend")) {
                sendRequest();
            }

            if (relation_state.equals("request_sent"))
            {
                cancelreq();
            }

            if ( relation_state.equals("request_recieved"))
            {
                acceptReq();

            }
            if (relation_state.equals("friends")){
                unJoinComunity();
            }

        }
    });
}
else {
    dec_req.setVisibility(View.INVISIBLE);
    send_req.setVisibility(View.INVISIBLE);
}
    }

    private void refuseJoinCommunity() {
        referenceTolistReqs.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                relation_state="unfriend";
                                send_req.setEnabled(true);
                                send_req.setText("add again");
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }}
                    });
                }
            }
        });

    }

    private void unJoinComunity() {

        communityDBReference.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    communityDBReference.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()) {
                             send_req.setEnabled(true);
                             relation_state="unfriend";
                             send_req.setText("add");
                             dec_req.setVisibility(View.INVISIBLE);
                             dec_req.setEnabled(false);
                         }
                        }
                    });
                }
            }
        });
    }

    private void acceptReq() {
        Calendar date = Calendar.getInstance();
        final SimpleDateFormat currentDate =new SimpleDateFormat("dd-MMM-yyyy");
        final String saveCurrentDate = currentDate.format(date.getTime());


        communityDBReference.child(my_current_id).child(targed_person_id).child("date").setValue(saveCurrentDate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {


                    @Override
                    public void onSuccess(Void aVoid) {
                        communityDBReference.child(targed_person_id).child(my_current_id).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                referenceTolistReqs.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        send_req.setEnabled(true);
                                                        relation_state="friends";
                                                        send_req.setText("unfriend");
                                                        dec_req.setVisibility(View.INVISIBLE);
                                                        dec_req.setEnabled(false);
                                                    }}
                                            });
                                        }
                                    }
                                });
                            }
                        });

                    }
                });

    }

    private void cancelreq() {
        referenceTolistReqs.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                relation_state="unfriend";
                                send_req.setEnabled(true);
                                send_req.setText("add again");
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }}
                    });
                }
            }
        });
    }

    private void sendRequest() {
        // getting the id to whome i will send the friend request
        referenceTolistReqs.child(my_current_id)
                .child(targed_person_id)
                .child("request_type")
                .setValue("request_sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                        if (task.isSuccessful()) {
                            referenceTolistReqs.child(targed_person_id)
                                    .child(my_current_id)
                                    .child("request_type")
                                    .setValue("request_recieved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {


                                                HashMap <String,String> notifiactions = new HashMap<String, String>();
                                                notifiactions.put("from",my_current_id);
                                                notifiactions.put("type","request");
                                                notificationReference.child(targed_person_id).push().setValue(notifiactions).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()){
                                                       send_req.setEnabled(true);
                                                       relation_state = "request_sent";

                                                       send_req.setText("cancel demand");
                                                       dec_req.setVisibility(View.INVISIBLE);
                                                       dec_req.setEnabled(false);
                                                   }
                                                    }
                                                });

                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
