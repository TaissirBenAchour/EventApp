package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 30/12/17.
 */

public class ListOfMessages {

private String userStatus;

    public ListOfMessages() {
    }

    public ListOfMessages(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
