package com.example.joinn.communityfragment;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Post implements Serializable, Parcelable {
    private String key;
    private String title;
    private String startpoint;

    private String arrivepoint;
    private String writer;
    private String imageUrl;
    private long timestamp;

    private boolean taxi;
    private double latitude; // 게시물의 위치 위도
    private double longitude; // 게시물의 위치 경도

    public Post() {
        // Firebase Realtime Database에서 객체를 deserialize할 때 필요한 빈 생성자
    }

    public Post(String key, String title, String startpoint, String arrivepoint
                ,String writer, String imageUrl, long timestamp) {
        this.key = key;
        this.title = title;
        this.startpoint = startpoint;
        this.arrivepoint = arrivepoint;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }
    public Post(String key, String title, String startpoint, String arrivepoint
            ,String writer, String imageUrl, long timestamp, boolean taxi, double latitude, double longitude) {
        this.key = key;
        this.title = title;
        this.startpoint = startpoint;
        this.arrivepoint = arrivepoint;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.taxi = taxi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // getter/setter 메서드들
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getArrivepoint() {
        return arrivepoint;
    }

    public void setArrivepoint(String arrivepoint) {
        this.arrivepoint = arrivepoint;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
    public boolean getTaxi() {
        return taxi;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude(){return latitude;}
    public void setLatitude(double latitude){this. latitude = latitude;}

    public double getLongitude(){return longitude;}
    public void setLongitude(double longitude){this. longitude = longitude;}

    // Parcelable 인터페이스 구현 메서드들
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(startpoint);
        dest.writeString(arrivepoint);
        dest.writeString(writer);
        dest.writeString(imageUrl);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (taxi ? 1 : 0)); // 0이면 false, 1이면 true
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    protected Post(Parcel in) {
        key = in.readString();
        title = in.readString();
        startpoint = in.readString();
        arrivepoint = in.readString();
        writer = in.readString();
        imageUrl = in.readString();
        timestamp = in.readLong();
        taxi = in.readByte() != 0; // 0이면 false, 1이면 true
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
