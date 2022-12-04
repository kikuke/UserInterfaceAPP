package com.echo.echofarm.Activity;

import static com.echo.echofarm.Activity.TagSettingActivity.separateString;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.UserService;

import java.util.ArrayList;
import java.util.Arrays;

public class UserProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ArrayList<String>> list;
    private final int RESULT_TAG_SETTING = 0;
    private ImageButton userRecommendBtn;
    private ImageView heart_filled_Image;
    private Button tagSettingButton;
    private TextView userRecommendCount;
    private String userId;
    private RelativeLayout tagSettingLayout;
    private GetUserInfoDto userInfoDto;
    private boolean buttonFlag = true;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>프로필</font>"));

        userRecommendBtn = findViewById(R.id.user_recommend_empty_button);
        userRecommendCount = findViewById(R.id.user_recommend_info);
        heart_filled_Image = findViewById(R.id.user_recommend_filled_view);
        tagSettingButton = findViewById(R.id.tag_setting_button);
        tagSettingLayout = findViewById(R.id.tag_setting_layout);

        userService = new UserServiceImpl();


        Intent intent = getIntent();
        // 상대 프로필
        if(!TextUtils.isEmpty(intent.getStringExtra("oppUserId"))
                    && !userService.getUserUid().equals(intent.getStringExtra("oppUserId"))) {
            userId = intent.getStringExtra("oppUserId");

            userService.getUserInfoDto(userId, new GetUserInfoDtoListener() {
                @Override
                public void onSuccess(GetUserInfoDto getUserInfoDto) {
                    userInfoDto = getUserInfoDto;
                    userRecommendCount.setText(""+userInfoDto.getLike());

                    // if user recommend list 에 상대방 아이디가 없다면
                    if(!userInfoDto.getLikedUser().contains(userService.getUserUid())) {
                        heart_filled_Image.setVisibility(View.INVISIBLE);
                        buttonFlag = false;
                    }

                    ArrayList<String> s = (ArrayList<String>) userInfoDto.getTags();

                    separateString(s, list);

                    TagAdapter tagAdapter = new TagAdapter(UserProfileActivity.this, list, userId,0);
                    recyclerView.setAdapter(tagAdapter);

                    // 상대 프로필일때만 클릭 리스너 작동
                    userRecommendBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArrayList<String> newList = (ArrayList<String>) userInfoDto.getLikedUser();
                            // 추천
                            if (!buttonFlag) {
                                Log.i("my", "recommend clicked");
                                newList.add(userService.getUserUid());
                                userInfoDto.setLikedUser(newList);

                                userService.sendUserDto(new SendUserDto(userId, userInfoDto.getName()));
                                heart_filled_Image.setVisibility(View.VISIBLE);
                                buttonFlag = true;
                            }
                            // 추천 삭제
                            else {
                                Log.i("my", "cancel recommend clicked");
                                newList.remove(userService.getUserUid());
                                userInfoDto.setLikedUser(newList);

                                userService.sendUserDto(new SendUserDto(userId, userInfoDto.getName()));
                                heart_filled_Image.setVisibility(View.INVISIBLE);
                                buttonFlag = false;
                            }
                        }
                    });
                }
                @Override
                public void onFailed() {

                }
            });

            tagSettingLayout.setVisibility(View.GONE);
        }
        // 내 프로필
        else {
            userId = userService.getUserUid();
            userService.getUserInfoDto(userId, new GetUserInfoDtoListener() {
                @Override
                public void onSuccess(GetUserInfoDto getUserInfoDto) {
                    Log.e("", "" + getUserInfoDto.getLike());
                    userInfoDto = getUserInfoDto;
                    userRecommendCount.setText("" + getUserInfoDto.getLike());

                    ArrayList<String> s = (ArrayList<String>) userInfoDto.getTags();

                    separateString(s, list);

                    TagAdapter tagAdapter = new TagAdapter(UserProfileActivity.this, list, userId,0);
                    recyclerView.setAdapter(tagAdapter);
                }

                @Override
                public void onFailed() {

                }
            });
        }



        recyclerView = findViewById(R.id.user_profile_recyclerview);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        list = new ArrayList<>();


        tagSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, TagSettingActivity.class);
                intent.putExtra("UserId", userId);
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

            TagAdapter tagAdapter = new TagAdapter(this, list, userId, 0);
            recyclerView.setAdapter(tagAdapter);
        }
    }
}