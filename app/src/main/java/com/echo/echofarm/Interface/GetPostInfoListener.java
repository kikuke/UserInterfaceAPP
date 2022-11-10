package com.echo.echofarm.Interface;

import com.echo.echofarm.Activity.PostInfo;
import com.echo.echofarm.Data.Dto.GetPostDto;

public interface GetPostInfoListener {
    void onSuccess(PostInfo postInfo);
    void onFailed();
}
