package com.example.cassio.Graduation_Project;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.models.EventClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AvailableEventActivity extends AppCompatActivity{

    private RecyclerView list_events;
    private DatabaseReference EventDBReference, savedEventsDBRefrence;
    private ImageButton searchEventButton;
    private EditText searchField;
    private boolean saveProcess =false ;
    private FirebaseAuth mAuth;
    private String my_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_event);


        searchEventButton = (ImageButton) findViewById(R.id.search_event_button);
        searchField = (EditText) findViewById(R.id.search_id);
        list_events = (RecyclerView) findViewById(R.id.list_events_id);
        list_events.setHasFixedSize(true);
        list_events.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
         my_id= mAuth.getCurrentUser().getUid();
        EventDBReference = FirebaseDatabase.getInstance().getReference().child("Events");
        savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");

        EventDBReference.keepSynced(true);
        savedEventsDBRefrence.keepSynced(true);


        searchEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchField.getText().toString();

                searchEvent(search);
            }
        });


    }

private void searchEvent(String search) {
    Query query = EventDBReference.orderByChild("title").startAt(search).endAt(search + "\uf8ff");

    FirebaseRecyclerAdapter<EventClass,AvailableEventActivity.EventViewHolder> firebaseRecyclerAdapter
            = new FirebaseRecyclerAdapter<EventClass, AvailableEventActivity.EventViewHolder>
            (
                    EventClass.class,
                    R.layout.simple_event_layout,
                    AvailableEventActivity.EventViewHolder.class,
                    query
            )
    {
        @Override
        protected void populateViewHolder(EventViewHolder viewHolder, EventClass model, final int position) {

            viewHolder.setEvent_title(model.getTitle());
            viewHolder.setEvent_description(model.getDescription());
            // viewHolder.setEvent_Image(getApplicationContext(),model.getImage());



        }


    };
    list_events.setAdapter(firebaseRecyclerAdapter);

    }

    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<EventClass,AvailableEventActivity.EventViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<EventClass, AvailableEventActivity.EventViewHolder>
                (
                        EventClass.class,
                        R.layout.simple_event_layout,
                        AvailableEventActivity.EventViewHolder.class,
                        EventDBReference
                )
        {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, EventClass model, final int position) {
                final String event_id= getRef(position).getKey();

                viewHolder.setEvent_title(model.getTitle());
                viewHolder.setEvent_description(model.getDescription());
             //   viewHolder.setEvent_Image(getApplicationContext(),model.getImage());
                viewHolder.setSaveBtn(event_id);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String id_key = getRef(position).getKey();

                        Intent intent = new Intent(AvailableEventActivity.this,SingleEventPostView.class);
                        intent.putExtra("id_event",id_key);
                        startActivity(intent);
                    }
                });

                viewHolder.saveEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveProcess = true;

                            savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (saveProcess)
                                    {
                                    if (dataSnapshot.child(event_id).hasChild(my_id))
                                    {
                                        savedEventsDBRefrence.child(event_id).child(my_id).removeValue();
                                        saveProcess=false;
                                    }
                                    else
                                    {
                                        Calendar date = Calendar.getInstance();
                                        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
                                        final String saveCurrentDate = currentDate.format(date.getTime());
                                        Toast.makeText(AvailableEventActivity.this, my_id, Toast.LENGTH_SHORT).show();
                                        savedEventsDBRefrence.child(event_id).child(my_id).child("savedfrom").setValue(saveCurrentDate);


                                        saveProcess=false;
                                    }

                                }}

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }
                });

            }

        };
        list_events.setAdapter(firebaseRecyclerAdapter);
    }



    public static class EventViewHolder extends RecyclerView.ViewHolder
    {


        View mView;
        ImageButton saveEvent;
        ImageButton joinEvent;
        DatabaseReference savedEventsDBRefrence;
        FirebaseAuth mAuth;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            saveEvent = (ImageButton) mView.findViewById(R.id.saveEventbtn_id);
            joinEvent = (ImageButton) mView.findViewById(R.id.joinEventbtn_id);
            savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");
            mAuth = FirebaseAuth.getInstance();

        }
        public void setSaveBtn (final String event_id){
            savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(event_id).hasChild(mAuth.getCurrentUser().getUid()))
                    {
                        saveEvent.setImageResource(R.drawable.ic_bookmark_black_24dp);



                    }
                    else
                    {

                        saveEvent.setImageResource(R.drawable.ic_check_black_24dp);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

        public void setEvent_title (String event_title){
            TextView title =(TextView) mView.findViewById(R.id.title_field_id);
            title.setText(event_title);
        }
        public void setEvent_description (String event_description){
            TextView status =(TextView) mView.findViewById(R.id.desc_field_id);
            status.setText(event_description);
        }

//        public void setEvent_Image (final Context context, final String imageEvent){
//
//                    final ImageView image_event =(ImageView) mView.findViewById(R.id.event_image_holder);
//
//
//                    if (!imageEvent.equals("imageplaceholder")){
//// OFF LINE CASE  !!!!
//                        // I SHOULD O BACK TO USERS PROFILE IN CASE I WILL CREATE ONES , TO VERIFY THE OFFLINE MODE ,
//                        // DONT FORGET !
//                        Picasso.with(context).load(imageEvent).networkPolicy(NetworkPolicy.OFFLINE)
//                                .placeholder(R.drawable.imageplaceholder).into(image_event, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Picasso.with(context).load(imageEvent).placeholder(R.drawable.ic_account_circle_black_24dp).into(image_event);
//                            }
//                        });
//
//                    }
//                }


}}
