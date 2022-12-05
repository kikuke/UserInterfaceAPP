package com.echo.echofarm.Data.Dto;

import java.util.ArrayList;
import java.util.List;

public class SendUserDto {

    private String uid;
    private String name;
    private List<String> tags = new ArrayList<>();
    private List<String> likedUser= new ArrayList<>();

    public SendUserDto() {};

    public SendUserDto(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public SendUserDto(String uid, String name, List<String> tags, List<String> likedUser) {
        this.uid = uid;
        this.name = name;
        this.tags = tags;
        this.likedUser = likedUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLike() {
        return likedUser.size();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getLikedUser() {
        return likedUser;
    }

    public void setLikedUser(List<String> likedUser) {
        this.likedUser = likedUser;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                ", likedUser=" + likedUser +
                '}';
    }
}
