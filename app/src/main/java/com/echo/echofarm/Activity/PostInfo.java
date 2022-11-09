package com.echo.echofarm.Activity;

public class PostInfo {

    private int id;
    private String title;
    private String tags;
    private int imageUri;

    public PostInfo(int id, String title, String tags, int imageUri) {
        this.id = id;
        this.title = title;
        this.tags = tags;
        this.imageUri = imageUri;
    }
    public int getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getTags() {
        return tags;
    }
    public int getImageUri() {
        return imageUri;
    }
}
