package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cassio.Graduation_Project.Adapters.MessageAdapter;
import com.example.cassio.Graduation_Project.models.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity {


    private final List<Messages>  messagesList = new ArrayList<>();
    private String messageRecieverId;
    private String messageRecieverName;
    private Toolbar mtool;
    private TextView recieverName;
    private CircleImageView recieverImage;
    private DatabaseReference globalRef;
    private ImageButton sendTextbtn;
    private EditText messageText;
    private FirebaseAuth mAuth;
    private String sender_id;
    private RecyclerView sentMessages;
    private SwipeRefreshLayout swipeIt;
    private LinearLayoutManager linearLayoutManager ;
    private MessageAdapter messageAdapter;
    private int num_page=1;
    private int itemPos = 0;
    private String firstKey="";
    private  String secondKey ="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageRecieverId = getIntent().getExtras().get("targed_person_id").toString();
        messageRecieverName =getIntent().getExtras().get("user_name").toString();


        globalRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        sender_id = mAuth.getCurrentUser().getUid();



        Toast.makeText(MessagesActivity.this,messageRecieverName,Toast.LENGTH_SHORT).show();


        mtool = (Toolbar) findViewById(R.id.toolbar_chat_id);
        setSupportActionBar(mtool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.chat_room_header,null);
        getSupportActionBar().setCustomView(actionBarView);

        recieverImage =(CircleImageView)findViewById(R.id.recieverImage_id);
        recieverName=(TextView)findViewById(R.id.reciver_name_id);
        sendTextbtn = (ImageButton) findViewById(R.id.sendbutton_id) ;
        messageText =(EditText) findViewById(R.id.messageText_id);



        messageAdapter = new MessageAdapter(messagesList);
        sentMessages =(RecyclerView)findViewById(R.id.message_list_id);
        swipeIt =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout_id) ;
        linearLayoutManager = new LinearLayoutManager(this);
        sentMessages.setHasFixedSize(true);
        linearLayoutManager.setStackFromEnd(true);
        sentMessages.setLayoutManager(linearLayoutManager);
        sentMessages.setAdapter(messageAdapter);
        bringMessages();


        recieverName.setText(messageRecieverName);

        globalRef.child("Users").child(messageRecieverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               final String reciverImage = dataSnapshot.child("userImage").getValue().toString();
           Picasso.with(MessagesActivity.this).load(reciverImage).placeholder(R.drawable.profile_pic).into(recieverImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendTextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        swipeIt.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                num_page++;

                 itemPos = 0;

                whenSwipe();


            }
        });


    }

    private void whenSwipe() {


        DatabaseReference messageRef= globalRef.child("Messages").child(sender_id).child(messageRecieverId);
        Query messageQuery = messageRef.orderByKey().endAt(firstKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Messages msg = dataSnapshot.getValue(Messages.class);

                String key = dataSnapshot.getKey();

                if(!secondKey.equals(key)){

                    messagesList.add(itemPos++ ,msg);

                } else {

                    secondKey = firstKey;

                }

                if (itemPos == 1){
                    firstKey = key;
                }


                 messageAdapter.notifyDataSetChanged();
                swipeIt.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String message_text= messageText.getText().toString();

        if (TextUtils.isEmpty(message_text))
        {
            Toast.makeText(MessagesActivity.this,"write a message",Toast.LENGTH_SHORT).show();
        }
        else
        {


            String senderRef = "Messages/" +sender_id + "/"+ messageRecieverId;
            String recieverRef = "Messages/" +messageRecieverId + "/" + sender_id;
            DatabaseReference keyRef = globalRef.child("Messages").child(sender_id).child(messageRecieverId).push();
            String pushRef=keyRef.getKey();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            String dateString = formatter.format(new Date());
            Map message = new HashMap();
            message.put("message",message_text);
            message.put("type","text");
            message.put("msgtime", dateString);
            message.put("from",sender_id);
            Map details = new HashMap();
            details.put(senderRef+ "/" +pushRef,message);
            details.put(recieverRef+ "/" +pushRef,message);

            globalRef.updateChildren(details, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null)
                    {
                        Log.d("message",databaseError.getMessage().toString());
                    }
                    messageText.setText("");
                }
            });

        }
    }


    private void bringMessages() {
         DatabaseReference messageRef= globalRef.child("Messages").child(sender_id).child(messageRecieverId);
        Query messageQuery = messageRef.limitToLast(10*num_page);



                messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Messages msg = dataSnapshot.getValue(Messages.class);
                itemPos ++;
                if (itemPos == 1){
                    String key = dataSnapshot.getKey();
                    firstKey = key;
                    secondKey = key;
                }
                messagesList.add(msg);
                sentMessages.scrollToPosition(messagesList.size()-1);
                messageAdapter.notifyDataSetChanged();
                swipeIt.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
