package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MakeYourChoiceActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button findEventsBtn;
    private Button addEventsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_your_choice);
        findEventsBtn = (Button) findViewById(R.id.find_events_id);
        addEventsBtn = (Button) findViewById(R.id.add_events_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mAuth = FirebaseAuth.getInstance();


addEventsBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent goToEventList = new Intent (MakeYourChoiceActivity.this, AddEventActivity.class);
        startActivity(goToEventList);
        finish();
    }
});

        findEventsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEventList = new Intent (MakeYourChoiceActivity.this, AvailableEventActivity.class);
                startActivity(goToEventList);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_related_to_choice_activity, menu);

        return super.onCreateOptionsMenu(menu);


    }
    private void logout_account(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            Intent goBacktoLoginPageIntent = new Intent(MakeYourChoiceActivity.this, loginActivity.class);
            goBacktoLoginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(goBacktoLoginPageIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mAuth.signOut();
            logout_account();
        }
        if (id == R.id.action_about_us) {
            Intent goBacktoLoginPageIntent = new Intent(MakeYourChoiceActivity.this, SettingsActivity.class);
            startActivity(goBacktoLoginPageIntent);

        }

        if (id == R.id.action_profile) {
            Intent goBacktoLoginPageIntent = new Intent(MakeYourChoiceActivity.this, ProfileActivity.class);
            startActivity(goBacktoLoginPageIntent);
        }


        return super.onOptionsItemSelected(item);
    }
}

