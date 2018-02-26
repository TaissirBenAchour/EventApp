package com.example.cassio.Graduation_Project.StartApp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.FragmentsUnionActivity;
import com.example.cassio.Graduation_Project.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class loginActivity extends AppCompatActivity {
    private static final String TAG = "loginActivity";
    private Button loginBtn , aboutus;
    private Button registerBtn;
    private EditText userEmail;
    private EditText userPassword;
    private ProgressDialog progressing;
    private FirebaseAuth mAuth;
    private DatabaseReference usersReference;
    LoginButton loginButton;
    CallbackManager callbackManager;


    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         callbackManager= CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        loginBtn = (Button) findViewById(R.id.btn_login);
        aboutus =(Button) findViewById(R.id.about_us_id);
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
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAboutusActivity = new Intent(loginActivity.this, AboutUsActivity.class);
                startActivity(goToAboutusActivity);
            }
        });
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "User ID: " +
                        loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + loginResult.getAccessToken().getToken());
                        handleFacebookAccessToken(loginResult.getAccessToken());
                      }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(loginActivity.this, "Failed loging: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                return;            }
        });


    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            String current_id = mAuth.getCurrentUser().getUid();
                            String dviceToken = FirebaseInstanceId.getInstance().getToken();

                            usersReference.child(current_id).child("deviceToken").setValue(dviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent goToProfileIntent = new Intent(loginActivity.this,Afterloggingwithfacebook.class);
                                    goToProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(goToProfileIntent);
                                    finish();
                                }
                            });

                        }


                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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

