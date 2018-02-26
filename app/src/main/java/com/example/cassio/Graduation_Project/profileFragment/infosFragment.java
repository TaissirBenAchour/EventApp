package com.example.cassio.Graduation_Project.profileFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.FriendsProfileActivity;
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
    TextView name,status,gender,contact;
    TextView edit1,edit2,edit3,edit4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.fragment_infos, container, false);

        FriendsProfileActivity activity = (FriendsProfileActivity) getActivity();
        final String myDataFromActivity = activity.getMyData();
        name=(TextView)view.findViewById(R.id.name_id);
        status=(TextView)view.findViewById(R.id.status_id);
        gender=(TextView)view.findViewById(R.id.gender_id);
        contact=(TextView)view.findViewById(R.id.contact_id);
        edit1=(TextView)view.findViewById(R.id.edit_name_id);
        edit2=(TextView)view.findViewById(R.id.edit_status_id);
        edit3=(TextView)view.findViewById(R.id.edit_gender_id);
        edit4=(TextView)view.findViewById(R.id.edit_contact_id);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        userRef.child(myDataFromActivity).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String _name=dataSnapshot.child("userName").getValue().toString();
                String _status=dataSnapshot.child("userStatus").getValue().toString();
                String _gender=dataSnapshot.child("usergender").getValue().toString();
                String _contact=dataSnapshot.child("userPhone").getValue().toString();

                name.setText(_name);
                edit1.setVisibility(View.INVISIBLE);
                status.setText(_status);
                edit2.setVisibility(View.INVISIBLE);
                gender.setText(_gender);
                edit3.setVisibility(View.INVISIBLE);
                contact.setText(_contact);
edit4.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
