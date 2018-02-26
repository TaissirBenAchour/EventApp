package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 15/02/18.
 */

public class HomeListPost {

    String name;
    String userImage;
    String title;
    String eventImage;
    String time;

    public HomeListPost(String name, String userImage, String title,String eventImage,String time) {
        this.name = name;
        this.userImage = userImage;
        this.title = title;
        this.eventImage=eventImage;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public HomeListPost(String title) {
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
