package com.example.cassio.Graduation_Project.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.messagesClass;

import java.util.List;


public class listOfMessagesAdapter extends RecyclerView.Adapter<listOfMessagesAdapter.MyViewHolder> {

    private List<messagesClass> lastmessage;


    public listOfMessagesAdapter(List<messagesClass> lastmessage) {
        this.lastmessage = lastmessage;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dispaly_users_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        messagesClass messagesClass = lastmessage.get(position);
        holder.message.setText(messagesClass.getmessage());
        holder.msgtime.setText(messagesClass.getmsgtime());
        holder.name.setText(messagesClass.getFrom());


    }

    @Override
    public int getItemCount() {
        return lastmessage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message, msgtime, name;

        public MyViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.lastmessage_id);
            msgtime = (TextView) view.findViewById(R.id.messagetime_id);
            name = (TextView) view.findViewById(R.id.all_users_name);


        }


    }
}

