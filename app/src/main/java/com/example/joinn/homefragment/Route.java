package com.example.joinn.homefragment;

public class Route {

    private String startAddress;
    private String endAddress;
    private LatLng start;
    private LatLng end;
    private String nickname;
    private static String imageURL;
    private String level;
    private String spottxt;

    // 기본 생성자는 Firebase가 데이터를 자바 객체로 변환하는데 필요합니다.
    public Route() {
    }

    public Route(LatLng start, LatLng end, String nickname, String imageURL, String level, String spottxt, String startAddress, String endAddress) {
        this.start = start;
        this.end = end;
        this.nickname = nickname;
        this.imageURL = imageURL;
        this.level = level;
        this.spottxt = spottxt;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    public LatLng getStart() {
        return start;
    }

    public void setStart(LatLng start) {
        this.start = start;
    }

    public LatLng getEnd() {
        return end;
    }

    public void setEnd(LatLng end) {
        this.end = end;
    }

    public String getNickname() { return nickname;}

    public void setNickname(String nickname) {this.nickname= nickname;}

    public static String getImageURL() { return imageURL;}

    public void setImageURL(String imageURL) {this.imageURL= imageURL;}

    public String getLevel() { return level;}

    public void setLevel(String level) {this.level= level;}

    public String getSpottxt() {return spottxt;}

    public void setSpottxt(String spottxt) { this.spottxt = spottxt; }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

}

