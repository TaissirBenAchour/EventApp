package com.example.cassio.Graduation_Project.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cassio on 28/01/18.
 */

public class Appeals implements Parcelable {

    public static final Creator<Appeals> CREATOR = new Creator<Appeals>() {
        @Override
        public Appeals createFromParcel(Parcel in) {
            return new Appeals(in);
        }

        @Override
        public Appeals[] newArray(int size) {
            return new Appeals[size];
        }
    };
    String vision ;
    String content;
    String address;
    String name;
    String id;

    public Appeals() {
    }

    public Appeals(String vision, String content, String address, String name,String id) {
        this.vision = vision;
        this.content = content;
        this.address = address;
        this.name=name;
        this.id=id;
    }

    public Appeals(String vision) {
        this.vision = vision;
    }

    protected Appeals(Parcel in) {
        vision = in.readString();
        content = in.readString();
        address = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vision);
        parcel.writeString(content);
        parcel.writeString(address);
    }
}
