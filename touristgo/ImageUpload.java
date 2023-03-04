package com.example.user.touristgo;

/**
 * Created by sirinebaba on 4/17/17.
 */



public class ImageUpload {

    public String name;
    public String url;
    String userId;



    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    public String getUserId(){ return userId;}

    public ImageUpload(String name, String url,String userId) {
        this.name = name;
        this.url = url;
        this.userId = userId;
    }
}
