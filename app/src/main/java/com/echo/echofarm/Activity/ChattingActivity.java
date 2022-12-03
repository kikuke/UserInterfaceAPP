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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.echo.echofarm.Data.Dto.GetChatDto;
import com.echo.echofarm.Data.Dto.GetChatResultDto;
import com.echo.echofarm.Data.Dto.SendChatDto;
import com.echo.echofarm.Interface.GetChatDtoListener;
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


    private List<GetChatDto> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>홍석희</font>"));


        recyclerView = findViewById(R.id.chatting_recyclerView);
        sendingMessage = findViewById(R.id.sending_message_editText);
        sendMessageButton = findViewById(R.id.send_message_button);

        ChatService chatService = new ChatServiceImpl();
        UserService userService = new UserServiceImpl();

        Intent intent = getIntent();
        String oppId = intent.getStringExtra("userId");

        list = new ArrayList<>();
        chatList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        chatService.getChatList(userService.getUserUid(), oppId, null, new GetChatDtoListener() {
            @Override
            public void onSuccess(GetChatResultDto getChatDtoResult) {
                chatList = getChatDtoResult.getGetChatDtoList();
            }

            @Override
            public void onFailed() {

            }
        });

        for(int i = 0; i < chatList.size(); i++) {
            int code = 0;

            if(chatList.get(i).getUid().equals(userService.getUserUid())) {
                code = 1;
            }
            ChattingData chattingData = new ChattingData(chatList.get(0).getMessage(), code);
            list.add(chattingData);
        }

         if(list.size() != 0) {
             ChattingDataAdapter adapter = new ChattingDataAdapter(this, list, "홍석희");
             recyclerView.setAdapter(adapter);
        }

        // 전송 버튼 클릭
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("my", "onClick");
                try {
                    if (!sendingMessage.getText().toString().equals("")) {
                        String message = sendingMessage.getText().toString();
                        list.add(new ChattingData(message, 1));

                        // 처음 채팅이라면 adapter 생성
                        if(list.size() == 1) {
                            Log.i("my", "list size is 1");
                            adapter = new ChattingDataAdapter(ChattingActivity.this, list, "홍석희");
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyItemRangeChanged(list.size() - 1, 1);
                        }

                        recyclerView.scrollToPosition(list.size() - 1);
                        sendingMessage.setText("");

                        // send message info to server
                        chatService.sendChat(userService.getUserUid(), oppId, new SendChatDto(message));
                   }
                } catch (Exception e) {
                    Toast.makeText(ChattingActivity.this, "메세지만 입력 가능합니다.", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}