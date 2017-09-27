package com.sylvain.domisoin.Models;

/**
 * Created by Sylvain on 27/03/2017.
 */

public class MessageModel {

    private String from = null;
    private String to = null;
    private String message = null;

    public MessageModel() {

    }

    public MessageModel(String from, String to, String message) {
        this.from=from;
        this.to=to;
        this.message=message;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
