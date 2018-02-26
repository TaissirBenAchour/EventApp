package com.example.cassio.Graduation_Project.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cassio on 28/01/18.
 */

public class Committee implements Parcelable {

String topic;
    String idea ;
String where;
String money;
String profile;

    public Committee() {
    }

    public Committee(String topic, String idea , String where, String money, String profile) {
        this.topic = topic;
        this.idea = idea;
        this.where = where;
        this.money = money;
        this.profile = profile;
    }

    public Committee(String topic) {
        this.topic = topic;
    }

    protected Committee(Parcel in) {
        idea = in.readString();
        topic = in.readString();
        where = in.readString();
        money = in.readString();
        profile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idea);
        dest.writeString(topic);
        dest.writeString(where);
        dest.writeString(money);
        dest.writeString(profile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Committee> CREATOR = new Creator<Committee>() {
        @Override
        public Committee createFromParcel(Parcel in) {
            return new Committee(in);
        }

        @Override
        public Committee[] newArray(int size) {
            return new Committee[size];
        }
    };

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
