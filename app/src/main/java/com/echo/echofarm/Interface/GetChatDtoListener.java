package com.echo.echofarm.Interface;

import com.echo.echofarm.Data.Dto.GetChatResultDto;

public interface GetChatDtoListener {
    void onSuccess(GetChatResultDto getChatDtoResult);
    void onFailed();
}
