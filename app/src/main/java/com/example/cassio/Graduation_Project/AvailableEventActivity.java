package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.models.EventClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class AvailableEventActivity extends AppCompatActivity {

    private RecyclerView list_events;
    private DatabaseReference EventDBReference;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_event);




        list_events =(RecyclerView) findViewById(R.id.list_events_id);
        list_events.setHasFixedSize(true);
        list_events.setLayoutManager(new LinearLayoutManager(this));


        EventDBReference = FirebaseDatabase.getInstance().getReference().child("Events");
        EventDBReference.keepSynced(true);





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
            protected void populateViewHolder(EventViewHolder viewHolder, EventClass model, int position) {
                viewHolder.setEvent_title(model.getTitle());
                viewHolder.setEvent_description(model.getDescription());
                viewHolder.setEvent_Image(getApplicationContext(),model.getImage());
            }

        };
        list_events.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setEvent_title (String event_title){
            TextView title =(TextView) mView.findViewById(R.id.title_field_id);
            title.setText(event_title);
        }
        public void setEvent_description (String event_description){
            TextView status =(TextView) mView.findViewById(R.id.desc_field_id);
            status.setText(event_description);
        }

        public void setEvent_Image (final Context context, final String imageEvent){

                    final ImageView image_event =(ImageView) mView.findViewById(R.id.event_image_holder);


                    if (!imageEvent.equals("imageplaceholder")){
// OFF LINE CASE  !!!!
                        // I SHOULD O BACK TO USERS PROFILE IN CASE I WILL CREATE ONES , TO VERIFY THE OFFLINE MODE ,
                        // DONT FORGET !
                        Picasso.with(context).load(imageEvent).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.imageplaceholder).into(image_event, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context).load(imageEvent).placeholder(R.drawable.ic_account_circle_black_24dp).into(image_event);
                            }
                        });

                    }
                }


}}
