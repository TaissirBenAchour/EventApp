package com.example.cassio.Graduation_Project.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.Messages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cassio on 29/12/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> messages_List;
    private FirebaseAuth mAuth;

    public MessageAdapter(List<Messages> messages_List) {
        this.messages_List = messages_List;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.message_sent,parent,false);
        mAuth=FirebaseAuth.getInstance();

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        String sender_id = mAuth.getCurrentUser().getUid();
        Messages messages = messages_List.get(position);
        String messagefrom_id = messages.getFrom();

        if(sender_id.equals(messagefrom_id)) {
            holder.messageText.setBackgroundResource(R.drawable.shape_of_recieved_message);
            holder.messageText.setGravity(Gravity.RIGHT);
        }
        else
        {
            holder.messageText.setBackgroundResource(R.drawable.shape_of_sent_message);
            holder.messageText.setGravity(Gravity.LEFT | Gravity.END);
        }
        holder.messageText.setText(messages.getmessage());


    }

    @Override
    public int getItemCount() {
        return messages_List.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public CircleImageView profileImage;

        public MessageViewHolder(View view) {
            super(view);
            messageText =(TextView) view.findViewById(R.id.custom_message_id);
//            profileImage=(CircleImageView) view.findViewById(R.id.messageOfSender_id);
        }
    }
}
