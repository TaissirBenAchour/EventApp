package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomMenuButton;

public class FragmentsUnionActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String key ;
    BoomMenuButton boomMenuButton;
    private FirebaseAuth mAuth;
    private DatabaseReference storedDataReference;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private Toolbar toolbar;
    private TabPagerAdapter mainPagerAdapter;
    private boolean init = false;



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

        mytabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        mytabLayout.getTabAt(1).setIcon(R.drawable.ic_perm_identity_black_24dp);
        mytabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_bubble_outline_black_24dp);
        mytabLayout.getTabAt(3).setIcon(R.drawable.ic_people_outline_black_24dp);
        myMainViewPager.setOffscreenPageLimit(3);
        LinearLayout linearLayout = (LinearLayout)mytabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.LTGRAY);
        drawable.setSize(2, 3);
        linearLayout.setDividerPadding(20);
        linearLayout.setDividerDrawable(drawable);
        mytabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF813232"));
        mytabLayout.setSelectedTabIndicatorHeight((int) (1 * getResources().getDisplayMetrics().density));






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
        if (item.getItemId() == R.id.action_survey) {
            Intent goToSurveyPageIntent = new Intent(FragmentsUnionActivity.this, SurveyActivity.class);
            startActivity(goToSurveyPageIntent);
        }
        if (item.getItemId() == R.id.action_choice) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, AppealForEvents.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_users) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, AllAppUsersActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_event) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, AvailableEventActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_speaker) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, LogisticsActivity.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_appeal) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, AppealsList.class);
            startActivity(goToSettingsPageIntent);
        }
        if (item.getItemId() == R.id.action_calendar) {
            Intent goToSettingsPageIntent = new Intent(FragmentsUnionActivity.this, testCalendar.class);
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



