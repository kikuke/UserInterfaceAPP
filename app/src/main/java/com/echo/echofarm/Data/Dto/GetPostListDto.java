package com.echo.echofarm.Data.Dto;

import java.util.List;

public class GetPostListDto {

    public GetPostListDto(){}

    public GetPostListDto(String uid, String title, String ownProduct, List<String> ownTag, String wantProduct, List<String> wantTag, Boolean allowOther) {
        this.uid = uid;
        this.title = title;
        this.ownProduct = ownProduct;
        this.ownTag = ownTag;
        this.wantProduct = wantProduct;
        this.wantTag = wantTag;
        this.allowOther = allowOther;
    }

    private String uid;
    private String title;
    private String ownProduct;
    private List<String> ownTag;
    private String wantProduct;
    private List<String> wantTag;

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

    public Boolean getAllowOther() {
        return allowOther;
    }

    public void setAllowOther(Boolean allowOther) {
        this.allowOther = allowOther;
    }

    private Boolean allowOther;

    @Override
    public String toString() {
        return "GetPostListDto{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", ownProduct='" + ownProduct + '\'' +
                ", ownTag=" + ownTag +
                ", wantProduct='" + wantProduct + '\'' +
                ", wantTag=" + wantTag +
                ", allowOther=" + allowOther +
                '}';
    }
}
