package com.echo.echofarm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.PostService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // xml 뷰, 뷰그룹
    private RecyclerView recyclerView;
    private ProgressBar loadingPB;
    private NestedScrollView nestedSV;
    private Button morePostBtn;
    private ImageButton editPostBtn, chattingBtn, settingBtn;
    //
    // post에 들어갈 데이터 예제, PostAdapter의 매개변수
    private String[] titles = {"1", "2", "3", "4", "5"};
    private String[] message = {"m1", "m2", "m3", "m4", "m5"};
    private int[] images = {R.drawable.settings, R.drawable.edit, R.drawable.chatting,
            R.drawable.settings, R.drawable.exchange};

    private ArrayList<PostInfo> postInfoArrayList;
    private PostAdapter postAdapter;
    private int postCount = 0;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        getData(); // 화면에 뿌릴 초기 데이터

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
                    Log.i("my", "" + postCount, null);
                    loadingPB.setVisibility(View.VISIBLE); // progressBar 생성

                    // 데이터 5개씩 불러옴
                    getData();
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

    private void getData() {
        postInfoArrayList = new ArrayList<>();
        GetPostListDto getPostListDto = new GetPostListDto();
        PostService postService = new PostServiceImpl();
        postService.getPostList(getPostListDto, null, 3, postInfoArrayList, new GetPostInfoListener() {
            @Override
            public void onSuccess(PostInfo postInfo) {
                Log.i("my", "success", null);
                String beforePostId = postInfoArrayList.get(postInfoArrayList.size()-1).getPostId();

                //메인에서 또는 프로필에서 게시글 리스트 불러올때.
                postService.getPostList(getPostListDto,
                        beforePostId, 3, postInfoArrayList, new GetPostInfoListener() {
                            //데이터는 일괄 로딩되지만, 사진은 한장씩 로딩됨.
                            @Override
                            public void onSuccess(PostInfo postInfo) {
                                //현재 postInfoList 다 다운 되있을거임
                                //System.out.println(postInfoList);

                                postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
                                recyclerView.setAdapter(postAdapter);
                                Log.d(TAG, "GetPostInfo: " + postInfo);
                                postAdapter = new PostAdapter(MainActivity.this, postInfoArrayList);
                                recyclerView.setAdapter(postAdapter);
                            }

                            @Override
                            public void onFailed() {
                                Log.i("my", "fail", null);
                            }
                        });
            }

            @Override
            public void onFailed() {
                Log.i("my", "failed", null);
            }
        });

    }
}