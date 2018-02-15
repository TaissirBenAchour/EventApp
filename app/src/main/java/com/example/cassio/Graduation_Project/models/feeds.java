package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 23/01/18.
 */

public class feeds {

    private String Comment;
    private String timeOfComment ;

    public feeds(String comment, String timeOfComment) {
        Comment = comment;
        this.timeOfComment = timeOfComment;
    }

    public feeds() {
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getTimeOfComment() {
        return timeOfComment;
    }

    public void setTimeOfComment(String timeOfComment) {
        this.timeOfComment = timeOfComment;
    }
}
