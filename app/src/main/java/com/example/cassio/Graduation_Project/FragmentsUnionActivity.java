package com.example.cassio.Graduation_Project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.AppealFragments.AppealPager;
import com.example.cassio.Graduation_Project.StartApp.loginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class FragmentsUnionActivity extends AppCompatActivity {

    private static final String TAG = "***********";
    String get_Unique_Id;
    private BoomMenuButton bmb;
    private FirebaseAuth mAuth;
    private DatabaseReference storedDataReference, savedEventRef, eventRef;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private TabPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_home);


        mAuth = FirebaseAuth.getInstance();

         get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        storedDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
        savedEventRef = FirebaseDatabase.getInstance().getReference().child("SavedEvents").child(get_Unique_Id);
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        storedDataReference.keepSynced(true);
        myMainViewPager = (ViewPager) findViewById(R.id.viewpager_fragment);


        mainPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        myMainViewPager.setAdapter(mainPagerAdapter);
        mytabLayout = (TabLayout) findViewById(R.id.main_tab_id);
        mytabLayout.setupWithViewPager(myMainViewPager);


        bmb = (BoomMenuButton) findViewById(R.id.bmb4);


        HamButton.Builder newBuilder1 = new HamButton.Builder()
                .normalTextRes(R.string.Setting)
                .normalImageRes(R.drawable.ic_settings_black_24dp)
                .normalColor(getResources().getColor(R.color.btn1))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent1 = new Intent(FragmentsUnionActivity.this, SettingsActivity.class);
                        startActivity(intent1);
                    }
                });
        bmb.addBuilder(newBuilder1);
        HamButton.Builder newBuilder4 = new HamButton.Builder()
                .normalTextRes(R.string.appeal)
                .normalColor(getResources().getColor(R.color.btn2))
                .normalImageRes(R.drawable.ic_record_voice_over_black_24dp)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent2 = new Intent(FragmentsUnionActivity.this, AppealPager.class);
                        startActivity(intent2);
                    }
                });
        bmb.addBuilder(newBuilder4);
        HamButton.Builder newBuilder2 = new HamButton.Builder()
                .normalTextRes(R.string.AnsweraSurvey)
                .normalColor(getResources().getColor(R.color.btn3))
                .normalImageRes(R.drawable.ic_content_paste_black_24dp)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent2 = new Intent(FragmentsUnionActivity.this, SurveyActivity.class);
                        startActivity(intent2);
                    }
                });
        bmb.addBuilder(newBuilder2);
        HamButton.Builder newBuilder3 = new HamButton.Builder()
                .normalTextRes(R.string.logout)
                .normalColor(getResources().getColor(R.color.btn4))
                .normalImageRes(R.drawable.ic_subdirectory_arrow_left_black_24dp)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        mAuth.signOut();
                        logout_account();
                    }
                });
        bmb.addBuilder(newBuilder3);

        HamButton.Builder newBuilder5 = new HamButton.Builder()
                .normalTextRes(R.string.ignore)
                .normalColor(getResources().getColor(R.color.btn5))
                .normalImageRes(R.drawable.ic_subdirectory_arrow_right_black_24dp)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                    }
                });
        bmb.addBuilder(newBuilder5);


        mytabLayout.getTabAt(0).setIcon(R.drawable.ic_home_black_24dp);
        mytabLayout.getTabAt(1).setIcon(R.drawable.ic_perm_identity_black_24dp);
        mytabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_bubble_outline_black_24dp);
        mytabLayout.getTabAt(3).setIcon(R.drawable.ic_people_outline_black_24dp);
        myMainViewPager.setOffscreenPageLimit(3);
        LinearLayout linearLayout = (LinearLayout) mytabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.LTGRAY);
        drawable.setSize(2, 3);
        linearLayout.setDividerPadding(20);
        linearLayout.setDividerDrawable(drawable);
        mytabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF113232"));
        mytabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));

        savedEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (final DataSnapshot snapshot1 : snapshot.getChildren()) {
                        eventRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                    eventRef.child(snapshot2.getKey()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot3 : dataSnapshot.getChildren()) {
                                                if (snapshot3.getKey().equals(snapshot1.getKey())) {

                                                    String date = snapshot3.child("date").getValue().toString();
                                                    int day_event = Integer.valueOf(date.split("-")[0]);
                                                    int month_event = Integer.valueOf(date.split("-")[1]);
                                                    int year_event = Integer.valueOf(date.split("-")[2]);
                                                    Calendar c = Calendar.getInstance();
                                                    int mYear = c.get(Calendar.YEAR);
                                                    int mMonth = c.get(Calendar.MONTH);
                                                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                                                    String currentDate = mDay + "-" + (mMonth + 1) + "-" + mYear;
                                                    int day_currentDate = Integer.valueOf(currentDate.split("-")[0]);
                                                    int month_currentDate = Integer.valueOf(currentDate.split("-")[1]);
                                                    int year_currentDate = Integer.valueOf(currentDate.split("-")[2]);
                                                    int notificationDay = day_event-3;
                                                    int notificationMonth = month_event;
                                                    int notificationYear = year_event;


                                                    if (month_currentDate == notificationMonth && year_currentDate==notificationYear ) {
                                                        int counterInDays = day_currentDate-notificationDay;

                                                        if (counterInDays == 0){
                                                            Intent intent = new Intent(FragmentsUnionActivity.this, MyBroadcastReceiver.class);
                                                            intent.putExtra("eventId",snapshot1.getKey());
                                                            intent.putExtra("userEventId",snapshot2.getKey());
                                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                                    FragmentsUnionActivity.this, 234324243, intent, 0);
                                                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                                            Toast.makeText(FragmentsUnionActivity.this, "ok !", Toast.LENGTH_SHORT).show();
                                                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                                                                    + (counterInDays*86400*1000), pendingIntent);
                                                        }


                                                    }

                                                } else
                                                    Log.d(TAG, "++++++++++++++++++");

                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        PrefManager prefManager = new PrefManager(this);
        if (prefManager.isFirstTimeLaunch()) {

        final String[] listItems;
        final boolean[] checkedItems;
        final ArrayList<Integer> mUserItems = new ArrayList<>();
        prefManager.setFirstTimeLaunch(false);
        listItems = getResources().getStringArray(R.array.theme_item);
        checkedItems = new boolean[listItems.length];


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FragmentsUnionActivity.this);
        mBuilder.setTitle("3 types of events you are interested in ..");
        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    mUserItems.add(position);
                }else{
                    mUserItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";

                for (int i = 0; i < mUserItems.size(); i++) {
                    if (mUserItems.size()==3) {

                        item = listItems[mUserItems.get(i)];
                        DatabaseReference myPreferedEventsRef = FirebaseDatabase.getInstance().getReference().child("myPreferedEvents");
                        myPreferedEventsRef.child(get_Unique_Id).child(String.valueOf(i)).setValue(item);

                    }


                }




            }
        });

        mBuilder.setNegativeButton("not ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton("clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                }
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

            }}



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



