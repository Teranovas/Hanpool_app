package com.example.joinn.chatfragment;

public class Message {

    private String message;
    private String senderId;

    private boolean isSelf;

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }
}