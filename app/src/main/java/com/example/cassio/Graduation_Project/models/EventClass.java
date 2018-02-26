package com.example.cassio.Graduation_Project.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cassio on 24/12/17.
 */

public class EventClass implements Parcelable {
    public static final Creator<EventClass> CREATOR = new Creator<EventClass>() {
        @Override
        public EventClass createFromParcel(Parcel in) {
            return new EventClass(in);
        }

        @Override
        public EventClass[] newArray(int size) {
            return new EventClass[size];
        }
    };
    private String title;
    private String description;
    private String imageEvent;
    private String date;
    private String time ;
    private String address;
    private String price;
    private String month;
    private String pushId;
    private String eventId;

    public EventClass(Parcel in) {

    }

    public EventClass() {
    }

    public  EventClass(String title){
        this.title=title;
    }

    public EventClass(String title, String desc, String date, String pushId , String eventId,String month,String imageEvent) {
        this.title = title;
        this.description=desc;
        this.date=date;
        this.pushId=pushId;
        this.eventId=eventId;
        this.month=month;
        this.imageEvent=imageEvent;

    }

    public EventClass(String title, String description, String imageEvent, String date, String time, String address, String price, String month) {
        this.title = title;
        this.description = description;
        this.imageEvent = imageEvent;
        this.date = date;
        this.time = time;
        this.address = address;
        this.price = price;
        this.month = month;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageEvent);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(address);
        parcel.writeString(price);
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

    public String getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(String imageEvent) {
        this.imageEvent = imageEvent;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String  getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
