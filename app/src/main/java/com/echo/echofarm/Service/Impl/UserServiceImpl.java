package com.echo.echofarm.Service.Impl;

import com.echo.echofarm.Service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserServiceImpl implements UserService {

    private FirebaseAuth mAuth;

    public UserServiceImpl(){
        mAuth = FirebaseAuth.getInstance();
    }

    public String getUserUid(){
        return mAuth.getUid();
    }
}
