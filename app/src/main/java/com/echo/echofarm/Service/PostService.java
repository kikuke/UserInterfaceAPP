package com.echo.echofarm.Service;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.SendPostDto;

public interface PostService {

    void sendPostDto(SendPostDto sendPostDto);

    void getPostDto(String postId);

}
