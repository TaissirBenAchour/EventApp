package com.example.cassio.Graduation_Project.profileFragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.FriendsProfileActivity;
import com.example.cassio.Graduation_Project.MessagesActivity;
import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class infosFragment extends Fragment {

    View view;
    DatabaseReference userRef,friendRef;
    TextView status,gender,description,nofriends;
    String phone;
    ImageButton callbtn,messagebtn;
    FirebaseAuth mAuth;
    String myId;
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_infos, container, false);

        final FriendsProfileActivity activity = (FriendsProfileActivity) getActivity();
        final String myDataFromActivity = activity.getMyData();
        final  String name = activity.getMyDataname();
        description=(TextView)view.findViewById(R.id.descp_user_id);
        status=(TextView)view.findViewById(R.id.status_id);
        gender=(TextView)view.findViewById(R.id.gender_id);
        callbtn = (ImageButton) view.findViewById(R.id.call_phone);
        nofriends=(TextView)view.findViewById(R.id.no_friends);
        messagebtn=(ImageButton)view.findViewById(R.id.message_user_id);
        recyclerView =(RecyclerView) view.findViewById(R.id.friend_list) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAuth=FirebaseAuth.getInstance();
        myId=mAuth.getCurrentUser().getUid();


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        friendRef = FirebaseDatabase.getInstance().getReference().child("Community").child(myDataFromActivity);


        FirebaseRecyclerAdapter<AllUsersClass,userViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsersClass, userViewHolder>
                (
                        AllUsersClass.class,
                        R.layout.freind_layout,
                        userViewHolder.class,
                        friendRef

                ) {
            @Override
            protected void populateViewHolder(final userViewHolder viewHolder, final AllUsersClass model, int position) {
                if (!friendRef.child(myDataFromActivity).equals(null)){

                    nofriends.setVisibility(View.INVISIBLE);
                    userRef.child(getRef(position).getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String image = dataSnapshot.child("userImage").getValue().toString();
                            viewHolder.setImage(image,getContext());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }



            }
        };recyclerView.setAdapter(firebaseRecyclerAdapter);

        userRef.child(myDataFromActivity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String _status=dataSnapshot.child("userStatus").getValue().toString();
                String _gender=dataSnapshot.child("usergender").getValue().toString();
                String _contact= dataSnapshot.child("userPhone").getValue().toString();
                String _bio=dataSnapshot.child("userBio").getValue().toString();
                phone = _contact;
                status.setText(_status);
                gender.setText(_gender);
                if (!_bio.equals("")){
                    description.setText(_bio);
                }
                else {
                    description.setText("no bio is defined");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       callbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               callMe();
           }
       });
       messagebtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Bundle bundle =new Bundle();
               bundle.putString("targed_person_id",myDataFromActivity);
               bundle.putString("user_name",name);
               Intent intent = new Intent(getContext(), MessagesActivity.class);
               intent.putExtras(bundle);
               startActivity(intent);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

           }
       });
        return view;
    }

    public void callMe(){
        try {

            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phone.trim()));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);

        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }
    public  static class  userViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public userViewHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
        }
        public void setImage(String image, Context context){
            CircleImageView _image = (CircleImageView) mView.findViewById(R.id.image_friend_id);
            Picasso.with(context).load(image).placeholder(R.drawable.profile_pic).into(_image);
        }
    }

}
