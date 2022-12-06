package com.echo.echofarm.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.echo.echofarm.Data.Dto.GetChatDto;
import com.echo.echofarm.Data.Dto.GetChatResultDto;
import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendChatDto;
import com.echo.echofarm.Interface.GetChatDtoListener;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.ChatService;
import com.echo.echofarm.Service.Impl.ChatServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.UserService;
import com.google.android.material.tabs.TabLayout;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class ChattingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText sendingMessage;
    private Button sendMessageButton;
    private ArrayList<ChattingData> list;
    private ChattingDataAdapter adapter;

    private ChatService chatService;
    private UserService userService;

    private List<GetChatDto> chatList;

    private String oppId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        recyclerView = findViewById(R.id.chatting_recyclerView);
        sendingMessage = findViewById(R.id.sending_message_editText);
        sendMessageButton = findViewById(R.id.send_message_button);

        list = new ArrayList<>();
        chatList = new ArrayList<>();
        chatService = new ChatServiceImpl();
        userService = new UserServiceImpl();


        Intent intent = getIntent();
        oppId = intent.getStringExtra("userId");
        String postTitle = intent.getStringExtra("postTitle");
        Log.e("",userService.getUserUid() + " " + oppId + " ==================");


        chatService.detectChat(userService.getUserUid(), oppId, null, new GetChatDtoListener() {
            @Override
            public void onSuccess(GetChatResultDto getChatDtoResult) {
                if(!sendingMessage.getText().toString().equals(""))
                    list.add(new ChattingData(sendingMessage.getText().toString(), 1));

                if(list.size() >= 6) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                }
                setChatting();
            }

            @Override
            public void onFailed() {

            }
        });



        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>"+ postTitle +"</font>"));


        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        adapter = new ChattingDataAdapter(ChattingActivity.this, list, "");
        recyclerView.setAdapter(adapter);


        // 전송 버튼 클릭
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!sendingMessage.getText().toString().equals("")) {
                    String message = sendingMessage.getText().toString();
                    sendingMessage.setText("");
                    list.add(new ChattingData(message, 1));

                    // send message info to server
                    chatService.sendChat(userService.getUserUid(), oppId, new SendChatDto(message));
                }
            }
        });
    }

    private void setChatting() {
        chatService.getChatList(userService.getUserUid(), oppId, null, new GetChatDtoListener() {
            @Override
            public void onSuccess(GetChatResultDto getChatDtoResult) {
                chatList = getChatDtoResult.getGetChatDtoList();
                String oppName = "";

                for(int i = 0; i < chatList.size(); i++) {
                    int code = 0;

                    if(chatList.get(i).getUid().equals(userService.getUserUid())) {
                        code = 1;
                    } else {
                        oppName = chatList.get(i).getName();
                    }
                    ChattingData chattingData = new ChattingData(chatList.get(i).getMessage(), code);
                    list.add(chattingData);
                }

                if(list.size() != 0) {

                    ChattingDataAdapter adapter = new ChattingDataAdapter(ChattingActivity.this, list, oppName);
                    recyclerView.setAdapter(adapter);

                    recyclerView.scrollToPosition(list.size() - 1);
                }
            }

            @Override
            public void onFailed() {

            }
        });
    }
}