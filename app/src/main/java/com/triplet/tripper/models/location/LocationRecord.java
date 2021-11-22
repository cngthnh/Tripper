package com.triplet.tripper.models.location;

import com.google.android.gms.maps.model.LatLng;

public class LocationRecord {
    private LatLng province;
    private String date;
    private String event;
    private String location;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private String audioUrl;

    public LocationRecord(){

    }

    public LocationRecord(LatLng province, String date, String event, String location, String content, String imageUrl, String videoUrl, String audioUrl) {
        this.province = province;
        this.date = date;
        this.event = event;
        this.location = location;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.audioUrl = audioUrl;
    }

    public LatLng getProvince() {
        return province;
    }

    public void setProvince(LatLng province) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}
