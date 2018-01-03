package com.example.cassio.Graduation_Project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback  {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int MY_PERMISSIONS_REQUEST_GOOGLE_MAP= 978;
    public static final int MY_PERMISSIONS_CODE= 128;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private final static int galery_pick = 1;
    private static final String TAG = "AddEventActivity";
    ImageButton btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private FirebaseAuth mAuth;
    private StorageReference mStoreEventReference;
    private Toolbar mtoolbar;
    private ImageButton imageOfEvent;
    private EditText eventTitle;
    private EditText eventDescription;
    private EditText eventPrice;
    private Uri imageUri;
    private DatabaseReference mStoreEvent_dataReference;
   private String[] permissionsList= new String[]{
            READ_EXTERNAL_STORAGE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION};
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Button addEventButton;

    private ProgressDialog progressing;
    private boolean permessionGranted=false;
    private GoogleMap gMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "map is ready !", Toast.LENGTH_SHORT).show();
        gMap=googleMap;


    }

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
        btnDatePicker = (ImageButton) findViewById(R.id.image_Button_date);
        btnTimePicker = (ImageButton) findViewById(R.id.image_Button_time);
        txtDate= (EditText) findViewById(R.id.setDate);
        txtTime = (EditText) findViewById(R.id.setTime);

if (checkGoogleMap()){
checkPermission(getApplicationContext());}

        imageOfEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission(AddEventActivity.this)) {

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

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
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
        final String date = txtDate.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(price)
                && imageOfEvent != null && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time)) {

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
                    newEvent.child("date").setValue(date);
                    newEvent.child("time").setValue(time);
                   // newEvent.child("imageEvent").setValue(downloadUri.toString());
                    progressing.dismiss();
                    Intent goBackToEventList = new Intent(AddEventActivity.this,AvailableEventActivity.class);
                    startActivity(goBackToEventList);
                }
            });


        }
    }


    public boolean checkPermission(final Context context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissionsList) {
            result = ContextCompat.checkSelfPermission(AddEventActivity.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
                initMap();
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {


        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    String _permissions = "";
                    for (String per : permissionsList) {
                        _permissions += "\n" + per;
                    }
                    // permissions list of don't granted permission
                }
                return;
            }
        }

    }

    public void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(AddEventActivity.this);
    }

    public boolean checkGoogleMap ()
    {
        Log.d(TAG,"checking google service version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(AddEventActivity.this);
        if (available== ConnectionResult.SUCCESS){
            Log.d(TAG,"google play services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Log.d(TAG,"Error has occcured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(AddEventActivity.this,available,MY_PERMISSIONS_REQUEST_GOOGLE_MAP);
            dialog.show();

        }
        else {
            Toast.makeText(this, "we can not make a request for google map", Toast.LENGTH_SHORT).show();

        }return false;
        }




}




