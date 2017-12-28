package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.firebase.ui.database.FirebaseListAdapter;
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
    private String my_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myListView=inflater.inflate(R.layout.community_layout, null);
        listOfJoinedPersons = (RecyclerView) myListView.findViewById(R.id.myListView_id);
        mAuth = FirebaseAuth.getInstance();
        my_id=mAuth.getCurrentUser().getUid();
        CommunityRef = FirebaseDatabase.getInstance().getReference().child("Cummunity").child(my_id);
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        return myListView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CommunityListClass,CommunityViewHolder > firebaseListAdapter
                = new FirebaseRecyclerAdapter<CommunityListClass, CommunityViewHolder> (
                CommunityListClass.class,
                R.layout.dispaly_users_layout,
                CommunityViewHolder.class,
                CommunityRef)  {
            @Override
            protected void populateViewHolder(CommunityViewHolder viewHolder, CommunityListClass model, int position) {
                viewHolder.setDate(model.getDate());
                String listCommunityUid=getRef(position).getKey();
                UsersRef.child(listCommunityUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("userName").getValue().toString();
                        String image = dataSnapshot.child("userImage").getValue().toString();

                        CommunityViewHolder.setName(name);
                        CommunityViewHolder.setImage(image,getContext());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        };
        listOfJoinedPersons.setAdapter(firebaseListAdapter);
    }

    public static class CommunityViewHolder extends RecyclerView.ViewHolder{
        static View mView;
        public CommunityViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setDate(String date){
            TextView datePlacement=(TextView) mView.findViewById(R.id.all_users_status);
            datePlacement.setText(date);
        }
        public static void setName(String user_name){
            TextView username=(TextView) mView.findViewById(R.id.all_users_name);
            username.setText(user_name);
        }
        public static void setImage(final String user_image, final Context context){
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