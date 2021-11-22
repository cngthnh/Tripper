package com.triplet.tripper.models.location;

public class FileUrl {
    private String url;

    public  FileUrl(){

    }

    public FileUrl(String fileUrl) {
        this.url = fileUrl;
    }

    public String getFileUrl() {
        return url;
    }

    public void setFileUrl(String fileUrl) {
        this.url = fileUrl;
    }
}
