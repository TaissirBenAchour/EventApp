package com.example.cassio.Graduation_Project.AccountFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.Requests;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class FreindsRequestFragment extends Fragment {

    String my_id;
    private View mView;
    private DatabaseReference CommunityRef, UsersRef, Community_Ref, CommunityReq_Ref;
    private FirebaseAuth mAuth;
    private RecyclerView listOfrequests;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_freinds_request, container, false);
        listOfrequests = (RecyclerView) mView.findViewById(R.id.myListRequestView_id);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersRef.keepSynced(true);
        CommunityReq_Ref = FirebaseDatabase.getInstance().getReference().child("join_Community_requests").child(my_id);
        CommunityRef = FirebaseDatabase.getInstance().getReference().child("join_Community_requests");
        CommunityReq_Ref.keepSynced(true);
        Community_Ref = FirebaseDatabase.getInstance().getReference().child("Community");
        Community_Ref.keepSynced(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        listOfrequests.setLayoutManager(layoutManager);
        listOfrequests.setHasFixedSize(true);

        return mView;
    }

    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Requests, RequestsViewHolder> firebasereqRecyclerAdapter =
                new FirebaseRecyclerAdapter<Requests, RequestsViewHolder>

                        (
                                Requests.class,
                                R.layout.request_layout,
                                RequestsViewHolder.class,
                                CommunityReq_Ref) {
                    @Override
                    protected void populateViewHolder(final RequestsViewHolder viewHolder, Requests model, final int position) {
                        final String req_id = getRef(position).getKey();

                        DatabaseReference typeRef = getRef(position).child("request_type").getRef();
                        typeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String type = dataSnapshot.getValue().toString();


                                    if (type.equals("request_recieved")) {

                                        UsersRef.child(req_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String name = dataSnapshot.child("userName").getValue().toString();
                                                String status = dataSnapshot.child("userStatus").getValue().toString();
                                                String image = dataSnapshot.child("userImage").getValue().toString();


                                                viewHolder.setUserName(name);
                                                viewHolder.setUserStatus(status);
                                                viewHolder.setUserImage(image, getContext());
                                                viewHolder.AcceptBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Calendar date = Calendar.getInstance();
                                                        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
                                                        final String saveCurrentDate = currentDate.format(date.getTime());

                                                        Community_Ref.child(my_id).child(req_id).child("date").setValue(saveCurrentDate)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {


                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Community_Ref.child(req_id).child(my_id).child("date").setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                CommunityRef.child(my_id).child(req_id).child("request_type").removeValue();
                                                                                Toast.makeText(getContext(), my_id + "\n" + req_id, Toast.LENGTH_SHORT).show();
                                                                                CommunityRef.child(req_id).child(my_id).child("request_type").removeValue();

                                                                            }
                                                                        });

                                                                    }
                                                                });


//
                                                    }
                                                });


                                                viewHolder.DeclineBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        CommunityReq_Ref.child(my_id).child(req_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    CommunityReq_Ref.child(req_id).child(my_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(getContext(), "cancel successful", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else if (type.equals("request_sent")) {
                                        viewHolder.DeclineBtn.setVisibility(View.INVISIBLE);


                                        UsersRef.child(req_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String name = dataSnapshot.child("userName").getValue().toString();
                                                String status = dataSnapshot.child("userStatus").getValue().toString();
                                                String image = dataSnapshot.child("userImage").getValue().toString();


                                                viewHolder.setUserName(name);
                                                viewHolder.setUserStatus(status);
                                                viewHolder.setUserImage(image, getContext());

                                                viewHolder.AcceptBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        CharSequence options[] = new CharSequence[]{

                                                                "cancel Req"

                                                        };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                        builder.setTitle("Verification");
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                if (i == 0) {
                                                                    CommunityReq_Ref.child(my_id).child(req_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                CommunityReq_Ref.child(req_id).child(my_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {


                                                                                            Toast.makeText(getContext(), "cancel successful", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    });

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
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                };

        listOfrequests.setAdapter(firebasereqRecyclerAdapter);


    }


    public static class RequestsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton AcceptBtn;
        ImageButton DeclineBtn;

        public RequestsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            AcceptBtn = (ImageButton) mView.findViewById(R.id.acceptbtn_id);
            DeclineBtn = (ImageButton) mView.findViewById(R.id.declinebtn_id);
        }

        public void setUserStatus(String userStatus) {
            TextView user_status = (TextView) mView.findViewById(R.id.status_request_id);
            user_status.setText(userStatus);
        }

        public void setUserName(String userName) {
            TextView username = (TextView) mView.findViewById(R.id.name_request_id);
            username.setText(userName);
        }

        public void setUserImage(final String userImage, final Context context) {
            final CircleImageView image = (CircleImageView) mView.findViewById(R.id.photo_request_id);


            if (!image.equals("profile_pic")) {

                Picasso.with(context).load(userImage).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.profile_pic).into(image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(userImage).placeholder(R.drawable.profile_pic).into(image);
                    }
                });

            }
        }


    }


}
