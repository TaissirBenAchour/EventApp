package com.example.cassio.Graduation_Project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SurveyActivity extends AppCompatActivity {

    private Button useful_btn, not_useful_btn;
    private EditText sugg_txt;
    private Button submit_txt;
    private RatingBar rate;
    private ImageView checkbtn;
    private DatabaseReference surveyRef;
    private FirebaseAuth mAuth;
    private String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_activity);

        useful_btn = (Button) findViewById(R.id.yes_id);
        not_useful_btn = (Button) findViewById(R.id.no_id);
        sugg_txt = (EditText) findViewById(R.id.sugg_txt_id);
        submit_txt = (Button) findViewById(R.id.submit_id);
        rate = (RatingBar) findViewById(R.id.rate_id);
        checkbtn= (ImageView) findViewById(R.id.check_id);
        surveyRef = FirebaseDatabase.getInstance().getReference().child("Survey");
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();


        useful_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question1 = useful_btn.getText().toString().trim();
              DatabaseReference newSurvey = surveyRef.child(my_id).child("question1");
                newSurvey.setValue(question1);

            }
        });
        not_useful_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question1 = not_useful_btn.getText().toString().trim();
                DatabaseReference newSurvey = surveyRef.child(my_id).child("question1");
                newSurvey.setValue(question1);

            }
        });

        submit_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question2 = sugg_txt.getText().toString().trim();
                DatabaseReference newSurvey = surveyRef.child(my_id).child("question2");
                newSurvey.setValue(question2);

            }
        });


    }
    public void rateMe(View view){

        DatabaseReference newSurvey = surveyRef.child(my_id).child("question3");
        newSurvey.setValue(String.valueOf(rate.getRating()));


    }

}
