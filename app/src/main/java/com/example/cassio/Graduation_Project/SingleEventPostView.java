package com.example.cassio.Graduation_Project;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.models.feeds;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SingleEventPostView extends AppCompatActivity {

    private static final String TAG = "SingleEventPostView";
    DatabaseReference mFirebaseDatabase_event,savedEventsDBRefrence,feedsRef,userRef,rateRef;
    String my_id;
    private TextView title;
    private TextView date;
    private TextView time;
    private TextView address;
    private TextView description,ratingDisplayTextView;
    private TextView fees;
    private Button intressted_btn;
    private Button join_btn;
    private ImageView image_Event;
    private boolean saveProcess=false;
    private FirebaseAuth mAuth;
    private List<String> listofmonths = new ArrayList<String>();
    private RecyclerView listOfComments;
    private EditText addcommenttext;
    private Button submitcomment;
    private String key_id;
    private int x = 1;
    private String y;
    private RatingBar ratingBar;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_post_view);
        mAuth = FirebaseAuth.getInstance();
        my_id=mAuth.getCurrentUser().getUid();

        final Bundle bundle=getIntent().getExtras();

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
        intressted_btn = (Button) findViewById(R.id.btn_intresstedInEvent_id);
        join_btn = (Button) findViewById(R.id.btn_joinEvent_id);
        image_Event = (ImageView) findViewById(R.id.SingleView_EventImage);
        ratingBar = (RatingBar) findViewById(R.id.rating_rating_bar);
        btnSubmit = (Button)findViewById(R.id.submit) ;
        listofmonths.add("January");


        mFirebaseDatabase_event = FirebaseDatabase.getInstance().getReference().child("Events");
        rateRef = FirebaseDatabase.getInstance().getReference().child("Rate");

        mFirebaseDatabase_event.keepSynced(true);
        savedEventsDBRefrence = FirebaseDatabase.getInstance().getReference().child("SavedEvents");
        savedEventsDBRefrence.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        my_id = mAuth.getCurrentUser().getUid();
        feedsRef = FirebaseDatabase.getInstance().getReference().child("Feeds").child(my_id).child(key_id);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        listOfComments=(RecyclerView)findViewById(R.id.listofcomments_id);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listOfComments.setLayoutManager(linearLayoutManager);
        listOfComments.setHasFixedSize(true);
        addcommenttext = (EditText) findViewById(R.id.addcomment_id) ;
        submitcomment = (Button) findViewById(R.id.submitcommentbtn);
        submitcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
                addcommenttext.setText("");

            }
        });





        mFirebaseDatabase_event.child(event_id).child(key_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
      String _title = dataSnapshot.child("title").getValue().toString();

                String _time = dataSnapshot.child("time").getValue().toString();
                String _address = dataSnapshot.child("address").getValue().toString();
                String _description = dataSnapshot.child("description").getValue().toString();
                String _fees = dataSnapshot.child("price").getValue().toString();

                String _date = dataSnapshot.child("date").getValue().toString();
                title.setText(_title);
                description.setText(_description);
                date.setText(_date);
                time.setText(_time);
                address.setText(_address);
                fees.setText(_fees);
           //    Picasso.with(SingleEventPostView.this).load(image).placeholder(R.drawable.profile_pic).into(image_Event);


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

                        for (final DataSnapshot snapshot : dataSnapshot.getChildren()){


                            final String ParentKey = snapshot.getValue().toString();
                            if (saveProcess) {


                                Toast.makeText(SingleEventPostView.this, "your id exist", Toast.LENGTH_SHORT).show();

                                if (ParentKey.contains(key_id)) {
                                    String i = month;
                                    int j = Integer.parseInt(i);
                                    String searchedMonth = listofmonths.get(j);

                                    DatabaseReference parent = dataSnapshot.child(my_id).child(searchedMonth).child(key_id).getRef();
                                    parent.setValue(null);

                                    saveProcess = false;

                                } else {
                                    Calendar date = Calendar.getInstance();
                                    final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
                                    final String saveCurrentDate = currentDate.format(date.getTime());
                                    if (month.equals("0"))

                                    {
                                        savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("savedIn").setValue(saveCurrentDate);
                                        savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("month").setValue(month);
                                        savedEventsDBRefrence.child(my_id).child("January").child(key_id).child("title").setValue(titleevent);


                                    }
//                                   else if(month.equals("1"))
//                                   { savedEventsDBRefrence.child(my_id).child("February")
//                                           .child(key_id).child("savedIn").setValue(saveCurrentDate);}
//
                                else if(month.equals("2")){
                                    savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("savedIn").setValue(saveCurrentDate);
                                    savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("month").setValue(month);
                                        savedEventsDBRefrence.child(my_id).child("March").child(key_id).child("title").setValue(titleevent);


                                    }
//                                else if(month.equals("3")){
//                                    savedEventsDBRefrence.child(my_id).child("April").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//
//                                }
//                                else if(month.equals("4")){
//                                    savedEventsDBRefrence.child(my_id).child("May").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("5")){
//                                   savedEventsDBRefrence.child(my_id).child("June").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("6")){
//                                    savedEventsDBRefrence.child(my_id).child("July").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("7")){
//                                    savedEventsDBRefrence.child(my_id).child("August").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("8")){
//                                    savedEventsDBRefrence.child(my_id).child("September").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("9")){
//                                    savedEventsDBRefrence.child(my_id).child("October").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("10")){
//                                    savedEventsDBRefrence.child(my_id).child("November").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }
//                                else if(month.equals("11")){
//                                    savedEventsDBRefrence.child(my_id).child("December").child(key_id).child("savedIn").setValue(saveCurrentDate);
//
//                                }

                                }


                                saveProcess = false;

                            }
                        }


                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        savedEventsDBRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(key_id).hasChild(event_id))
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


    public void rateMe(View view){

        Toast.makeText(getApplicationContext(),
                String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
        DatabaseReference newComment = rateRef.child(key_id).child(my_id);
        newComment.child("rate").setValue(String.valueOf(ratingBar.getRating()));





    }
    public void addComment(){
        View _view = SingleEventPostView.this.getCurrentFocus();
        if (_view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }
        Calendar date = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        final String saveCurrentDate = currentDate.format(date.getTime());
        final String comment  =addcommenttext.getText().toString().trim();
        if (!TextUtils.isEmpty(comment)){

            y= Integer.toString(x++);
            DatabaseReference newComment = feedsRef.child(y);
            newComment.child("comment").setValue(comment);
            newComment.child("timeOfComment").setValue(saveCurrentDate);
            newComment.child("i").setValue(y);

        }
        else {
            Toast.makeText(this, "No comment is been writen", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<feeds,feedsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<feeds, feedsViewHolder>
                (
                        feeds.class,
                        R.layout.feeds_layout,
                        feedsViewHolder.class,
                        feedsRef
                )
        {
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
        };listOfComments.setAdapter(firebaseRecyclerAdapter);

    }

    public static  class feedsViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username;

        public feedsViewHolder(View itemView) {
            super(itemView);
            mView=itemView;

        }
        public void setComment(String comment){
            TextView _comment=(TextView) mView.findViewById(R.id.commentText);
            _comment.setText(comment);
        }
        public void setCommentTime(String timeOfComment){
            TextView _commentTime=(TextView) mView.findViewById(R.id.time_comment);
            _commentTime.setText(timeOfComment);
        }
        public void setuserName (String name){
            username = (TextView)mView.findViewById(R.id.username_comment);
            username.setText(name);

        }
    }
}
