package com.triplet.tripper.models.location;

public class Location {
    private String province;
    private String date;
    private String event;
    private String location;
    private String content;
    private FileUrl image;
    private FileUrl video;
    private FileUrl audio;

    public Location(String province, String date, String event, String location, String content, FileUrl image, FileUrl video, FileUrl audio) {
        this.province = province;
        this.date = date;
        this.event = event;
        this.location = location;
        this.content = content;
        this.image = image;
        this.video = video;
        this.audio = audio;
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

    public FileUrl getImage() {
        return image;
    }

    public void setImage(FileUrl image) {
        this.image = image;
    }

    public FileUrl getVideo() {
        return video;
    }

    public void setVideo(FileUrl video) {
        this.video = video;
    }

    public FileUrl getAudio() {
        return audio;
    }

    public void setAudio(FileUrl audio) {
        this.audio = audio;
    }
}
