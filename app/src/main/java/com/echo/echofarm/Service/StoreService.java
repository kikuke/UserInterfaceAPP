package com.echo.echofarm.Service;

import android.net.Uri;

import com.echo.echofarm.Activity.PostInfo;
import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Interface.GetImgUrlListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.Interface.StoreImgListener;

public interface StoreService {

    void storeImage(Uri imageUri, String forderName, String photoName, StoreImgListener storeImgListener);

    void getAllImageUrl(String forderName, GetPostDto getPostDto, GetImgUrlListener getImgUrlListener);

    void getImageUrl(String forderName, String photoName, GetPostDto getPostDto, GetImgUrlListener getImgUrlListener);

    void getImageUrl(String postId, String photoName, PostInfo postInfo, GetPostInfoListener getPostInfoListener);
}
