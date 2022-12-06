package com.echo.echofarm.Data.Entity;

import java.util.Date;

//시간순 오름차순 정렬하기
public class Chat {

    private String uid;
    private String name;
    private String message;
    private Date nowTime = new Date();
    private boolean isRead;

    public Chat(){}

    public Chat(String uid, String name, String message) {
        this.uid = uid;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "uid='" + uid + '\'' +
                ", message='" + message + '\'' +
                ", nowTime=" + nowTime +
                ", isRead=" + isRead +
                '}';
    }
}
