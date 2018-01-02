package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 24/12/17.
 */

public class EventClass {
    private String title;
    private String description;
    private String imageEvent;
    private String date;
    private String time ;

    public EventClass() {
    }

    public EventClass(String title, String description, String imageEvent, String date , String time) {
        this.title = title;
        this.description = description;
        this.imageEvent = imageEvent;
        this.date = date ;
        this.time = time ;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return imageEvent;
    }

    public void setImage(String image) {
        this.imageEvent = imageEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
