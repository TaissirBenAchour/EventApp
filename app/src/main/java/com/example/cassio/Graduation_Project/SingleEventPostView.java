package com.example.cassio.Graduation_Project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleEventPostView extends AppCompatActivity {

    DatabaseReference mFirebaseDatabase_event,savedEventsDBRefrence;
    private TextView title;
    private TextView date;
    private TextView time;
    private TextView address;
    private TextView description;
    private TextView fees;
    private Button intressted_btn;
    private Button join_btn;
    private ImageView image_Event;
    private boolean saveProcess=false;
    private FirebaseAuth mAuth;
    private String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_post_view);
        final String key_id = getIntent().getExtras().get("id_event").toString();

        title = (TextView) findViewById(R.id.SingleView_Eventtitle);
        fees = (TextView) findViewById(R.id.SingleView_EventPrice);
        time = (TextView) findViewById(R.id.SingleView_EventTime);
        date = (TextView) findViewById(R.id.SingleView_EventDate);
        address = (TextView) findViewById(R.id.SingleView_EventAddress);
        description = (TextView) findViewById(R.id.SingleView_EventDescpription);
        intressted_btn = (Button) findViewById(R.id.btn_intresstedInEvent_id);
        join_btn = (Button) findViewById(R.id.btn_joinEvent_id);
        image_Event = (ImageView) findViewById(R.id.SingleView_EventImage);


        mFirebaseDatabase_event = FirebaseDatabase.getInstance().getReference().child("Events");
        mFirebaseDatabase_event.keepSynced(true);
        savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");
        savedEventsDBRefrence.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();

        Toast.makeText(this, key_id, Toast.LENGTH_SHORT).show();

        mFirebaseDatabase_event.child(key_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                final String image = dataSnapshot.child("imageEvent").getValue().toString();
                String _title = dataSnapshot.child("title").getValue().toString();
                String _date = dataSnapshot.child("date").getValue().toString();
                String _time = dataSnapshot.child("time").getValue().toString();
                String _address = dataSnapshot.child("address").getValue().toString();
                String _description = dataSnapshot.child("description").getValue().toString();
                String _fees = dataSnapshot.child("price").getValue().toString();

                title.setText(_title);
                description.setText(_description);
                date.setText(_date);
                time.setText(_time);
                address.setText(_address);
                fees.setText(_fees);
               Picasso.with(SingleEventPostView.this).load(image).placeholder(R.drawable.profile_pic).into(image_Event);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        intressted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProcess = true;

                savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (saveProcess)
                        {
                            if (dataSnapshot.child(key_id).hasChild(my_id))
                            {
                                savedEventsDBRefrence.child(key_id).child(my_id).removeValue();
                                saveProcess=false;
                            }
                            else
                            {
                                Toast.makeText(SingleEventPostView.this, my_id, Toast.LENGTH_SHORT).show();
                                savedEventsDBRefrence.child(key_id).child(my_id).setValue("ok");


                                saveProcess=false;
                            }

                        }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(key_id).hasChild(my_id))
                {
                    intressted_btn.setText("done");
                }
                else
                {
                    intressted_btn.setText("intrested");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
