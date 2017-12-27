package com.example.cassio.Graduation_Project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project._profileFragments.ChatContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.CommunityContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.ProfileContentFragment;
import com.example.cassio.Graduation_Project._profileFragments.SavedEventsContentFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
//

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private final static int galery_pick = 1;
    private CircleImageView imageProfileSittings;
    private CircleImageView imageProfile;
    private DatabaseReference storedDataReference;
    private StorageReference storedProfileReference;
    private ProgressDialog progressDialog;
    private Uri SelectedImageUri;

    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ////

        mAuth = FirebaseAuth.getInstance();
        String get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        storedDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
        storedDataReference.keepSynced(true); //for offline mode
        storedProfileReference = FirebaseStorage.getInstance().getReference().child("Profile_images");

        imageProfileSittings = (CircleImageView) findViewById(R.id.ic_camera);
        imageProfile = (CircleImageView) findViewById(R.id.profilePic);


        storedDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final String image = dataSnapshot.child("userImage").getValue().toString();

                if (!image.equals("profile_pic")){
                    Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.profile_pic).into(imageProfile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.profile_pic).into(imageProfile);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageProfileSittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionREAD_EXTERNAL_STORAGE(ProfileActivity.this)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, galery_pick);
                }
            }
        });



        //
//        pseudoname=(TextView) findViewById(R.id.pseudoname);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Tab 1"));
        tabs.addTab(tabs.newTab().setText("Tab 2"));
        tabs.addTab(tabs.newTab().setText("Tab 3"));
        tabs.addTab(tabs.newTab().setText("Tab 4"));
        tabs.setBackgroundColor(Color.parseColor("#73CF3E56")); //active tab
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_message_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);
        tabs.getTabAt(2).setIcon(R.drawable.ic_person_black_24dp);
        tabs.getTabAt(3).setIcon(R.drawable.ic_bookmark_border_black_24dp);



        //authentification of users
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main,menu);
    return  true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            logout_account();
        }

        if(item.getItemId() == R.id.action_settings){
            Intent goToSettingsPageIntent = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if(item.getItemId() == R.id.action_choice){
            Intent goToSettingsPageIntent = new Intent(ProfileActivity.this, MakeYourChoiceActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if(item.getItemId() == R.id.action_users){
            Intent goToSettingsPageIntent = new Intent(ProfileActivity.this, AllAppUsersActivity.class);
            startActivity(goToSettingsPageIntent);
        }

        return true;
    }

    private void logout_account(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            Intent goBacktoLoginPageIntent = new Intent(ProfileActivity.this, loginActivity.class);
            goBacktoLoginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goBacktoLoginPageIntent);
            finish();
          }
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ChatContentFragment(), "");
        adapter.addFragment(new CommunityContentFragment(), "");
        adapter.addFragment(new ProfileContentFragment(), "");
        adapter.addFragment(new SavedEventsContentFragment(), "");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    ///

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(ProfileActivity.this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case galery_pick:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(ProfileActivity.this, "Image selected", Toast.LENGTH_SHORT).show();
                    SelectedImageUri = imageReturnedIntent.getData();
                }

                String user_id = mAuth.getCurrentUser().getUid();
                //create reference to images folder and assing a name to the file that will be uploaded
                StorageReference filePath = storedProfileReference.child(user_id + ".jpg");


                //creating and showing progress dialog
                progressDialog = new ProgressDialog(this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.setCancelable(false);


                //starting upload
                final UploadTask uploadTask = filePath.putFile(SelectedImageUri);

                // Observe state change events such as progress, pause, and resume
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //sets and increments value of progressbar
                        progressDialog.incrementProgressBy(15);
                    }
                });

                filePath.putFile(SelectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.incrementProgressBy(15);
                            Toast.makeText(ProfileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            String downloadPic = task.getResult().getDownloadUrl().toString();
                            storedDataReference.child("userImage").setValue(downloadPic)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ProfileActivity.this, "successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(ProfileActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });


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
}
