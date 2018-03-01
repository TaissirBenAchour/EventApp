package com.example.cassio.Graduation_Project.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.squareup.picasso.Picasso;

import java.util.List;

public class freindspostAdapter extends RecyclerView.Adapter<freindspostAdapter.MyViewHolder> {

    private List<HomeListPost> homeListPostList;
    private Context context;


    public freindspostAdapter(List<HomeListPost> homeListPostList) {
        this.homeListPostList = homeListPostList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeListPost homeListPost = homeListPostList.get(position);
        holder.title.setText(homeListPost.getTitle());
        holder.time.setText(homeListPost.getTime());
        Picasso.with(context).load(homeListPost.getEventImage())
                .placeholder(R.drawable.profile_pic).into(holder.imagePost);


    }

    @Override
    public int getItemCount() {
        return homeListPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,time;
        public ImageView imagePost;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.post_txt);
            time = (TextView)view.findViewById(R.id.timepost_id);
            imagePost = (ImageView)view.findViewById(R.id.postimage_id);
        }


    }
}
