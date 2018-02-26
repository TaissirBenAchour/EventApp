package com.example.cassio.Graduation_Project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SponsoringActivity extends AppCompatActivity {

    FirebaseAuth mAuth ;
    String my_id;
    DatabaseReference sponsorRef,userRef ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsoring);
        sponsorRef = FirebaseDatabase.getInstance().getReference().child("Sponsors");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth =FirebaseAuth.getInstance();
        my_id=mAuth.getCurrentUser().getUid();



    }

    public void noforsponsoring(View view){
        userRef.child(my_id).child("plus").setValue("not sponsor");
        Intent gotoprofile = new Intent(SponsoringActivity.this,FragmentsUnionActivity.class);
        startActivity(gotoprofile);


    }

    public void yesforsponsoring(View view){

        final AlertDialog.Builder builder = new AlertDialog.Builder(SponsoringActivity.this);
        final CharSequence options[] = new CharSequence[]{
                "Agency",
                "Restaurant/Food Industry",
                "Tech Company",
                "Media",
                "Store/Brand",
                "Photography",

        };

        builder.setTitle("what is your business type ? ");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                 String txt = options[i].toString();
                    Toast.makeText(SponsoringActivity.this, "Congrats ! ", Toast.LENGTH_SHORT).show();
                    sponsorRef.child(my_id).child("typeBusiness").setValue(options[i]);
                    userRef.child(my_id).child("plus").setValue("sponsor");
                    Intent gotoprofile = new Intent(SponsoringActivity.this,FragmentsUnionActivity.class);
                    startActivity(gotoprofile);





            }


        } );builder.show();



    }
}
