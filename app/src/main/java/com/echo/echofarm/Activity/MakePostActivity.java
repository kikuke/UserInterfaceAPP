package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.echo.echofarm.Data.Dto.SendPostDto;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class MakePostActivity extends AppCompatActivity {
    PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postService = new PostServiceImpl();

        setContentView(R.layout.activity_make_post);

        SendPostDto sendPostDto = new SendPostDto("TestUid", "TestTitle", Arrays.asList("imgSrc1", "imgSrc2"), "테스트 콘텐츠 입니다.",
                "Pencil", Arrays.asList("tag1", "tag2"), "ball", Arrays.asList("tag3", "tag4"), false);

        postService.sendPostDto(sendPostDto);
    }

}