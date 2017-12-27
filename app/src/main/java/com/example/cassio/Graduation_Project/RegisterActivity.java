package com.example.cassio.Graduation_Project;

/**
 * Created by cassio on 02/12/17.
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDefaultDataReference;

    private Toolbar mtoolbar;
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private Button registerBtn;
    private ProgressDialog progressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mtoolbar = (Toolbar) findViewById(R.id.toolbar_id);
        userName = (EditText) findViewById(R.id.user_name);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPassword = (EditText) findViewById(R.id.user_password);
        registerBtn = (Button) findViewById(R.id.btn_createAccount);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("main Page");
        // I have a prob here : I ll come back an other time to it :p
        // it is quitting the app instead of going back to login activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressing = new ProgressDialog(this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = userName.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();
                userAccount(name, email, password);

            }
        });


    }

    private void userAccount(final String name, String email, String password) {

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please check your name field ",
                    Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please check your email field ",
                    Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please check your password field ",
                    Toast.LENGTH_LONG).show();
        } else {
            progressing.setTitle("Create Account ");
            progressing.setMessage(" We are creating your account , thanks for joining us");
            progressing.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(RegisterActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                String dviceToken = FirebaseInstanceId.getInstance().getToken();

                                // store data !
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                storeUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID); // create reference and store it in that variable
                                storeUserDefaultDataReference.child("userName").setValue(name);
                                storeUserDefaultDataReference.child("userStatus").setValue(" I am productive !");
                                storeUserDefaultDataReference.child("userImage").setValue("profile_pic");
                                storeUserDefaultDataReference.child("deviceToken").setValue(dviceToken);
                                storeUserDefaultDataReference.child("userThumbImage").setValue("default_image") // from crop image
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // check if data is well stored in database
                                                if (task.isSuccessful()) {


                                                    Intent goToProfileIntent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                                    goToProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(goToProfileIntent);
                                                    finish();

                                                }
                                            }
                                        });


                            }
                            progressing.dismiss();
                        }


                    });
        }

    }

}
