package com.example.cassio.Graduation_Project;

/**
 * Created by cassio on 24/12/17.
 */

public class EventClass {
    private String title;
    private String description;
    private String imageEvent;

    public EventClass() {
    }

    public EventClass(String title, String description, String imageEvent) {
        this.title = title;
        this.description = description;
        this.imageEvent = imageEvent;
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
