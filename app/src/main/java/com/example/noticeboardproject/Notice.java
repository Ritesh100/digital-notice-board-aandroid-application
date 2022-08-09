package com.example.noticeboardproject;


import com.google.firebase.database.Exclude;

public class Notice {
    private String title;
    private String imageURL;
    private String key;
    private String description;
    private int position;

    public Notice() {
        //empty constructor needed
    }
    public Notice (int position){
        this.position = position;
    }
    public Notice(String title, String imageUrl ,String Des, String key) {
        if (title.trim().equals("")) {
            title = "No title";
        }
        this.title = title;
        this.imageURL = imageUrl;
        this.description = Des;
        this.key = key;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}