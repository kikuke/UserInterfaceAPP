package com.echo.echofarm.Service;

import com.echo.echofarm.Data.Dto.SendChatDto;
import com.echo.echofarm.Interface.GetChatDtoListener;

public interface ChatService {

    void sendChat(String sender, String receiver, SendChatDto sendChatDto);

    void getChatList(String uid1, String uid2, String beforeChatId, GetChatDtoListener getChatDtoListener);
}
