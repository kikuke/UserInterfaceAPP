package com.echo.echofarm.Activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.FcmService;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.PushUpdateService;
import com.echo.echofarm.Service.UserService;

import java.util.ArrayList;

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

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 채팅 수신
        fcmService.subscribeTopic("user_" + userService.getUserUid());
        ComponentName componentName = new ComponentName(this, PushUpdateService.class);
        JobInfo info = new JobInfo.Builder(999, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(5 * 60 * 1000)//5분간격 실행
                .build();
        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(info);
        if(resultCode == jobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "푸시업데이트 서비스 실행");
        } else {
            Log.d(TAG, "푸시업데이트 서비스 실패");
        }

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


        getData(postCount); // 화면에 뿌릴 초기 데이터

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // ex) 3 * 5개 게시물 표시시 '더 많은 게시물'버튼 생성
                if(postCount >= 5*3) {
                    loadingPB.setVisibility(View.GONE);
                    morePostBtn.setVisibility(View.VISIBLE);
                }
                // 스크롤이 끝이라면 데이터 불러옴
                else if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    loadingPB.setVisibility(View.VISIBLE); // progressBar 생성

                    // 데이터 불러옴
                    getData(postCount);
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

    private void getData(int count) {

        String beforeId;

        if(count == 0)
            beforeId = null;
        else {
            beforeId = postInfoArrayList.get(postInfoArrayList.size() - 1).getPostId();
            postInfoArrayList = new ArrayList<>();
        }

        Log.i("my", "beforeId : " + beforeId, null);


        postService.getPostList(getPostListDto, beforeId, 3, postInfoArrayList, new GetPostInfoListener() {
            @Override
            public void onSuccess(PostInfo postInfo) {
                Log.i("my", "success", null);

                Log.i("my", "size : " + postInfoArrayList.size(), null);
                postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
                recyclerView.setAdapter(postAdapter);
                Log.e(TAG, "GetPostInfo: " + postInfo);
                postCount++;

                // post가 view를 모두 채우지 않으면 재호출
                // 뷰가 다 채워지지 않으면 스크롤 리스너가 동작하지 않아서 넣어줘야 함
                // 지금 beforeId 때문에 에러나서 주석처리함

                if(postCount < 3) {
                    Log.i("my", "postCount : " + postCount, null);
                    getData(postCount);
                }
            }

            @Override
            public void onFailed() {
                morePostBtn.setVisibility(View.VISIBLE);
                Log.i("my", "failed", null);
            }
        });

    }
}