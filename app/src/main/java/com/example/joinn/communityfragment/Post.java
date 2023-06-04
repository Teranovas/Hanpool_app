package com.example.joinn.communityfragment;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Post implements Serializable, Parcelable {
    private String key;
    private String title;
    private String content;
    private String writer;
    private String imageUrl;
    private long timestamp;

    public Post() {
        // Firebase Realtime Database에서 객체를 deserialize할 때 필요한 빈 생성자
    }

    public Post(String key, String title, String content, String writer, String imageUrl, long timestamp) {
        this.key = key;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Parcelable 인터페이스 구현 메서드들
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(writer);
        dest.writeString(imageUrl);
        dest.writeLong(timestamp);
    }

    protected Post(Parcel in) {
        key = in.readString();
        title = in.readString();
        content = in.readString();
        writer = in.readString();
        imageUrl = in.readString();
        timestamp = in.readLong();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
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
