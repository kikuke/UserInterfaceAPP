package com.echo.echofarm.Data.Dto;

import android.net.Uri;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class GetPostDto {

    private String uid;
    private String title;
    private List<Uri> imgSrc;
    private String contents;
    private String ownProduct;
    private List<String> ownTag;
    private String wantProduct;
    private List<String> wantTag;
    private boolean allowOther;
    private Date nowTime;
    private boolean complete;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Uri> getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(List<Uri> imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getOwnProduct() {
        return ownProduct;
    }

    public void setOwnProduct(String ownProduct) {
        this.ownProduct = ownProduct;
    }

    public List<String> getOwnTag() {
        return ownTag;
    }

    public void setOwnTag(List<String> ownTag) {
        this.ownTag = ownTag;
    }

    public String getWantProduct() {
        return wantProduct;
    }

    public void setWantProduct(String wantProduct) {
        this.wantProduct = wantProduct;
    }

    public List<String> getWantTag() {
        return wantTag;
    }

    public void setWantTag(List<String> wantTag) {
        this.wantTag = wantTag;
    }

    public boolean isAllowOther() {
        return allowOther;
    }

    public void setAllowOther(boolean allowOther) {
        this.allowOther = allowOther;
    }

    public Date getNowTime() {return nowTime;}

    public void setNowTime(Date nowTime) {this.nowTime = nowTime;}

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public String toString() {
        return "GetPostDto{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", imgSrc=" + imgSrc +
                ", contents='" + contents + '\'' +
                ", ownProduct='" + ownProduct + '\'' +
                ", ownTag=" + ownTag +
                ", wantProduct='" + wantProduct + '\'' +
                ", wantTag=" + wantTag +
                ", allowOther=" + allowOther +
                ", nowTime=" + nowTime +
                ", complete=" + complete +
                '}';
    }
}
