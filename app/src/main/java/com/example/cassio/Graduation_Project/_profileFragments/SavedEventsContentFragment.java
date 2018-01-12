package com.example.cassio.Graduation_Project._profileFragments;

/**
 * Created by cassio on 16/12/17.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.SavedEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class  SavedEventsContentFragment extends Fragment {

    private RecyclerView list_events;
    private DatabaseReference EventDBReference, savedEventsDBRefrence;
    private ImageButton searchEventButton;
    private EditText searchField;
    private boolean saveProcess =false ;
    private FirebaseAuth mAuth;
    private String my_id;
    private View m_View;
    private boolean validation =false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        m_View =  inflater.inflate(R.layout.saved_layout, null);

        list_events = (RecyclerView) m_View.findViewById(R.id.list_events_id);
        list_events.setHasFixedSize(true);
        list_events.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        my_id= mAuth.getCurrentUser().getUid();
        EventDBReference = FirebaseDatabase.getInstance().getReference().child("Events");
        savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");

        EventDBReference.keepSynced(true);
        savedEventsDBRefrence.keepSynced(true);

        return m_View;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SavedEvent,SavedEventsContentFragment.savedEventsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<SavedEvent, SavedEventsContentFragment.savedEventsViewHolder>
                (
                        SavedEvent.class,
                        R.layout.single_saved_event,
                        SavedEventsContentFragment.savedEventsViewHolder.class,
                        savedEventsDBRefrence
                )
        {
            @Override
            protected void populateViewHolder(final SavedEventsContentFragment.savedEventsViewHolder viewHolder, final SavedEvent model, final int position) {
                final String event_id= getRef(position).getKey();


                savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if (dataSnapshot.child(event_id).child(my_id).exists())

                        {
                            validation =true;
                            String time = dataSnapshot.child(event_id).child(my_id).child("savedfrom").getValue().toString();
                            viewHolder.setSavedfrom(time);

                            EventDBReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String title = dataSnapshot.child(event_id).child("title").getValue().toString();
                                    viewHolder.setTitle(title);
                                    String time = dataSnapshot.child(event_id).child("time").getValue().toString();
                                    viewHolder.setTime(time);
                                    String date = dataSnapshot.child(event_id).child("date").getValue().toString();
                                    viewHolder.setDate(date);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                    }}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }

        };
        list_events.setAdapter(firebaseRecyclerAdapter);

    }

    public static class savedEventsViewHolder extends RecyclerView.ViewHolder {

View mView;
        public savedEventsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;



        }
        public void setTitle(String _title) {
            TextView title = (TextView) mView.findViewById(R.id.title_id);
            title.setText(_title);
        }
        public void setDate(String _date) {
            TextView date = (TextView) mView.findViewById(R.id.date_id);
            date.setText(_date);
        }
        public void setTime(String _time) {
            TextView time = (TextView) mView.findViewById(R.id.time_id);
            time.setText(_time);
        }
        public void setSavedfrom(String savedfrom){
            TextView _savedfrom = (TextView) mView.findViewById(R.id.saved_from_id);
            _savedfrom.setText("event saved since " + savedfrom);
        }

    }}