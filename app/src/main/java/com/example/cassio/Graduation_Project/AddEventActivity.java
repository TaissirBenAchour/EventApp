package com.example.cassio.Graduation_Project;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.AddressListAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class AddEventActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, AdapterView.OnItemSelectedListener {

    public static final int MULTIPLE_PERMISSIONS = 10;
    private final static int galery_pick = 1;
    private static final String TAG = "AddEventActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    String my_id ;
    String _Month;
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
    private DatabaseReference mStoreEvent_dataReference,postsRef;
    private String[] permissionsList = new String[]{
            READ_EXTERNAL_STORAGE,
            ACCESS_COARSE_LOCATION,
            ACCESS_FINE_LOCATION};
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button addEventButton;
    private ProgressDialog progressing;
    private Spinner category;
    private AutoCompleteTextView mAutocompleteTextView;
    private GoogleApiClient mGoogleApiClient;
    private AddressListAdapter mAddressListAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();

        mStoreEventReference = FirebaseStorage.getInstance().getReference().child("Events");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Post");
        mStoreEvent_dataReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mStoreEvent_dataReference.keepSynced(true);
        category = (Spinner) findViewById(R.id.spinner_category_id);
        category.setOnItemSelectedListener(AddEventActivity.this);
        List<String> categories = Arrays.asList(getResources().getStringArray(R.array.theme_item));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(dataAdapter);

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
                                _Month = Integer.toString(monthOfYear);





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

        View _view = AddEventActivity.this.getCurrentFocus();
        if (_view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }



        final String title = eventTitle.getText().toString().trim();
        final String description = eventDescription.getText().toString().trim();
        final String price = eventPrice.getText().toString().trim();
        final String date = txtDate.getText().toString().trim();
        final String time = txtTime.getText().toString().trim();
        final String address = mAutocompleteTextView.getText().toString().trim();
        final String categoryEvent = category.getSelectedItem().toString();
        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(price)
                && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(time) && !TextUtils.isEmpty(address)) {

            final StorageReference filePath = mStoreEventReference.child("imageevent_" + title);
          try{

              filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {               @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  Uri downloadUri = taskSnapshot.getDownloadUrl();
                  if (!downloadUri.equals(null)) {
                      progressing.setMessage(" image is being downloading .. ");
                      progressing.show();
                      DatabaseReference keyRef = mStoreEvent_dataReference.child(my_id).push();
                      final DatabaseReference newEvent = keyRef;
                      final String pushRef = keyRef.getKey();
                      newEvent.child("title").setValue(title);
                      newEvent.child("description").setValue(description);
                      newEvent.child("price").setValue(price);
                      newEvent.child("date").setValue(date);
                      newEvent.child("time").setValue(time);
                      newEvent.child("address").setValue(address);
                      newEvent.child("month").setValue(_Month);
                      newEvent.child("pushId").setValue(pushRef);
                      newEvent.child("eventId").setValue(my_id);
                      newEvent.child("imageEvent").setValue(downloadUri.toString());
                      newEvent.child("category").setValue(categoryEvent);
                      progressing.dismiss();


                      // if added event's category exist in the preferedEvents node , a notification
                      //related to that matter will be added , so an alert be send to users interested
                      // in such kind categories
                      DatabaseReference preferedEventRef = FirebaseDatabase.getInstance().getReference().child("myPreferedEvents");
                      preferedEventRef.addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                  String id = snapshot.getKey();
                                  for (int i =0 ; i<3;i++){
                                      String category = snapshot.child(String.valueOf(i)).getValue().toString();
                                      // i need to add condition to not count my added events later ! i am keeping it now for test matter
                                  if (category.equals(categoryEvent) ){
                                      Toast.makeText(AddEventActivity.this, "done", Toast.LENGTH_SHORT).show();
                                  DatabaseReference notificationNewEventAddedRef =  FirebaseDatabase.getInstance().getReference().child("NotificationNewEventAdded").child(id).child(pushRef);
                                  notificationNewEventAddedRef.child("notif").setValue("sent");
                                  notificationNewEventAddedRef.child("addedBy").setValue(my_id);

                                  }
                                  else {

                                  }

                                  }

                              }
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });





                      final AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
                      SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
                      final String dateString = formatter.format(new Date());

                      builder.setTitle("You HAVE A NEW EVENT !")
                              .setMessage("do you want to add a post about how great your new event is going to be?")
                              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      String new_Event = "I am sharing a new Event called " + title + ", Come and check ! ";

                                      DatabaseReference keyRef = postsRef.child(my_id).child(pushRef);
                                      final DatabaseReference newEvent = keyRef;
                                      newEvent.child("post").setValue(new_Event);
                                      newEvent.child("time").setValue(dateString);
                                      Intent goBackToEventList = new Intent(AddEventActivity.this, FragmentsUnionActivity.class);
                                      startActivity(goBackToEventList);

                                  }
                              })
                              .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      Intent goBackToEventList = new Intent(AddEventActivity.this, FragmentsUnionActivity.class);
                                      goBackToEventList.putExtra("keyRef", pushRef);
                                      startActivity(goBackToEventList);
                                  }
                              });
                      builder.show();


                  }
                  else {
                      Toast.makeText(AddEventActivity.this, "Error Encountered", Toast.LENGTH_SHORT).show();
                  }
              }
              });
          }
          catch (Exception e){
              Toast.makeText(AddEventActivity.this, "pick a photo for the event please", Toast.LENGTH_SHORT).show();

          }



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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}




