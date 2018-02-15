package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllAppUsersActivity extends Fragment {


    private Toolbar mtool;
    private RecyclerView listUsers;
    private DatabaseReference allusersDBReference;

    View mView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.activity_all_ausers, container, false);


        mtool = (Toolbar) mView.findViewById(R.id.all_users);
        listUsers = (RecyclerView) mView.findViewById(R.id.recycle_id);


        listUsers.setHasFixedSize(true);
        listUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        allusersDBReference = FirebaseDatabase.getInstance().getReference().child("Users");
        allusersDBReference.keepSynced(true);

return mView;
    }


    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerAdapter<AllUsersClass, AllUserViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllUsersClass, AllUserViewHolder>
                (
                        AllUsersClass.class,
                        R.layout.dispaly_users_layout,
                        AllUserViewHolder.class,
                        allusersDBReference
                ) {
            @Override // to set values for the recycler views
            // so after setting the parameters , i have to fill up the fields ; a55 !
            protected void populateViewHolder(final AllUserViewHolder viewHolder, AllUsersClass model, final int position) {
                viewHolder.setUser_name(model.getUserName());
                viewHolder.setUser_status(model.getUserStatus());
                viewHolder.setUser_Image(getContext(), model.getUserImage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String targed_person_id = getRef(position).getKey();
                        Intent goToTargetPersonProfile = new Intent(getContext(), friendsProfileActivity.class);
                        goToTargetPersonProfile.putExtra("targed_person_id", targed_person_id);
                        startActivity(goToTargetPersonProfile);
                    }
                });

            }


        };
        listUsers.setAdapter(firebaseRecyclerAdapter);
    }


    public static class AllUserViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public AllUserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }


        public void setUser_name(String userName) {
            TextView name = (TextView) mView.findViewById(R.id.all_users_name);
            name.setText(userName);
        }

        public void setUser_status(String userStatus) {
            TextView status = (TextView) mView.findViewById(R.id.all_users_status);
            status.setText(userStatus);
        }

        public void setUser_Image(final Context context, final String userImage) {
            final CircleImageView image = (CircleImageView) mView.findViewById(R.id.all_users_image);


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
