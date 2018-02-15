package com.example.cassio.Graduation_Project;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppealForEvents extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String key ;
    private FirebaseAuth mAuth;
    private DatabaseReference storedDataReference;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private Toolbar toolbar;
    private AppealPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_appeal_event);


        mAuth = FirebaseAuth.getInstance();
        String get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        storedDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
        storedDataReference.keepSynced(true);
        myMainViewPager = (ViewPager) findViewById(R.id.viewpager_fragment);
        mainPagerAdapter = new AppealPagerAdapter(getSupportFragmentManager());
        myMainViewPager.setAdapter(mainPagerAdapter);
        mytabLayout = (TabLayout) findViewById(R.id.main_tab_id);
        mytabLayout.setupWithViewPager(myMainViewPager);
        setSupportActionBar(toolbar);
        mytabLayout.getTabAt(0).setText("Appeal for Event");
        mytabLayout.getTabAt(1).setText("Appeal for Committee");

        myMainViewPager.setOffscreenPageLimit(2);
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
}

