package com.example.joinn.chatfragment;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User  {

    private String key;

    private String writer;
    private String imageUrl;

    private String uid;

////    private long timestamp;

    public User() {
        // Firebase Realtime Database에서 객체를 deserialize할 때 필요한 빈 생성자
    }

    public User(String key, String writer, String imageUrl){
        this.key = key;
        this.writer = writer;
        this.imageUrl = imageUrl;
//        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);

        dest.writeString(writer);
        dest.writeString(imageUrl);
//        dest.writeLong(timestamp);
    }

    protected User(Parcel in) {
        key = in.readString();
        writer = in.readString();
        imageUrl = in.readString();
//        timestamp = in.readLong();
    }

}
