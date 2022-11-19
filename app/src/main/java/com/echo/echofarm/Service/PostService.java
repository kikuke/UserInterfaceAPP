package com.echo.echofarm.Service;

import com.echo.echofarm.Activity.PostInfo;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Data.Dto.SendPostDto;
import com.echo.echofarm.Interface.GetImgUrlListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.Interface.StoreImgListener;
import com.google.firebase.firestore.Query;

import java.util.List;

import java.util.List;

public interface PostService {

    void getPostList(GetPostListDto getPostListDto,
                     String beforePostId, Integer limitSize, List<PostInfo> postInfoList, GetPostInfoListener getPostInfoListener);

    void sendPostDto(SendPostDto sendPostDto, StoreImgListener sendPostListener);

    void getPostDto(String postId, GetImgUrlListener getPostListener);

}
