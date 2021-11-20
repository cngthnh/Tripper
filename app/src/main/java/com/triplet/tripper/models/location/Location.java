package com.triplet.tripper.models.location;

public class Location {
    private String province;
    private String date;
    private String event;
    private String location;

    public Location(String province, String date, String event, String location) {
        this.province = province;
        this.date = date;
        this.event = event;
        this.location = location;
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
}
