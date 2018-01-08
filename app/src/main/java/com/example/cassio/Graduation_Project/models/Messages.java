package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 29/12/17.
 */

public class Messages {

    public String message;
    public String type;
    public String msgtime;
    public String from;
    public String date;

    public Messages() {
    }

    public Messages(String message, String type, String msgtime, String from, String date) {
        this.message = message;
        this.type = type;
        this.msgtime = msgtime;
        this.from=from;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getmsgtime() {
        return msgtime;
    }

    public void setmsgtime(String msgtime) {
        this.msgtime = msgtime;
    }
}
