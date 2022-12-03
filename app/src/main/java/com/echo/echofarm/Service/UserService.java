package com.echo.echofarm.Service;

import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;

public interface UserService {

    String getUserUid();
    void getUserInfoDto(String uid, GetUserInfoDtoListener getUserInfoDtoListener);
    void sendUserDto(SendUserDto sendUserDto);
}
