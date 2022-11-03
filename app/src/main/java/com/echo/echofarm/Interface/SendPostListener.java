package com.echo.echofarm.Interface;


public interface SendPostListener {
    void onSuccess(String postId);
    void onFailed();
}
