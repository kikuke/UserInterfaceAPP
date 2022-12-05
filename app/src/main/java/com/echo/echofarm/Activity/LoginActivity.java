package com.echo.echofarm.Activity;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.echo.echofarm.Data.Dto.GetChatResultDto;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendChatDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Interface.GetChatDtoListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.ChatService;
import com.echo.echofarm.Service.Impl.ChatServiceImpl;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private UserService userService;

    EditText editID;
    EditText editPW;
    Button loginBtn;
    Button joinBtn;
    TextView loginErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userService = new UserServiceImpl();

        setContentView(R.layout.activity_login);

        editID = (EditText) findViewById(R.id.idEditText);
        editPW = (EditText) findViewById(R.id.passwordEditText);
        loginErrorText = findViewById(R.id.loginErrorText);

        loginBtn = (Button) findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(this);
        joinBtn = (Button) findViewById(R.id.signupButton);
        joinBtn.setOnClickListener(this);

        editPW.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    signIn(editID.getText().toString(), editPW.getText().toString());
                    return true;
                }
                return false;
            }
        });

        // 액션바
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == loginBtn) {
            signIn(editID.getText().toString(), editPW.getText().toString());
        }
        else if(view == joinBtn) {
            Intent joinIntent = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(joinIntent);
        }
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication success",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            //테스트 용으로 포스트만드는 인텐트로 이동.
                            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainActivity);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            loginErrorText.setVisibility(View.VISIBLE);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {//로비로 이동하게끔

    }

    //https://github.com/firebase/snippets-android/blob/4dac0d7756bd9f2af56b8fedc228e7f365f42d88/auth/app/src/main/java/com/google/firebase/quickstart/auth/FirebaseUIActivity.java#L49-L62
}