package com.example.cassio.Graduation_Project.models;

/**
 * Created by cassio on 29/12/17.
 */

public class messagesClass {

    public String message;
    public String type;
    public String msgtime;
    public String from;
    public String date;
    public String image;
    public String idReciever;
    public String recieverName;

    public messagesClass() {
    }
    public messagesClass(String message) {
        this.message=message;
    }
    public messagesClass(String message, String type, String msgtime, String from, String date) {
        this.message = message;
        this.type = type;
        this.msgtime = msgtime;
        this.from = from;
        this.date = date;
    }

    public String getIdReciever() {
        return idReciever;
    }

    public void setIdReciever(String idReciever) {
        this.idReciever = idReciever;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
    }

    public messagesClass(String message, String msgtime, String from, String recieverName , String idReciever,String image) {
        this.message = message;
        this.msgtime = msgtime;
    this.from=from;
    this.recieverName=recieverName;
    this.idReciever=idReciever;
        this.image=image;


    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
