package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 13/02/18.
 */

public class Post {
    String post ;
    String image ;
    String time;

    public Post(String post ,String image,String time) {
        this.post = post;
        this.image = image;
        this.time=time;

    }


    public Post(String image) {
        this.image = image;
    }

    public Post() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
