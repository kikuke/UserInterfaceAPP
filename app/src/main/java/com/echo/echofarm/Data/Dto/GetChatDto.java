package com.echo.echofarm.Data.Dto;

import java.util.Date;

public class GetChatDto {

    private String chatId;
    private String uid;
    private String name;
    private String message;
    private Date nowTime;
    private boolean isRead;

    public GetChatDto(String chatId, String uid, String name, String message, Date nowTime, boolean isRead) {
        this.chatId = chatId;
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.nowTime = nowTime;
        this.isRead = isRead;
    }

    public GetChatDto() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
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
        return "GetChatDto{" +
                "chatId='" + chatId + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", nowTime=" + nowTime +
                ", isRead=" + isRead +
                '}';
    }
}
