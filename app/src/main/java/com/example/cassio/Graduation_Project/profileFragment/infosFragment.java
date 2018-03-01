package com.example.cassio.Graduation_Project.profileFragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.FriendsProfileActivity;
import com.example.cassio.Graduation_Project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class infosFragment extends Fragment {

    View view;
    DatabaseReference userRef;
    TextView status,gender,description;
    String phone;
    ImageButton callbtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_infos, container, false);

        FriendsProfileActivity activity = (FriendsProfileActivity) getActivity();
        final String myDataFromActivity = activity.getMyData();
        description=(TextView)view.findViewById(R.id.descp_user_id);
        status=(TextView)view.findViewById(R.id.status_id);
        gender=(TextView)view.findViewById(R.id.gender_id);
        callbtn = (ImageButton) view.findViewById(R.id.call_phone);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        userRef.child(myDataFromActivity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String _status=dataSnapshot.child("userStatus").getValue().toString();
                String _gender=dataSnapshot.child("usergender").getValue().toString();
                String _contact= dataSnapshot.child("userPhone").getValue().toString();
                phone = _contact;

                status.setText(_status);
                gender.setText(_gender);
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
        return view;
    }

    public void callMe(){
        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(callIntent);

        } catch (ActivityNotFoundException activityException) {
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }
    }

}
