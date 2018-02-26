package com.example.cassio.Graduation_Project.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cassio.Graduation_Project.R;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<HomeListPost> homeListPostList;
    private Context context;


    public HomeAdapter(List<HomeListPost> homeListPostList) {
        this.homeListPostList = homeListPostList;
    }

    public HomeAdapter(List<HomeListPost> homeListPostList, Context ctx) {
        this.homeListPostList = homeListPostList;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homelist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HomeListPost homeListPost = homeListPostList.get(position);
        holder.title.setText(homeListPost.getTitle());
        holder.name.setText(homeListPost.getName());
        holder.time.setText(homeListPost.getTime());

        Picasso.with(context).load(homeListPost.getuserImage())
                .placeholder(R.drawable.profile_pic).
                into(holder.image);
        Picasso.with(context).load(homeListPost.getEventImage())
                .placeholder(R.drawable.profile_pic).
                into(holder.eventImage);


    }

    @Override
    public int getItemCount() {
        return homeListPostList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, name, time;
        public CircleImageView image;
        private LinearLayout linear;
        private ImageView eventImage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.post_id);
            name = (TextView) view.findViewById(R.id.name_id);
            linear = (LinearLayout) view.findViewById(R.id.linear_homelist_id);
            image = (CircleImageView) view.findViewById(R.id.image_id);
            eventImage = (ImageView) view.findViewById(R.id.eventimage_id);
            time = (TextView) view.findViewById(R.id.time_post);

        }


    }
}