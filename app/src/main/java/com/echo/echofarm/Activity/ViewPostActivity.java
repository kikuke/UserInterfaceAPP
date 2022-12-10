package com.echo.echofarm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetImgUrlListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPager2 mPager;
    private RecyclerView.Adapter pagerAdapter;
    private int num_page;
    List<Uri> list = new ArrayList<>();

    ImageButton backButton;
    ImageButton homeButton;
    Button profileButton;
    ImageButton chatButton;
    Button alreadyExchangedButton;
    TextView myProductTag;
    TextView needProductTag;
    TextView permitOtherProduct;
    TextView productName;
    TextView productDesc;
    TextView postTime;
    TextView needProduct;
    TextView ownProduct;
    TextView complete;


    PostService postService;
    String postId;
    String userId;
    String postTitle;

    LinearLayout bottomLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        postService = new PostServiceImpl();
        backButton = (ImageButton) findViewById(R.id.backButton);
        homeButton = (ImageButton) findViewById(R.id.homeButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        chatButton = (ImageButton) findViewById(R.id.chatButton);
        alreadyExchangedButton = (Button) findViewById(R.id.check_product_already_exchanged);
        myProductTag = (TextView) findViewById(R.id.myProductTag);
        needProductTag = (TextView) findViewById(R.id.needProductTag);
        permitOtherProduct = (TextView) findViewById(R.id.permitOtherProduct);
        productName = (TextView) findViewById(R.id.productName);
        productDesc = (TextView) findViewById(R.id.productDesc);
        postTime = (TextView) findViewById(R.id.postTime);
        ownProduct = (TextView) findViewById(R.id.ownProduct);
        needProduct = (TextView) findViewById(R.id.needProduct);
        bottomLayout = findViewById(R.id.bottom_layout);
        complete = (TextView)findViewById(R.id.complete);

        mPager = findViewById(R.id.imageView);

        backButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);
        alreadyExchangedButton.setOnClickListener(this);


        UserService userService = new UserServiceImpl();

        Intent intent = getIntent();
        postId=intent.getStringExtra("postId");
        userId = intent.getStringExtra("userId");
        postTitle = intent.getStringExtra("postTitle");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>"+ postTitle +"</font>"));


        postService.getPostDto(postId, new GetImgUrlListener() {
            @Override
            public void onSuccess(GetPostDto getPostDto) {
                if(!getPostDto.getUid().equals(userService.getUserUid())) bottomLayout.setVisibility(View.VISIBLE);
                else alreadyExchangedButton.setVisibility(View.VISIBLE);
                productName.setText(getPostDto.getTitle());
                productDesc.setText(getPostDto.getContents());
                myProductTag.setText(tagListToString(getPostDto.getOwnTag()));
                needProductTag.setText(tagListToString(getPostDto.getWantTag()));
                ownProduct.setText(getPostDto.getOwnProduct());
                needProduct.setText(getPostDto.getWantProduct());

                if(getPostDto.isComplete()) {
                    alreadyExchangedButton.setVisibility(View.VISIBLE);
                    alreadyExchangedButton.setClickable(false);
                }
                getPostDto.setComplete(true);//거래체결
                
                list = getPostDto.getImgSrc();
                if(getPostDto.isComplete())
                    complete.setText("거래완료");
                else
                    complete.setText("거래를 기다리고 있어요");
                mPager.setAdapter(new PostViewPhotoAdapter(ViewPostActivity.this, list));

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
            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
            intent.putExtra("oppUserId", userId);
            startActivity(intent);
        }
        else if(v==chatButton){
            Intent intent = new Intent(getApplicationContext(),ChattingActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("postTitle", postTitle);
            startActivity(intent);
        }
        else if(v == alreadyExchangedButton) {

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