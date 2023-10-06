package com.example.joinn.mypagefragment;

public class Review {
    private String photoUrl;
    private String nickname;
    private long date;

    public Review() {
        // 기본 생성자는 Firebase 데이터베이스에서 데이터를 객체로 읽을 때 필요합니다.
    }

    public Review(String photoUrl, String nickname, long date) {
        this.photoUrl = photoUrl;
        this.nickname = nickname;
        this.date = date;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
