package com.echo.echofarm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.UserService;

import java.util.ArrayList;
import java.util.Arrays;

public class TagSettingActivity extends AppCompatActivity {


    private ArrayList<ArrayList<String>> list;
    private RecyclerView recyclerView;
    private Button addTagButton;
    private EditText inputTag;
    private UserService userService;
    private GetUserInfoDto userInfoDto;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_tag);

        inputTag = findViewById(R.id.input_tag_edittext);
        RecyclerView recyclerView = findViewById(R.id.setting_tag_recyclerview);
        addTagButton = findViewById(R.id.add_tag_button);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>태그 설정</font>"));


        userService = new UserServiceImpl();
        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");
        userService.getUserInfoDto(userId, new GetUserInfoDtoListener() {
            @Override
            public void onSuccess(GetUserInfoDto getUserInfoDto) {
                userInfoDto = getUserInfoDto;

                list = new ArrayList<>();

                ArrayList<String> s = (ArrayList<String>) userInfoDto.getTags();

                separateString(s, list);

                TagAdapter tagAdapter = new TagAdapter(TagSettingActivity.this, list, userId, 1);
                recyclerView.setAdapter(tagAdapter);
            }

            @Override
            public void onFailed() {

            }
        });

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = inputTag.getText().toString();
                if(input.length() > 20) {
                    Toast.makeText(TagSettingActivity.this, "최대 20글자로 입력 해주세요", Toast.LENGTH_SHORT).show();
                    inputTag.setText("");
                } else if(input.length() == 1){
                    Toast.makeText(TagSettingActivity.this, "두 글자 이상 입력 해주세요", Toast.LENGTH_SHORT).show();
                } else if(input.equals("")) {
                    // do nothing
                } else {
                    addTag(inputTag.getText().toString());
                }
            }
        });


        inputTag.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    String input = inputTag.getText().toString();
                    if(input.length() > 20) {
                        Toast.makeText(TagSettingActivity.this, "최대 20글자로 입력 해주세요", Toast.LENGTH_SHORT).show();
                        inputTag.setText("");
                    } else if(input.length() == 1){
                        Toast.makeText(TagSettingActivity.this, "두 글자 이상 입력 해주세요", Toast.LENGTH_SHORT).show();
                    } else if(input.equals("")) {
                        // do nothing
                    }
                    else {
                        addTag(inputTag.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        list = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("S");
        ArrayList<String> slist = new ArrayList<>();
        for(int i = 0; i < list.size(); i++)
            for(int j = 0; j < list.get(i).size(); j++)
                slist.add(list.get(i).get(j));
        list = new ArrayList<>();
        separateString(slist, list);

        TagAdapter tagAdapter = new TagAdapter(this, list, userId,1);
        recyclerView = findViewById(R.id.setting_tag_recyclerview);
        recyclerView.setAdapter(tagAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UserProfileActivity.class).putExtra("tagList", list);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public static void separateString(ArrayList<String> arrayList, ArrayList<ArrayList<String>> result) {

        int checkLength = 0;

        ArrayList<String> sepList = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++) {

            String s = arrayList.get(i);
            checkLength += s.length();
            Log.i("my", "check : " + checkLength, null);

            if(s.length() >= 20) {
                result.add(new ArrayList<>(sepList));
                sepList = new ArrayList<>();
                sepList.add(s);
                result.add(new ArrayList<>(sepList));
                sepList = new ArrayList<>();
                checkLength = 0;
            } else if(checkLength < 20) {
                sepList.add(s);
                if(sepList.size() == 3) {
                    result.add(new ArrayList<>(sepList));
                    sepList = new ArrayList<>();
                    checkLength = 0;
                }
            } else {
                result.add(new ArrayList<>(sepList));
                sepList = new ArrayList<>();
                checkLength = 0;
                i--;
            }
        }

        if(checkLength != 0) result.add(new ArrayList<>(sepList));
    }

    private void addTag(String addedTag) {
        userService.addUserTag(userInfoDto, addedTag);
        inputTag.setText("");

        ArrayList<String> slist = new ArrayList<>();
        for(int i = 0; i < list.size(); i++)
            for(int j = 0; j < list.get(i).size(); j++)
                slist.add(list.get(i).get(j));

        slist.add(addedTag);

        list = new ArrayList<>();
        separateString(slist, list);

        TagAdapter tagAdapter = new TagAdapter(this, list, userId,1);
        recyclerView = findViewById(R.id.setting_tag_recyclerview);
        recyclerView.setAdapter(tagAdapter);
    }
}