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
    String idEvent;
    String month ;

    public HomeListPost(String name, String userImage, String title, String eventImage, String time, String idEvent,String month) {
        this.name = name;
        this.userImage = userImage;
        this.title = title;
        this.eventImage=eventImage;
        this.time=time;
        this.idEvent=idEvent;
        this.month=month;
    }

    public HomeListPost(String title, String time, String userImage) {
        this.title = title;
        this.time = time;
        this.userImage = userImage;
    }

    public HomeListPost(String title, String time) {
        this.title=title;
        this.time=time;
    }

    public HomeListPost(String title) {
        this.title = title;
    }

    public HomeListPost() {
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
