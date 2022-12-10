package com.echo.echofarm.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.echo.echofarm.Data.Dto.GetChatResultDto;
import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Interface.GetChatDtoListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.ChatService;
import com.echo.echofarm.Service.FcmService;
import com.echo.echofarm.Service.Impl.ChatServiceImpl;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.PushUpdateService;
import com.echo.echofarm.Service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // xml 뷰, 뷰그룹
    private RecyclerView recyclerView;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private Button morePostBtn;
    private ImageButton editPostBtn, chattingBtn, settingBtn;
    private ArrayList<PostInfo> postInfoArrayList;
    private PostAdapter postAdapter;
    private GetPostListDto getPostListDto = new GetPostListDto();
    private PostService postService = new PostServiceImpl();
    private UserService userService = new UserServiceImpl();
    private FcmService fcmService = new FcmService();
    private int postCount = 0;
    private int checkPositionBefore = 0;
    private ArrayList<String> tagList;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "User UID: "+ userService.getUserUid(), null);
        //FCM토큰 확인
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: "+ token);
                    }
                });


        //User사용 예시
        UserService userService = new UserServiceImpl();
        userService.sendUserDto(new SendUserDto("userUidTest", "userNameTest"));
        userService.getUserInfoDto("userUidTest", new GetUserInfoDtoListener() {
            @Override
            public void onSuccess(GetUserInfoDto getUserInfoDto) {
                System.out.println(getUserInfoDto);
            }

            @Override
            public void onFailed() {
                System.out.println("Failed UserDto");
            }
        });

        ChatService chatService = new ChatServiceImpl();
        chatService.detectChat("123", "369", null, new GetChatDtoListener() {
            @Override
            public void onSuccess(GetChatResultDto getChatDtoResult) {
                System.out.println(getChatDtoResult);
            }

            @Override
            public void onFailed() {

            }
        });

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>최근 게시물</font>"));

        // 액티비티 버튼 처리
        editPostBtn = findViewById(R.id.editPostBtn);
        chattingBtn = findViewById(R.id.chattingBtn);
        settingBtn = findViewById(R.id.settingBtn);
        editPostBtn.setOnClickListener(this);
        chattingBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);

        // post, 스크롤 관련 처리
        recyclerView = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        morePostBtn = findViewById(R.id.idBtn);
        postInfoArrayList = new ArrayList<>();


        postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
        recyclerView.setAdapter(postAdapter);

        userService.getUserInfoDto(userService.getUserUid(), new GetUserInfoDtoListener() {
            @Override
            public void onSuccess(GetUserInfoDto getUserInfoDto) {
                tagList = (ArrayList<String>) getUserInfoDto.getTags();
                if(tagList.size() != 0) {

                    getData(null, tagList);
                    Log.i("my", "tag size is not 0");

                }
                else {
                    getData(null, null);
                    Log.i("my", "tag size is 0");

                }
            }

            @Override
            public void onFailed() {

            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // ex) 3 * 5개 게시물 표시시 '더 많은 게시물'버튼 생성
                if(postCount >= 100) {
                    Log.i("my", "post count : " + postCount + " ======================");
                    loadingPB.setVisibility(View.GONE);
                    morePostBtn.setVisibility(View.VISIBLE);
                    morePostBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, SearchedPostActivity.class));
                        }
                    });
                }
                // 스크롤이 끝이라면 데이터 불러옴
                else if (checkPositionBefore != scrollY && (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    checkPositionBefore = scrollY + 126;
                    Log.i("my", checkPositionBefore + "", null);

                    if(postInfoArrayList.get(postInfoArrayList.size() - 1) == null) return;

                    // 데이터 불러옴
                    getData(postInfoArrayList.get(postInfoArrayList.size() - 1).getPostId(), null);
                }
            }
        });
    }
    // 액티비티 수준 onClick
    @Override
    public void onClick(View view) {
        if(view == editPostBtn) {
            startActivity(new Intent(this, EditPostActivity.class));
        } else if(view == chattingBtn) {
            startActivity(new Intent(this, ChattingListActivity.class));
        } else if(view == settingBtn) {
            startActivity(new Intent(this, UserProfileActivity.class));
        }
    }

    private void getData(String beforeId, ArrayList<String> tagList) {
        if(tagList != null) {
            for(String tag : tagList) {
                GetPostListDto getTagPostListDto = new GetPostListDto(null, null, null, Arrays.asList(tag), null, null, null);
                postService.getPostList(getTagPostListDto, null, 30, postInfoArrayList, new GetPostInfoListener() {
                    @Override
                    public void onSuccess(PostInfo postInfo) {

                        if(postInfo == null) {
                            Log.i("my", "null post");
                            return;
                        }

                        postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
                        recyclerView.setAdapter(postAdapter);
                        postCount++;
                        Log.i("my", "success " + postCount);
                        if(postInfoArrayList.size() == postCount)
                            getData(null, null);
                    }

                    @Override
                    public void onFailed() {
                        loadingPB.setVisibility(View.GONE);
                        morePostBtn.setVisibility(View.VISIBLE);
                        morePostBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MainActivity.this, SearchedPostActivity.class));
                            }
                        });
                        Log.i("my", "failed", null);
                    }
                });
            }
            return;
        }



        int beforePostCount = postCount;

        Log.i("my", "beforeId : " + beforeId + " , arrSize : " + postInfoArrayList.size(), null);

        postService.getPostList(getPostListDto, beforeId, 30, postInfoArrayList, new GetPostInfoListener() {
            @Override
            public void onSuccess(PostInfo postInfo) {
                if(postInfo == null) {
                    postAdapter.notifyItemRangeChanged(beforePostCount, postCount - beforePostCount);
                    Log.i("my", "null post");
                    return;
                }

                postCount++;
                Log.i("my", "success " + postCount);
                if(postInfoArrayList.size() == postCount) {
                    Log.i("my", "before size : " + postInfoArrayList.size());
                    LinkedHashSet<PostInfo> set = new LinkedHashSet<>(postInfoArrayList);
                    postInfoArrayList.clear();
                    postInfoArrayList.addAll(set);
                    Log.i("my", "after size : " + postInfoArrayList.size());
                    postCount = postInfoArrayList.size();

                    postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
                    recyclerView.setAdapter(postAdapter);
                }
            }

            @Override
            public void onFailed() {
                loadingPB.setVisibility(View.GONE);
                morePostBtn.setVisibility(View.VISIBLE);
                morePostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, SearchedPostActivity.class));
                    }
                });
                Log.i("my", "failed", null);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search_btn) {
            startActivity(new Intent(this, SearchedPostActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}