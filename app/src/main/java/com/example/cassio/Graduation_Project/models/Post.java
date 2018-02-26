package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 13/02/18.
 */

public class Post {
    String post ;
    String image ;

    public Post(String post ,String image) {
        this.post = post;
        this.image = image;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Post() {
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
