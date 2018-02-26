package com.example.cassio.Graduation_Project.StartApp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.SponsoringActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Afterloggingwithfacebook extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference DefaultReference;


    private EditText userName;
    private Button registerBtn;
    private RadioGroup userStatus;
    private String _status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterloggingwithfacebook);
        mAuth = FirebaseAuth.getInstance();
        userName = (EditText) findViewById(R.id.user_name);
        userStatus = (RadioGroup) findViewById(R.id.user_status);
        registerBtn = (Button) findViewById(R.id.btn_createAccount);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFormStatus();
                String dviceToken = FirebaseInstanceId.getInstance().getToken();
                final String status = _status;
                final String name = userName.getText().toString();
                final String currentUserID = mAuth.getCurrentUser().getUid();
                DefaultReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
                DefaultReference.child("userName").setValue(name);
                DefaultReference.child("userStatus").setValue(status);
                DefaultReference.child("userImage").setValue("profile_pic");
                DefaultReference.child("deviceToken").setValue(dviceToken);
                DefaultReference.child("usergender").setValue("");
                DefaultReference.child("userId").setValue(currentUserID);
                DefaultReference.child("userPhone").setValue("");
                DefaultReference.child("userId").setValue(currentUserID)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Intent goToProfileIntent = new Intent(Afterloggingwithfacebook.this, SponsoringActivity.class);
                                    goToProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    goToProfileIntent.putExtra("id",currentUserID);
                                    startActivity(goToProfileIntent);
                                    finish();

                                }
                            }
                        });

            }
        });

    }
    private void submitFormStatus() {
        int selectedId = userStatus.getCheckedRadioButtonId();

        if(selectedId == R.id.event_planner_radio_btn)
            _status = "Event planner";
        else
            _status = "Event Seeker";
    }
}
