package com.example.cassio.Graduation_Project.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cassio on 22/12/17.
 */

public class AllUsersClass {
    public String userName;
    public String userStatus;
    public String userImage;
    public String userId;

    public AllUsersClass(String userName, String userStatus, String userImage, String userId) {
        this.userName = userName;
        this.userStatus = userStatus;
        this.userImage = userImage;
        this.userId =userId;
    }

    public AllUsersClass() {
    }

    public AllUsersClass(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AllUsersClass(String userName, String userImage,String userId) {
        this.userImage=userImage;
        this.userName=userName;
        this.userId=userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    public String getUserImage() {
        return userImage;
    }
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
