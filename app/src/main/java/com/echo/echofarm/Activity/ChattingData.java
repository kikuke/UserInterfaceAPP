package com.echo.echofarm.Activity;

import java.util.ArrayList;

public class ChattingData {

    private String message;

    // 0 : user, 1 : opponent
    private int chatCode;

    public ChattingData(String message, int chatCode) {
        this.message = message;
        this.chatCode = chatCode;
    }

    public String getMessage() {
        return message;
    }

    public int getChatCode() {
        return chatCode;
    }
}
