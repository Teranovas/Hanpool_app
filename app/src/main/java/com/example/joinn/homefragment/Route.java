package com.example.joinn.homefragment;

public class Route {
    private LatLng start;
    private LatLng end;

    // 기본 생성자는 Firebase가 데이터를 자바 객체로 변환하는데 필요합니다.
    public Route() {
    }

    public Route(LatLng start, LatLng end) {
        this.start = start;
        this.end = end;
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
}

