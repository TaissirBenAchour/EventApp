package com.example.cassio.Graduation_Project;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
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

public class FriendsProfileActivity extends AppCompatActivity {
    TextView rated, numevents;
    RatingBar ratingbar;
    float i = 0;
    private TextView send_req;
    private TextView dec_req;
    private TextView username;
    private TextView desc;
    private DatabaseReference referenceTolistReqs, referenceToUsersList, rateRef, communityDBReference, eventsRef;
    private String relation_state;
    private FirebaseAuth mAuth;
    private String my_current_id, targed_person_id, name;
    private DatabaseReference notificationReference;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private ProfileTabAdapter mainPagerAdapter;
    private CircleImageView imageProfileFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        rated = (TextView) findViewById(R.id.rated_id);
        ratingbar = (RatingBar) findViewById(R.id.ratebar);
        numevents = (TextView) findViewById(R.id.numevents);
        send_req = (TextView) findViewById(R.id.add_request);
        dec_req = (TextView) findViewById(R.id.clear_request);
        username = (TextView) findViewById(R.id.username_id);
        desc = (TextView) findViewById(R.id.userstatus_id);
        myMainViewPager = (ViewPager) findViewById(R.id.viewpager_fragment);
        mainPagerAdapter = new ProfileTabAdapter(getSupportFragmentManager());
        myMainViewPager.setAdapter(mainPagerAdapter);
        mytabLayout = (TabLayout) findViewById(R.id.main_tab_id);
        mytabLayout.setupWithViewPager(myMainViewPager);
        myMainViewPager.setAdapter(mainPagerAdapter);
        mytabLayout.getTabAt(0).setIcon(R.drawable.ic_perm_identity_black_24dp);
        mytabLayout.getTabAt(1).setIcon(R.drawable.ic_info_outline_black_24dp);
        imageProfileFriend = (CircleImageView) findViewById(R.id.circleImageView);
        mAuth = FirebaseAuth.getInstance();
        my_current_id = mAuth.getCurrentUser().getUid();
        targed_person_id = getIntent().getExtras().get("targed_person_id").toString();
        name = getIntent().getExtras().get("user_name").toString();


        referenceToUsersList = FirebaseDatabase.getInstance().getReference().child("Users");
        referenceTolistReqs = FirebaseDatabase.getInstance().getReference().child("join_Community_requests");
        communityDBReference = FirebaseDatabase.getInstance().getReference().child("Community");
        notificationReference = FirebaseDatabase.getInstance().getReference().child("Notification");
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");
        eventsRef = FirebaseDatabase.getInstance().getReference().child("Events");

        notificationReference.keepSynced(true);
        relation_state = "unfriend";

        dec_req.setVisibility(View.INVISIBLE);
        dec_req.setEnabled(false);
        if (!my_current_id.equals(targed_person_id)) {


            send_req.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    send_req.setEnabled(false);
                    if (relation_state.equals("unfriend")) {
                        sendRequest();
                    }

                    if (relation_state.equals("request_sent")) {
                        cancelreq();
                    }

                    if (relation_state.equals("request_recieved")) {
                        acceptReq();

                    }
                    if (relation_state.equals("friends")) {
                        unJoinComunity();
                    }

                }
            });
        } else {
            dec_req.setVisibility(View.INVISIBLE);
            send_req.setVisibility(View.INVISIBLE);
        }


        eventsRef.child(targed_person_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshotevents : dataSnapshot.getChildren()) {
                    long num_events = dataSnapshot.getChildrenCount();
                    numevents.setText(String.valueOf(num_events));
                    if (num_events != 0) {
                        rateRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshotrates : dataSnapshot.getChildren()) {

                                    if (snapshotevents.getKey().equals(snapshotrates.getKey())) {
                                        rateRef.child(snapshotrates.getKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    long tuto = dataSnapshot.getChildrenCount();
                                                    String rate = snapshot.child("rate").getValue().toString();
                                                    float ratenumber = Float.valueOf(rate);
                                                    i = (i + ratenumber) / tuto;
                                                    String j = String.valueOf(i);
                                                    rated.setText(j);
                                                    ratingbar.setRating(i);


                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                    }

                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        referenceToUsersList.child(targed_person_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("userName").getValue().toString();
                final String image = dataSnapshot.child("userImage").getValue().toString();
                String status = dataSnapshot.child("userStatus").getValue().toString();


                username.setText(name);
                desc.setText(status);
                Picasso.with(FriendsProfileActivity.this).load(image).placeholder(R.drawable.profile_pic).into(imageProfileFriend);
                referenceTolistReqs.child(my_current_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(targed_person_id)) {
                            String req_type = dataSnapshot.child(targed_person_id).child("request_type").getValue().toString();
                            if (req_type.equals("request_sent")) {
                                relation_state = "request_sent";
                                send_req.setText("cancel sent request");
                               send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp,0,0,0);
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            } else if (req_type.equals("request_recieved")) {
                                relation_state = "request_recieved";
                                send_req.setText("Accept");
                                send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_black_24dp,0,0,0);
                                dec_req.setVisibility(View.VISIBLE);
                                dec_req.setEnabled(true);
                                dec_req.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        refuseJoinCommunity();
                                    }
                                });
                            }

                        } else {
                            communityDBReference.child(my_current_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(targed_person_id)) {
                                        relation_state = "friends";
                                        send_req.setText("unfriend");
                                        send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp,0,0,0);
                                        dec_req.setVisibility(View.INVISIBLE);
                                        dec_req.setEnabled(false);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void refuseJoinCommunity() {
        referenceTolistReqs.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                relation_state = "unfriend";
                                send_req.setEnabled(true);
                                send_req.setText("add");
                                send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_add_black_24dp,0,0,0);
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });

    }

    public String getMyData() {
        return targed_person_id;
    }
    public String getMyDataname() {
        return name;
    }

    private void unJoinComunity() {

        communityDBReference.child(my_current_id).child(targed_person_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    communityDBReference.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                send_req.setEnabled(true);
                                relation_state = "unfriend";
                                send_req.setText("add");
                                send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_add_black_24dp,0,0,0);
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
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
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
                                        if (task.isSuccessful()) {
                                            referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        send_req.setEnabled(true);
                                                        relation_state = "friends";
                                                        send_req.setText("unfriend");
                                                        send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp,0,0,0);
                                                        dec_req.setVisibility(View.INVISIBLE);
                                                        dec_req.setEnabled(false);
                                                    }
                                                }
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
                if (task.isSuccessful()) {
                    referenceTolistReqs.child(targed_person_id).child(my_current_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                relation_state = "unfriend";
                                send_req.setEnabled(true);
                                send_req.setText("add");
                                send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_add_black_24dp,0,0,0);
                                dec_req.setVisibility(View.INVISIBLE);
                                dec_req.setEnabled(false);
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendRequest() {
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


                                                HashMap<String, String> notifiactions = new HashMap<String, String>();
                                                notifiactions.put("from", my_current_id);
                                                notifiactions.put("type", "request");
                                                notificationReference.child(targed_person_id).push().setValue(notifiactions).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            send_req.setEnabled(true);
                                                            relation_state = "request_sent";
                                                            send_req.setText("cancel sent request");
                                                            send_req.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_24dp,0,0,0);
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
