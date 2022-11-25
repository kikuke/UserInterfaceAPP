package com.echo.echofarm.Activity;

import static com.echo.echofarm.Activity.SettingTagActivity.separateString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.echo.echofarm.R;

import java.util.ArrayList;
import java.util.Arrays;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ArrayList<String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        recyclerView = findViewById(R.id.user_profile_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        list = new ArrayList<>();

        ArrayList<String> s = new ArrayList<>(Arrays.asList("한글", "둖ㄷㄴ훋ㄴ", "나이키"
                , "일이삼사오육칠팔구십일이삼사오육", "아디다스",
                "일이삼사오육칠팔구십일이삼사오"));

        separateString(s, list);

        TagAdapter tagAdapter = new TagAdapter(this, list, 0);
        recyclerView.setAdapter(tagAdapter);

        Button button = findViewById(R.id.tag_setting_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, SettingTagActivity.class);
                startActivity(intent);
            }
        });
    }
}