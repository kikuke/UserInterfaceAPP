package com.echo.echofarm.Interface;

import com.echo.echofarm.Data.Dto.GetPostDto;

public interface GetPostListener {
    void onSuccess(GetPostDto getPostDto);
    void onFailed();
}
