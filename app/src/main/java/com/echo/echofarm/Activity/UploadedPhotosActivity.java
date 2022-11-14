package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class UploadedPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_photos);

        Intent intent = getIntent();
        ArrayList<UploadedPhotoData> list = intent.getBundleExtra("BUNDLE").getParcelableArrayList("URI_ARRAY");

        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        PhotoViewPageAdapter photoViewPageAdapter = new PhotoViewPageAdapter(UploadedPhotosActivity.this, list);
        viewPager2.setAdapter(photoViewPageAdapter);
    }
}