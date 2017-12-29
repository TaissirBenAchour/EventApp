package com.example.cassio.Graduation_Project;

/**
 * Created by cassio on 29/12/17.
 */

public class Messages {

    public String message;
    public String type;
    public String msgtime;

    public Messages() {
    }

    public Messages(String message, String type, String msgtime) {
        this.message = message;
        this.type = type;
        this.msgtime = msgtime;
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

    public String getmsgtime() {
        return msgtime;
    }

    public void setmsgtime(String msgtime) {
        this.msgtime = msgtime;
    }
}
