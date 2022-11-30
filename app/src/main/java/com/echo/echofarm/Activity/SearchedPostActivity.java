package com.echo.echofarm.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.echo.echofarm.R;

public class SearchedPostActivity extends AppCompatActivity {

    EditText searchedText;
    ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_post);


        // 액션바 제목
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000'>검색</font>"));

        searchedText = findViewById(R.id.search_bar_edittext);
        searchBtn = findViewById(R.id.search_bar_button);

        searchedText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE && !searchedText.getText().equals(""))
                {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    SearchedPostFragment fragment = new SearchedPostFragment(SearchedPostActivity.this, searchedText.getText().toString());
                    ft.replace(R.id.post_container, fragment);
                    ft.commit();
                    return true;
                }
                return false;
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("my", "called", null);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                SearchedPostFragment fragment = new SearchedPostFragment(SearchedPostActivity.this, searchedText.getText().toString());
                ft.replace(R.id.post_container, fragment);
                ft.commit();
            }
        });
    }
}