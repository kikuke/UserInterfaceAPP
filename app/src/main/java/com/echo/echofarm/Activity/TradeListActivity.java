package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.load.engine.Resource;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.PostService;

import java.util.ArrayList;
import java.util.List;

public class TradeListActivity extends AppCompatActivity {

    ArrayList<TradeListInfo> items;
    PostService postService;
    List<PostInfo> postInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_list);
        postService = new PostServiceImpl();
        postInfoList = new ArrayList<>();
        GetPostListDto getPostListDto2 = new GetPostListDto();
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        getPostListDto2.setUid(uid);

        postService.getPostList(getPostListDto2,
                null, 3, postInfoList, new GetPostInfoListener() {
                    //데이터는 일괄 로딩되지만, 사진은 한장씩 로딩됨.
                    //넉넉하게 로딩해야됨. 필터링되는게 있어서
                    @Override
                    public void onSuccess(PostInfo postInfo) {
                        //현재 postInfoList 다 다운 되있을거임
                        //System.out.println(postInfoList);
                        //현재 한장씩 다운로드 되는 사진들. 각 사진마다 해당 사진에 대해 다시 액티비티에 띄워야 함.
                        System.out.println("SearchUser PostList: " + postInfo);

                    }

                    @Override
                    public void onFailed() {
                        System.out.println("getPostInfo Failed");
                    }
                });

        int counts = postInfoList.size();
        ListView listView = findViewById(R.id.tradeLists);
        init_ArrayList(counts);
        TradeListAdapter mAdapter = new TradeListAdapter(this,items);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TradeListInfo item = (TradeListInfo) adapterView.getItemAtPosition(position);
            }
        });

        }

    // Item 리스트를 생성하는 함수
    private void init_ArrayList(int count) {
        // item을 저장할 List 생성
         items= new ArrayList<>();

        // Drawable 이미지 리소스 ID 값을 가져오기 위해 Resource객체 생성
        Resources res = getResources();

        // 함수의 인자로 넘겨준 count 아이템 개수만큼 반복, 아이템 추가
        for (int i = 0; i < count; i++) {
            // 이미지리소스 id값을 가져옴, res.getIdentifier("이미지 이름", "리소스 폴더 이름", 현재패키지 이름)
            // item 객체 생성하여 리스트에 추가
            items.add(new TradeListInfo(postInfoList.get(i).getImageUri(),postInfoList.get(i).getTitle(),postInfoList.get(i).getTags()));
        }
    }
}