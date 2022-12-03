package com.echo.echofarm.Activity;

import static com.echo.echofarm.Activity.TagSettingActivity.separateString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.UserService;

import java.util.ArrayList;
import java.util.Arrays;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ArrayList<String>> list;
    private final int RESULT_TAG_SETTING = 0;
    private ImageButton userRecommendImageButton;
    private TextView userRecommendCount;
    private String oppUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userRecommendImageButton = findViewById(R.id.user_recommend_button);
        userRecommendCount = findViewById(R.id.user_recommend_info);

        Intent intent = getIntent();
        if(!TextUtils.isEmpty(intent.getStringExtra("oppUserId"))) {
            oppUserId = intent.getStringExtra("oppUserId");
            UserService userService = new UserServiceImpl();

            // if user recommend list 에 상대방 아이디가 없다면
            userRecommendImageButton.setImageResource(R.drawable.heart_empty);
            userRecommendImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userRecommendImageButton.setImageResource(R.drawable.hear_filled);
                    userRecommendImageButton.setClickable(false);
                    //userRecommendCount.setText(상대 유저 추천수 + 1);
                    //서버 코드에 반영
                }
            });
        }

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
                Intent intent = new Intent(UserProfileActivity.this, TagSettingActivity.class);
                startActivityForResult(intent, RESULT_TAG_SETTING);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_TAG_SETTING && resultCode == RESULT_OK) {
            list = (ArrayList<ArrayList<String>>) data.getSerializableExtra("tagList");

            ArrayList<String> slist = new ArrayList<>();
            for(int i = 0; i < list.size(); i++)
                for(int j = 0; j < list.get(i).size(); j++)
                    slist.add(list.get(i).get(j));
            list = new ArrayList<>();

            separateString(slist, list);

            TagAdapter tagAdapter = new TagAdapter(this, list, 0);
            recyclerView.setAdapter(tagAdapter);
        }
    }
}