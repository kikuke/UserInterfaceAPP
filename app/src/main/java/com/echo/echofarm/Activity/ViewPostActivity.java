package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetImgUrlListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.PostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager2 mPager;
    private RecyclerView.Adapter pagerAdapter;
    private int num_page;
    List<Uri> list;

    Button backButton;
    Button homeButton;
    Button profileButton;
    Button chatButton;
    Button likeButton;
    TextView myProductTag;
    TextView needProductTag;
    TextView permitOtherProduct;
    TextView productName;
    TextView productDesc;
    TextView postTime;
    TextView needProduct;
    TextView ownProduct;


    PostService postService;
    String postId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        postService = new PostServiceImpl();
        backButton = (Button) findViewById(R.id.backButton);
        homeButton = (Button) findViewById(R.id.homeButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        chatButton = (Button) findViewById(R.id.chatButton);
        likeButton = (Button) findViewById(R.id.likeButton);
        myProductTag = (TextView) findViewById(R.id.myProductTag);
        needProductTag = (TextView) findViewById(R.id.needProductTag);
        permitOtherProduct = (TextView) findViewById(R.id.permitOtherProduct);
        productName = (TextView) findViewById(R.id.productName);
        productDesc = (TextView) findViewById(R.id.productDesc);
        postTime = (TextView) findViewById(R.id.postTime);
        ownProduct = (TextView) findViewById(R.id.ownProduct);
        needProduct = (TextView) findViewById(R.id.needProduct);

        backButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);

        Intent intent = getIntent();
        postId=intent.getStringExtra("postId");

        postService.getPostDto(postId, new GetImgUrlListener() {
            @Override
            public void onSuccess(GetPostDto getPostDto) {
                productName.setText(getPostDto.getTitle());
                productDesc.setText(getPostDto.getContents());
                myProductTag.setText(tagListToString(getPostDto.getOwnTag()));
                needProductTag.setText(tagListToString(getPostDto.getWantTag()));
                ownProduct.setText(getPostDto.getOwnProduct());
                needProduct.setText(getPostDto.getWantProduct());
                list = getPostDto.getImgSrc();

                if(getPostDto.isAllowOther())
                    permitOtherProduct.setText("다른 물품 허용");
                else
                    permitOtherProduct.setText("다른물품 허용 안함");
                postTime.setText("업로드 시간:"+ getPostDto.getNowTime().toString());



            }

            @Override
            public void onFailed() {
                Log.d("chanhos", "failed");
            }
        });

        mPager = findViewById(R.id.imageView);
        mPager.setAdapter(new PostViewPhotoAdapter(this, list));


        }
    @Override
    public void onClick(View v){
        if(v==backButton) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v==homeButton){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v==profileButton){

        }
        else if(v==chatButton){
            Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
            intent.putExtra("postId",postId);
            startActivity(intent);
        }

    }

    private String tagListToString(List<String> tags){
        String result = "";
        for(String tag: tags){
            result += " #" + tag;
        }

        return result;
    }
}