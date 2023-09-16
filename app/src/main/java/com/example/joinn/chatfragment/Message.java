package com.example.joinn.chatfragment;

public class Message {

    private String message;
    private String sendId;
    private String time;
    private String name;
    private boolean isAcceptButtonVisible = false; // 수락 버튼 표시 여부
    private boolean isRejectButtonVisible = false; // 거절 버튼 표시 여부

    public Message() {
        // Default constructor required for Firebase
    }

    public Message(String message, String sendId, String time, String name) {
        this.message = message;
        this.sendId = sendId;
        this.time = time;
        this.name = name;
        this.isAcceptButtonVisible = false; // 초기값으로 버튼을 숨김
        this.isRejectButtonVisible = false; // 초기값으로 버튼을 숨김
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

    public boolean isAcceptButtonVisible() {
        return isAcceptButtonVisible;
    }

    public void setAcceptButtonVisible(boolean acceptButtonVisible) {
        isAcceptButtonVisible = acceptButtonVisible;
    }

    public boolean isRejectButtonVisible() {
        return isRejectButtonVisible;
    }

    public void setRejectButtonVisible(boolean rejectButtonVisible) {
        isRejectButtonVisible = rejectButtonVisible;
    }
}
