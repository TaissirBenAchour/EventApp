package com.example.cassio.Graduation_Project;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View mView;
    RecyclerView recyclerView;
    DatabaseReference communityRef, userRef, postRef;
    String my_id;
    FirebaseAuth mAuth;


    private HomeAdapter mAdapter;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        communityRef.child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 final List<HomeListPost> homeListPostList = new ArrayList<>();
                 for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                     postRef.child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
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



        return mView;
    }








//    public static class homelistViewHolder extends RecyclerView.ViewHolder {
//        View mView;
//
//        public homelistViewHolder(View itemView) {
//            super(itemView);
//            mView = itemView;
//        }
//
//        public void setUserName(String userName) {
//            TextView name = (TextView) mView.findViewById(R.id.name_id);
//            name.setText(userName);
//        }
//
//        public void setEvent(String userName) {
//            TextView post = (TextView) mView.findViewById(R.id.post_id);
//            post.setText(userName);
//        }
//
//
//    }

}
