package com.example.cassio.Graduation_Project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public  static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private final static int galery_pick = 1;
    private CircleImageView imageProfileSittings;
    private TextView userNameSettings,statusUserSettings,save;
    private ImageButton changePictureBtnSettings;
    private EditText newNametxt,newBiotxt,newPhonetxt,newEmailtxt;
    private Spinner spinner;
    private DatabaseReference userRef;
    private StorageReference storedProfileReference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Uri SelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        String get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
        userRef.keepSynced(true);
        storedProfileReference = FirebaseStorage.getInstance().getReference().child("Profile_images");
        imageProfileSittings = (CircleImageView) findViewById(R.id.imageProfile);
        userNameSettings = (TextView)findViewById(R.id.userName);
        statusUserSettings = (TextView)findViewById(R.id.userStatus);
        changePictureBtnSettings = (ImageButton) findViewById(R.id.changeImage);
        newNametxt = (EditText) findViewById(R.id.newName_id);
        newPhonetxt = (EditText) findViewById(R.id.newphone_id);
        newEmailtxt =(EditText) findViewById(R.id.newemail_id);

        newBiotxt =(EditText) findViewById(R.id.bio_id);
        spinner = (Spinner) findViewById(R.id.spinner_status_id);
        save=(TextView) findViewById(R.id.save_id);
        spinner.setOnItemSelectedListener(SettingsActivity.this);
        List<String> status = new ArrayList<String>();
        status.add("Event planner");
        status.add("Event Seeker ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = newNametxt.getText().toString().trim();
                String newBio = newBiotxt.getText().toString().trim();
                String newPhone = newPhonetxt.getText().toString().trim();
                String newEmail = newEmailtxt.getText().toString().trim();
                userRef.child("userName").setValue(newName);
                userRef.child("userBio").setValue(newBio);
                userRef.child("userPhone").setValue(newPhone);
                userRef.child("userEmail").setValue(newEmail);
                Intent intent = new Intent(SettingsActivity.this,FragmentsUnionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Toast.makeText(SettingsActivity.this, "change saved", Toast.LENGTH_SHORT).show();


            }
        });


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("userName").getValue().toString();
                final String image = dataSnapshot.child("userImage").getValue().toString();
                String status = dataSnapshot.child("userStatus").getValue().toString();

                userNameSettings.setText(name);
                statusUserSettings.setText(status);

                if (!image.equals("profile_pic")){

                    Picasso.with(SettingsActivity.this).load(image)
                            .networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.profile_pic).
                            into(imageProfileSittings, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {  Picasso.with(SettingsActivity.this).load(image)
                                        .placeholder(R.drawable.profile_pic).
                                                into(imageProfileSittings);

                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        changePictureBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionREAD_EXTERNAL_STORAGE(SettingsActivity.this)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, galery_pick);                }



            }
        });


    }








    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case galery_pick: if (imageReturnedIntent!=null) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(SettingsActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
                    SelectedImageUri = imageReturnedIntent.getData();
                }


                String user_id = mAuth.getCurrentUser().getUid();
                StorageReference filePath = storedProfileReference.child(user_id + ".jpg");


                progressDialog = new ProgressDialog(this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);
                final UploadTask uploadTask = filePath.putFile(SelectedImageUri);

                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.incrementProgressBy(15);
                    }
                });

                filePath.putFile(SelectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.incrementProgressBy(15);
                            Toast.makeText(SettingsActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            String downloadPic = task.getResult().getDownloadUrl().toString();
                            userRef.child("userImage").setValue(downloadPic)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(SettingsActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


            }

default:break;


                }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(SettingsActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    // For Permission access to galery ! ( api 23 and above )

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }
    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String newStatus = parent.getItemAtPosition(position).toString();
        userRef.child("userStatus").setValue(newStatus);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}
