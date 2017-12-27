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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventActivity extends AppCompatActivity {


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private final static int galery_pick = 1;
    private FirebaseAuth mAuth;
    private StorageReference mStoreEventReference;
    private Toolbar mtoolbar;
    private ImageButton imageOfEvent;
    private EditText eventTitle;
    private EditText eventDescription;
    private EditText eventPrice;
    private Uri imageUri;
    private DatabaseReference mStoreEvent_dataReference;


    private Button addEventButton;

    private ProgressDialog progressing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();

        //FOR STORAGE
        mStoreEventReference = FirebaseStorage.getInstance().getReference().child("Events");
        //fOR DATABASE
        mStoreEvent_dataReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mStoreEvent_dataReference.keepSynced(true); //for offline mode

        mtoolbar = (Toolbar) findViewById(R.id.toolbar_id);
        imageOfEvent = (ImageButton) findViewById(R.id.image_event_id);
        eventTitle = (EditText) findViewById(R.id.title_id);
        eventDescription = (EditText) findViewById(R.id.description_id);
        eventPrice = (EditText) findViewById(R.id.price_id);
        addEventButton = (Button) findViewById(R.id.add_id);

        setSupportActionBar(mtoolbar);
        progressing = new ProgressDialog(this);






        imageOfEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissionREAD_EXTERNAL_STORAGE(AddEventActivity.this)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, galery_pick);
                }
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                eventPlan();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case galery_pick:
                if (resultCode == RESULT_OK) {
                    imageUri = imageReturnedIntent.getData();
                    imageOfEvent.setImageURI(imageUri);
                }
        }
    }


    public void eventPlan() {




        progressing.setMessage(" image is being downloading .. ");
        progressing.show();
        final String title = eventTitle.getText().toString().trim();
        final String description = eventDescription.getText().toString().trim();
        final String price = eventPrice.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(price) && imageOfEvent != null) {

            StorageReference filePath = mStoreEventReference.child("imageevent_" + title);
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    DatabaseReference newEvent = mStoreEvent_dataReference.push(); // push() for unique random  ID
                    newEvent.child("title").setValue(title);
                    newEvent.child("description").setValue(description);
                    newEvent.child("price").setValue(price);
                    newEvent.child("imageEvent").setValue("imageplaceholder");
                   // newEvent.child("imageEvent").setValue(downloadUri.toString());
                    progressing.dismiss();
                    Intent goBackToEventList = new Intent(AddEventActivity.this,AvailableEventActivity.class);
                    startActivity(goBackToEventList);
                }
            });


        }
    }


    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{READ_EXTERNAL_STORAGE},
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
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(AddEventActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


}
