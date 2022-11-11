package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.echo.echofarm.R;

public class ViewPostActivity extends AppCompatActivity implements View.OnClickListener{

    Button backButton;
    Button homeButton;
    Button profileButton;
    Button likeButton;
    Button chatButton;
    TextView myProductTag;
    TextView needProductTag;
    TextView permitOtherProduct;
    TextView productName;
    TextView productDesc;
    TextView postTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        backButton = (Button) findViewById(R.id.backButton);
        homeButton = (Button) findViewById(R.id.homeButton);
        profileButton = (Button) findViewById(R.id.profileButton);
        likeButton = (Button) findViewById(R.id.likeButton);
        chatButton = (Button) findViewById(R.id.chatButton);
        myProductTag = (TextView) findViewById(R.id.myProductTag);
        needProductTag = (TextView) findViewById(R.id.needProductTag);
        permitOtherProduct = (TextView) findViewById(R.id.permitOtherProduct);
        productName = (TextView) findViewById(R.id.productName);
        productDesc = (TextView) findViewById(R.id.productDesc);
        postTime = (TextView) findViewById(R.id.postTime);

        backButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        chatButton.setOnClickListener(this);

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
        }
        else if(v==profileButton){

        }
        else if(v==likeButton){

        }
        else if(v==chatButton){

        }

    }
}