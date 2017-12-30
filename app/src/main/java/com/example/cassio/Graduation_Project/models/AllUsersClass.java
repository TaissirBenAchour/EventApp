package com.example.cassio.Graduation_Project.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cassio on 22/12/17.
 */
@IgnoreExtraProperties
public class AllUsersClass {
    public String userName;
    public  String userStatus;
    public String userImage;

    public AllUsersClass(String userName, String userStatus, String userImage) {
        this.userName = userName;
        this.userStatus = userStatus;
        this.userImage = userImage;
    }

    public AllUsersClass() {
    }
    @Exclude

    public  String getUserName() {
        return userName;
    }
    @Exclude


    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Exclude

    public  String getUserStatus() {
        return userStatus;
    }
    @Exclude

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    @Exclude

    public String getUserImage() {
        return userImage;
    }
    @Exclude

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
