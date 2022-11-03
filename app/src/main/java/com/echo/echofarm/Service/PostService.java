package com.echo.echofarm.Service;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.SendPostDto;
import com.echo.echofarm.Interface.GetPostListener;
import com.echo.echofarm.Interface.SendPostListener;

public interface PostService {

    void sendPostDto(SendPostDto sendPostDto, SendPostListener sendPostListener);

    void getPostDto(String postId, GetPostListener getPostListener);

}
