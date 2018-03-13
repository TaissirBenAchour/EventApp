package com.example.cassio.Graduation_Project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.UserAdapter;
import com.example.cassio.Graduation_Project.models.AllUsersClass;
import com.example.cassio.Graduation_Project.models.feeds;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SingleEventPostActivity extends AppCompatActivity {

    private static final String TAG = "SingleEventPostActivity";
    DatabaseReference mFirebaseDatabase_event, postRef, savedEventsDBRefrence, feedsRef, userRef, rateRef;
    String my_id;
    private TextView title;
    private TextView date;
    private TextView time;
    private TextView address;
    private TextView description, ratingDisplayTextView;
    private TextView fees;
    private ImageView intressted_btn, notInterested;
    private TextView interestedText;
    private ImageView image_Event;
    private Boolean saveProcess = true;
    private FirebaseAuth mAuth;
    private List<String> listofmonths = new ArrayList<String>();
    private RecyclerView listOfComments, listofinterested;
    private EditText addcommenttext;
    private Button submitcomment;
    private String key_id;
    private int x = 1;
    private String y;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageButton morebtn;
    private Bundle Abundle = new Bundle();

    public static String date() {
        Calendar date = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        final String saveCurrentDate = currentDate.format(date.getTime());
        return saveCurrentDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_post_view);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
morebtn=(ImageButton)findViewById(R.id.more_event_id);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();

        final Bundle bundle = getIntent().getExtras();

        key_id = bundle.getString("push_id");
        final String event_id = bundle.getString("event_id");
        final String month = bundle.getString("month");
        final String titleevent = bundle.getString("title");

        title = (TextView) findViewById(R.id.SingleView_Eventtitle);
        ratingDisplayTextView = (TextView) findViewById(R.id.txtrating1);

        fees = (TextView) findViewById(R.id.SingleView_EventPrice);
        time = (TextView) findViewById(R.id.SingleView_EventTime);
        date = (TextView) findViewById(R.id.SingleView_EventDate);
        address = (TextView) findViewById(R.id.SingleView_EventAddress);
        description = (TextView) findViewById(R.id.SingleView_EventDescpription);
        intressted_btn = (ImageView) findViewById(R.id.btn_intresstedInEvent_id);
        image_Event = (ImageView) findViewById(R.id.SingleView_EventImage);
        ratingBar = (RatingBar) findViewById(R.id.rating_rating_bar);
        btnSubmit = (Button) findViewById(R.id.submit);
        interestedText = (TextView) findViewById(R.id.intrested_text);
        listofinterested = (RecyclerView) findViewById(R.id.listofinterested_id);
        listofmonths.add("January");
        listofmonths.add("February");
        listofmonths.add("March");
        mFirebaseDatabase_event = FirebaseDatabase.getInstance().getReference().child("Events");
        postRef = FirebaseDatabase.getInstance().getReference().child("Post");
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");

        mFirebaseDatabase_event.keepSynced(true);
        savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");
        savedEventsDBRefrence.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        feedsRef = FirebaseDatabase.getInstance().getReference().child("Feeds").child(my_id).child(key_id);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        listOfComments = (RecyclerView) findViewById(R.id.listofcomments_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfComments.setLayoutManager(linearLayoutManager);
        listOfComments.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        listofinterested.setLayoutManager(linearLayoutManager1);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        listofinterested.setHasFixedSize(true);
        addcommenttext = (EditText) findViewById(R.id.addcomment_id);
        submitcomment = (Button) findViewById(R.id.submitcommentbtn);
        submitcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
                addcommenttext.setText("");

            }
        });

        morebtn.setVisibility(View.INVISIBLE);
        mFirebaseDatabase_event.child(my_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(key_id).exists()){
                    morebtn.setVisibility(View.VISIBLE);
                    morebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(SingleEventPostActivity.this);
                            CharSequence option [] = new CharSequence[]{"Delet post"};
                            dialog.setItems(option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(my_id).child(key_id).exists()){
                                                Toast.makeText(SingleEventPostActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                                postRef.child(my_id).child(key_id).removeValue();
                                            }
                                            mFirebaseDatabase_event.child(my_id).child(key_id).removeValue();
                                            Toast.makeText(SingleEventPostActivity.this, "ooook", Toast.LENGTH_SHORT).show();

                                            Toast.makeText(SingleEventPostActivity.this, "event deleted successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SingleEventPostActivity.this,FragmentsUnionActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                            });
                            dialog.show();
                        }

                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<AllUsersClass> users = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String parent_id = snapshot.getKey();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                            if (key_id.equals(snapshot2.getKey())) {

                                userRef.child(parent_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String userImage = dataSnapshot.child("userImage").getValue().toString();

                                        users.add(new AllUsersClass(userImage));
                                        UserAdapter adapter = new UserAdapter(users, SingleEventPostActivity.this);
                                        listofinterested.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }


                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseDatabase_event.child(event_id).child(key_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String _title = dataSnapshot.child("title").getValue().toString();
                    String _time = dataSnapshot.child("time").getValue().toString();
                    String _address = dataSnapshot.child("address").getValue().toString();
                    String _description = dataSnapshot.child("description").getValue().toString();
                    String _fees = dataSnapshot.child("price").getValue().toString();
                    String _image = dataSnapshot.child("imageEvent").getValue().toString();

                    String _date = dataSnapshot.child("date").getValue().toString();
                    title.setText(_title);
                    description.setText(_description);
                    date.setText(_date);
                    time.setText(_time);
                    address.setText(_address);
                    fees.setText(_fees);
                    Picasso.with(SingleEventPostActivity.this).load(_image).placeholder(R.drawable.profile_pic).into(image_Event);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        intressted_btn.setImageResource(R.drawable.ic_bookmark_black_24dp);
        interestedText.setVisibility(View.INVISIBLE);
        intressted_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProcess = true;

                savedEventsDBRefrence.child(my_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String saveCurrentDate = SingleEventPostActivity.date();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if (saveProcess) {
                                    if (snapshot1.getKey().equals(key_id)) {
                                        String i = month;
                                        int j = Integer.parseInt(i);
                                        String searchedMonth = listofmonths.get(j);
                                        savedEventsDBRefrence.child(my_id).child(searchedMonth).child(key_id).removeValue();
                                        intressted_btn.setImageResource(R.drawable.ic_bookmark_black_24dp);
                                        interestedText.setVisibility(View.INVISIBLE);


                                        saveProcess = false;
                                    } else {
                                        interestedText.setVisibility(View.INVISIBLE);

                                        if (month.equals("1"))

                                        {
                                            savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("savedIn").setValue(saveCurrentDate);
                                            savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("month").setValue(month);
                                            savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("title").setValue(titleevent);
                                            intressted_btn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);


                                        }
                                        Abundle.putString(FirebaseAnalytics.Param.START_DATE, month);
                                        Abundle.putString(FirebaseAnalytics.Param.ITEM_NAME, titleevent);
                                        mFirebaseAnalytics.logEvent("saved_events", Abundle);
                                        saveProcess = false;
                                    }
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


//                savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//
//                            final String ParentKey = snapshot.getValue().toString();
//                            if (!ParentKey.contains(key_id)) {
//                                String saveCurrentDate = date();
//
//
//                                if (month.equals("0"))
//
//                                {
//                                    savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                    savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("month").setValue(month);
//                                    savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("title").setValue(titleevent);
//
//
//                                } else if (month.equals("1")) {
//                                    savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                    savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("month").setValue(month);
//                                    savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("title").setValue(titleevent);
//
//                                } else if (month.equals("2")) {
//                                    savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                    savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("month").setValue(month);
//                                    savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("title").setValue(titleevent);
//
//
//                                } else if (month.equals("3")) {
//                                    savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                    savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("month").setValue(month);
//                                    savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("title").setValue(titleevent);
//
//
//                                }
//
//                                Abundle.putString(FirebaseAnalytics.Param.START_DATE, month);
//                                Abundle.putString(FirebaseAnalytics.Param.ITEM_NAME, titleevent);
//                                mFirebaseAnalytics.logEvent("saved_events", Abundle);
//                                intressted_btn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//
//
//                            } else {
//
//
//                                String i = month;
//                                int j = Integer.parseInt(i);
//                                String searchedMonth = listofmonths.get(j);
//                                savedEventsDBRefrence.child(my_id).child(searchedMonth).child(key_id).removeValue();
//
//
//                                intressted_btn.setImageResource(R.drawable.ic_bookmark_black_24dp);
//
//
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


//
//                if (saveProcess.equals("not pressed")) {
//                    savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            String saveCurrentDate = date();
//
//
//                            if (month.equals("0"))
//
//                            {
//                                savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("month").setValue(month);
//                                savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("title").setValue(titleevent);
//
//
//                            } else if (month.equals("1")) {
//                                savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("month").setValue(month);
//                                savedEventsDBRefrence.child(my_id).child("February").child(key_id).child("title").setValue(titleevent);
//
//                            } else if (month.equals("2")) {
//                                savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("month").setValue(month);
//                                savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("title").setValue(titleevent);
//
//
//                            } else if (month.equals("3")) {
//                                savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("savedIn").setValue(saveCurrentDate);
//                                savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("month").setValue(month);
//                                savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("title").setValue(titleevent);
//
//
//                            }
//
//                            Abundle.putString(FirebaseAnalytics.Param.START_DATE, month);
//                            Abundle.putString(FirebaseAnalytics.Param.ITEM_NAME, titleevent);
//                            mFirebaseAnalytics.logEvent("saved_events", Abundle);
//                            intressted_btn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    saveProcess = "pressed";
//                }
//                else if (saveProcess.equals("pressed")) {
//                    savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//                            intressted_btn.setImageResource(R.drawable.ic_bookmark_black_24dp);
//
//                            String i = month;
//                            int j = Integer.parseInt(i);
//                            String searchedMonth = listofmonths.get(j);
//                            savedEventsDBRefrence.child(my_id).child(searchedMonth).child(key_id).removeValue();
//
//
//                        }
//
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    saveProcess = "not pressed";
//                }
            }
        });


        savedEventsDBRefrence.child(my_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            if (snapshot1.getKey().equals(key_id)) {
                                interestedText.setVisibility(View.VISIBLE);
                                intressted_btn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);

                            }
                            else{

                            }

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

        FirebaseRecyclerAdapter<feeds, feedsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<feeds, feedsViewHolder>
                (
                        feeds.class,
                        R.layout.feeds_layout,
                        feedsViewHolder.class,
                        feedsRef
                ) {
            @Override
            protected void populateViewHolder(final feedsViewHolder viewHolder, final feeds model, int position) {

                feedsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        viewHolder.setComment(model.getComment());
                        viewHolder.setCommentTime(model.getTimeOfComment());


                        userRef.child(my_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String username = dataSnapshot.child("userName").getValue().toString();
                                viewHolder.setuserName(username);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };


    }

    public void rateMe(View view) {

        DatabaseReference newComment = rateRef.child(key_id).child(my_id);
        newComment.child("rate").setValue(String.valueOf(ratingBar.getRating()));


    }

    public void addComment() {
        View _view = SingleEventPostActivity.this.getCurrentFocus();
        if (_view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }
        Calendar date = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        final String saveCurrentDate = currentDate.format(date.getTime());
        final String comment = addcommenttext.getText().toString().trim();
        if (!TextUtils.isEmpty(comment)) {

            y = Integer.toString(x++);
            DatabaseReference newComment = feedsRef.child(y);
            newComment.child("comment").setValue(comment);
            newComment.child("timeOfComment").setValue(saveCurrentDate);
            newComment.child("i").setValue(y);

        } else {
            Toast.makeText(this, "No comment is been writen", Toast.LENGTH_SHORT).show();
        }

    }

    public static class feedsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView username;

        public feedsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setComment(String comment) {
            TextView _comment = (TextView) mView.findViewById(R.id.commentText);
            _comment.setText(comment);
        }

        public void setCommentTime(String timeOfComment) {
            TextView _commentTime = (TextView) mView.findViewById(R.id.time_comment);
            _commentTime.setText(timeOfComment);
        }

        public void setuserName(String name) {
            username = (TextView) mView.findViewById(R.id.username_comment);
            username.setText(name);

        }
    }
}
