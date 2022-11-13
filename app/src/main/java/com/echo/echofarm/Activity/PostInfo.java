package com.echo.echofarm.Activity;

import android.net.Uri;

public class PostInfo {

    private String postId;
    private String id;
    private String title;
    private String tags;//리스트 형태잉
    private Uri imageUri;

    public PostInfo(String postId,String id, String title, String tags) {
        this.postId=postId;
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public String getPostId() {return postId;}
    public String getId() { return id; }
    public String getTitle() {
        return title;
    }
    public String getTags() {
        return tags;
    }
    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
         this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "postId='" + postId + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tags='" + tags + '\'' +
                ", imageUri=" + imageUri +
                '}';
    }
}
