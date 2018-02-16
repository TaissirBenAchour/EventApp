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
import com.example.cassio.Graduation_Project.SettingsActivity;
import com.example.cassio.Graduation_Project.models.HomeListPost;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
 
    private List<HomeListPost> homeListPostList;
    private Context context;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, name;
        public CircleImageView image;
        private LinearLayout linear;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.post_id);
            name = (TextView) view.findViewById(R.id.name_id);
            linear = (LinearLayout) view.findViewById(R.id.linear_homelist_id);
            image = (CircleImageView) view.findViewById(R.id.image_id);

        }


    }
 
 
    public HomeAdapter(List<HomeListPost> homeListPostList , Context ctx ) {
        this.homeListPostList = homeListPostList;
        this.context=ctx;
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
        Picasso.with(context).load(homeListPost.getuserImage())
                .placeholder(R.drawable.profile_pic).
                into(holder.image);
        if (position % 2 == 0) holder.linear.setBackgroundResource(R.color.dot_light_screen1);
        if (position % 3 == 0) holder.linear.setBackgroundResource(R.color.dot_light_screen2);
        if (position % 5 == 0) holder.linear.setBackgroundResource(R.color.dot_light_screen3);
        if (position % 7 == 0) holder.linear.setBackgroundResource(R.color.dot_light_screen4);


    }
 
    @Override
    public int getItemCount() {
        return homeListPostList.size();
    }
}