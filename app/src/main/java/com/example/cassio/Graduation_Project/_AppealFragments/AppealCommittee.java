package com.example.cassio.Graduation_Project._AppealFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cassio.Graduation_Project.FragmentsUnionActivity;
import com.example.cassio.Graduation_Project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AppealCommittee extends Fragment {


    View mView ;
    EditText txt;
    Button btn;
    DatabaseReference comRef;
    String my_id;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_appeal_committee, container, false);

        txt = (EditText)mView.findViewById(R.id.testtxt);
        btn =(Button) mView.findViewById(R.id.testadd);
        comRef = FirebaseDatabase.getInstance().getReference().child("CommitteeAppeal");
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String test = txt.getText().toString().trim();


                DatabaseReference newAppeal = comRef.child(my_id).push();

                newAppeal.child("test").setValue(test);


                Intent goBackToProfile = new Intent(getContext(), FragmentsUnionActivity.class);
                startActivity(goBackToProfile);



            }
        });

        return mView;

    }

}
