package com.echo.echofarm.Data.Dto;

import java.util.ArrayList;
import java.util.List;

public class GetUserInfoDto {

    private String uid;
    private String name;
    private int like;
    private List<String> tags = new ArrayList<>();
    private List<String> likeUser = new ArrayList<>();
    private List<String> likedUser= new ArrayList<>();

    public GetUserInfoDto() {};
    public GetUserInfoDto(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }
    public GetUserInfoDto(String uid, String name, int like, List<String> tags, List<String> likeUser, List<String> likedUser) {
        this.uid = uid;
        this.name = name;
        this.like = like;
        this.tags = tags;
        this.likeUser = likeUser;
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
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(List<String> likeUser) {
        this.likeUser = likeUser;
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
                ", like=" + like +
                ", tags=" + tags +
                ", likeUser=" + likeUser +
                ", likedUser=" + likedUser +
                '}';
    }
}
