package com.example.joinn.chatfragment;

public class Message {

    private String message;
    private String sendId;
    private String time;
    private String name;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String message, String sendId, String time, String name) {
        this.message = message;
        this.sendId = sendId;
        this.time = time;
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public String getSendId() {
        return sendId;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
