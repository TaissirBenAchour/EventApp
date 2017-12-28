package com.example.cassio.Graduation_Project;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {


    private String messageRecieverId;
    private String messageRecieverName;
    private Toolbar mtool;
    private EditText message;
    private Icon sendbtn;
    private TextView recieverMessageName;
    private CircleImageView recieverMessageImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recieverMessageImage =(CircleImageView)findViewById(R.id.recieverImage_id);
        recieverMessageName=(TextView)findViewById(R.id.reciver_name_id);
        message =(EditText) findViewById(R.id.messageText_id);

        messageRecieverId = getIntent().getExtras().get("targed_person_id").toString();
        messageRecieverName =getIntent().getExtras().get("user_name").toString();

        Toast.makeText(ChatActivity.this , messageRecieverId,Toast.LENGTH_LONG).show();
        Toast.makeText(ChatActivity.this , messageRecieverName,Toast.LENGTH_LONG).show();

        mtool = (Toolbar) findViewById(R.id.toolbar_chat_id);
        //sendbtn = (Icon) findViewById(R.id.sendbutton_id);
        setSupportActionBar(mtool);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //set a view to our layoutinflater
        View actionBarView = layoutInflater.inflate(R.layout.chat_room_header,null);
        //coonect action bar to chat header view
        actionBar.setCustomView(actionBarView);
        recieverMessageName.setText(messageRecieverName);



    }
}
