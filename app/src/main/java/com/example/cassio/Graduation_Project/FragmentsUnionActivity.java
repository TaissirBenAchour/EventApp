package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.cassio.Graduation_Project.StartApp.loginActivity;
import com.example.cassio.Graduation_Project.AppealFragments.AppealPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;

public class FragmentsUnionActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    String key;
    BoomMenuButton boomMenuButton;
    BoomMenuButton bmb;
    private FirebaseAuth mAuth;
    private DatabaseReference storedDataReference;
    private ViewPager myMainViewPager;
    private TabLayout mytabLayout;
    private TabPagerAdapter mainPagerAdapter;
    private boolean init = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_home);


        mAuth = FirebaseAuth.getInstance();

        String get_Unique_Id = mAuth.getInstance().getCurrentUser().getUid();
        storedDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(get_Unique_Id);
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



