package com.example.cassio.Graduation_Project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class loginActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button registerBtn;
    private EditText userEmail;
    private EditText userPassword;
    private ProgressDialog progressing;
    private FirebaseAuth mAuth;
    private DatabaseReference usersReference;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.btn_login);
        registerBtn = (Button) findViewById(R.id.btn_register);

        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        progressing = new ProgressDialog(this);
        usersReference= FirebaseDatabase.getInstance().getReference().child("Users");

        registerBtn.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent goToRegisterActivityIntent = new Intent(loginActivity.this, RegisterActivity.class);
                                               startActivity(goToRegisterActivityIntent);
                                               finish();
                                           }
                                       }

        );
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                loginUserAccount(email, password);
            }
        });


    }

    private void loginUserAccount(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please check your email field ",
                    Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please check your password field ",
                    Toast.LENGTH_LONG).show();
        } else {
            progressing.setTitle("Loging Account ");
            progressing.setMessage(" We are loging in to your account");
            progressing.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(loginActivity.this, "Failed loging: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            else {
                                String current_id = mAuth.getCurrentUser().getUid();
                                String dviceToken = FirebaseInstanceId.getInstance().getToken();
                                usersReference.child(current_id).child("deviceToken").setValue(dviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent goToProfileIntent = new Intent(loginActivity.this,FragmentsUnionActivity.class);
                                        goToProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(goToProfileIntent);
                                        finish();
                                    }
                                });

                            }
                            progressing.dismiss();
                        }


                    });
    }}

    @Override
    protected void onStart() {
        super.onStart();

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            startActivity(new Intent(loginActivity.this, FragmentsUnionActivity.class));
        } else {
            //returns to login
        }
    }


}

