package com.echo.echofarm.Service;

import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;

public interface UserService {

    void detectUserInfo(String uid, GetUserInfoDtoListener getUserInfoDtoListener);
    String getUserUid();
    void getUserInfoDto(String uid, GetUserInfoDtoListener getUserInfoDtoListener);
    void sendUserDto(SendUserDto sendUserDto);
    void addUserLike(String sourceUid, GetUserInfoDto targetUserInfo);
    void deleteUserLike(String sourceUid, GetUserInfoDto targetUserInfo);
    void addUserTag(GetUserInfoDto targetUserInfo, String tag);
    void deleteUserTag(GetUserInfoDto targetUserInfo, String tag);
}
