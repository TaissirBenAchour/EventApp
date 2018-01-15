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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class FragmentsUnionActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private FirebaseAuth mAuth;
    private DatabaseReference storedDataReference;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private Toolbar toolbar;
    private TabPagerAdapter mainPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth = FirebaseAuth.getInstance();
        String get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        storedDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
        storedDataReference.keepSynced(true);
        myMainViewPager = (ViewPager) findViewById(R.id.viewpager_fragment);
        toolbar = (Toolbar) findViewById(R.id.appbar_profile);
        mainPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        myMainViewPager.setAdapter(mainPagerAdapter);
        mytabLayout = (TabLayout) findViewById(R.id.main_tab_id);
        mytabLayout.setupWithViewPager(myMainViewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("E-App");
        mytabLayout.getTabAt(0).setIcon(R.drawable.ic_person_black_24dp);
        mytabLayout.getTabAt(1).setIcon(R.drawable.ic_message_black_24dp);
        mytabLayout.getTabAt(2).setIcon(R.drawable.ic_group_black_24dp);
        mytabLayout.getTabAt(3).setIcon(R.drawable.ic_bookmark_border_black_24dp);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser == null) {
    Intent intent = new Intent(FragmentsUnionActivity.this, loginActivity.class);
        startActivity(intent);
        finish();
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            logout_account();
        }

        if (item.getItemId() == R.id.action_settings) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, SettingsActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_choice) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, MakeYourChoiceActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_users) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, AllAppUsersActivity.class);
            startActivity(goToSettingsPageIntent);
        }

        return true;
    }

    private void logout_account() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent goBacktoLoginPageIntent = new Intent(FragmentsUnionActivity.this, loginActivity.class);
            goBacktoLoginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goBacktoLoginPageIntent);
            finish();
        }
    }



}
