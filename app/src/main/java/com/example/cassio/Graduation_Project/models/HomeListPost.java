package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 15/02/18.
 */

public class HomeListPost {

    String name;
    String userImage;
    String title;

    public HomeListPost(String name, String userImage, String title) {
        this.name = name;
        this.userImage = userImage;
        this.title = title;
    }

    public HomeListPost() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuserImage() {
        return userImage;
    }

    public void setImage(String userImage) {
        userImage = userImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
