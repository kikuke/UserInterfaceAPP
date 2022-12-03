package com.echo.echofarm.Interface;


import com.echo.echofarm.Data.Dto.GetUserInfoDto;

public interface GetUserInfoDtoListener {
    void onSuccess(GetUserInfoDto getUserInfoDto);
    void onFailed();
}
