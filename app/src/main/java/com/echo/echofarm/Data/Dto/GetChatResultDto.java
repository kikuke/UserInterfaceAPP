package com.echo.echofarm.Data.Dto;

import java.util.ArrayList;
import java.util.List;

public class GetChatResultDto {
    private List<GetChatDto> getChatDtoList = new ArrayList<>();

    public GetChatResultDto(){}

    public GetChatResultDto(List<GetChatDto> getChatDtoList) {
        this.getChatDtoList = getChatDtoList;
    }

    public List<GetChatDto> getGetChatDtoList() {
        return getChatDtoList;
    }

    public void setGetChatDtoList(List<GetChatDto> getChatDtoList) {
        this.getChatDtoList = getChatDtoList;
    }

    @Override
    public String toString() {
        return "GetChatResultDto{" +
                "getChatDtoList=" + getChatDtoList +
                '}';
    }
}
