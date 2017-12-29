package com.example.cassio.Graduation_Project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cassio on 29/12/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> messagesList;

    public MessageAdapter(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.message_sent,parent,false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Messages messages = messagesList.get(position);
        holder.messageText.setText(messages.getmessage());

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public CircleImageView profileImage;

        public MessageViewHolder(View view) {
            super(view);
            messageText =(TextView) view.findViewById(R.id.custom_message_id);
            profileImage=(CircleImageView) view.findViewById(R.id.messageOfSender_id);
        }
    }
}
