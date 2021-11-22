package com.triplet.tripper.models.location;

import com.google.android.gms.maps.model.LatLng;

public class LocationRecord {
    private String province;
    private String date;
    private String event;
    private String location;
    private String content;
    private FileUrl imageUrl;
    private FileUrl videoUrl;
    private Double latitude;
    private Double longitude;


    public LocationRecord(){

    }

    public LocationRecord(String province, String date, String event, String location, String content, FileUrl imageUrl, FileUrl videoUrl, Double latitude, Double longitude) {
        this.province = province;
        this.date = date;
        this.event = event;
        this.location = location;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FileUrl getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(FileUrl imageUrl) {
        this.imageUrl = imageUrl;
    }

    public FileUrl getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(FileUrl videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
