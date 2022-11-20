package com.echo.echofarm.Data.Dto;

public class SendChatDto {

    private String message;

    public SendChatDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
