package com.echo.echofarm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;

import com.echo.echofarm.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ChattingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>채팅</font>"));

        RecyclerView recyclerView = findViewById(R.id.chatting_list_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        ArrayList<String> names = new ArrayList<>();
        names.add("홍석희");
        names.add("이찬호");
        names.add("김진수");

        ArrayList<String> recent = new ArrayList<>();
        recent.add("대화1");
        recent.add("대화2");
        recent.add("대화3");

        ChattingListAdapter adapter = new ChattingListAdapter(this, names, recent);
        recyclerView.setAdapter(adapter);
    }
}