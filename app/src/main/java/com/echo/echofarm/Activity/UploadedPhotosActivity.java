package com.echo.echofarm.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class UploadedPhotosActivity extends AppCompatActivity {

    private ArrayList<UploadedPhotoData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_photos);

        Intent intent = getIntent();
        list = intent.getBundleExtra("BUNDLE").getParcelableArrayList("URI_ARRAY");


        Log.i("my", "in Uploaded Activity", null);
        for(int i = 0; i < list.size(); i++)
            Log.i("my", ""+list.get(i).getPhotoUri().toString(), null);


        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        PhotoViewPageAdapter photoViewPageAdapter = new PhotoViewPageAdapter(UploadedPhotosActivity.this, list);
        viewPager2.setAdapter(photoViewPageAdapter);
    }
    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.echo.echofarm", "com.echo.echofarm.ActivityEditPostActivity"));
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("URI_ARRAY", list);
        intent.putExtra("BUNDLE", bundle);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}