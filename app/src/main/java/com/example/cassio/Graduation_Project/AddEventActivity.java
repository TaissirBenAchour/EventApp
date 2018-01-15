package com.example.cassio.Graduation_Project;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.AddressListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static final int MULTIPLE_PERMISSIONS = 10;
    private final static int galery_pick = 1;
    private static final String TAG = "AddEventActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private ImageButton btnDatePicker, btnTimePicker;
    private EditText txtDate, txtTime;
    private FirebaseAuth mAuth;
    private StorageReference mStoreEventReference;
    private Toolbar mtoolbar;
    private ImageButton imageOfEvent;
    private EditText eventTitle;
    private EditText eventDescription;
    private EditText eventPrice;
    private Uri imageUri;
    private DatabaseReference mStoreEvent_dataReference;
    private String[] permissionsList = new String[]{
            READ_EXTERNAL_STORAGE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION};
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addEventButton;
    private ProgressDialog progressing;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private AddressListAdapter mAddressListAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();

        mStoreEventReference = FirebaseStorage.getInstance().getReference().child("Events");
        mStoreEvent_dataReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mStoreEvent_dataReference.keepSynced(true);

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
        txtDate = (EditText) findViewById(R.id.setDate);
        txtTime = (EditText) findViewById(R.id.setTime);

        mGoogleApiClient = new GoogleApiClient.Builder(AddEventActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAddressListAdapter = new AddressListAdapter(AddEventActivity.this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mAddressListAdapter);


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
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

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

    public void eventPlan() {


        progressing.setMessage(" image is being downloading .. ");
        progressing.show();
        final String title = eventTitle.getText().toString().trim();
        final String description = eventDescription.getText().toString().trim();
        final String price = eventPrice.getText().toString().trim();
        final String date = txtDate.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        final String address = mAutocompleteTextView.getText().toString().trim();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(price)
                && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(address)) {

            StorageReference filePath = mStoreEventReference.child("imageevent_" + title);
//            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUri = taskSnapshot.getDownloadUrl();
            DatabaseReference newEvent = mStoreEvent_dataReference.push(); // push() for unique random  ID
            newEvent.child("title").setValue(title);
            newEvent.child("description").setValue(description);
            newEvent.child("price").setValue(price);
            newEvent.child("imageEvent").setValue("imageplaceholder");
            newEvent.child("date").setValue(date);
            newEvent.child("time").setValue(time);
            newEvent.child("address").setValue(address);
            // newEvent.child("imageEvent").setValue(downloadUri.toString());
            progressing.dismiss();
            Intent goBackToEventList = new Intent(AddEventActivity.this, AvailableEventActivity.class);
            startActivity(goBackToEventList);
//                }
//            });


        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mAddressListAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Google Places API connection failed" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAddressListAdapter.setGoogleApiClient(null);
    }


    public boolean checkPermission(final Context context) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissionsList) {
            result = ContextCompat.checkSelfPermission(AddEventActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (imageReturnedIntent != null) {
            switch (requestCode) {
                case galery_pick:
                    if (resultCode == RESULT_OK) {
                        imageUri = imageReturnedIntent.getData();
                        imageOfEvent.setImageURI(imageUri);
                    }
            }
        }
    }



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                return;
            }
            CharSequence attributions = places.getAttributions();

            if (attributions != null) {
            }
        }
    };


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AddressListAdapter.PlaceAutocomplete item = mAddressListAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

}




