package com.echo.echofarm.Service.Impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Data.Entity.Post;
import com.echo.echofarm.Data.Entity.User;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.Service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class UserServiceImpl implements UserService {

    private static final String TAG = "PostService";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public UserServiceImpl(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public String getUserUid(){
        return mAuth.getUid();
    }

    @Override
    public void getUserInfoDto(String uid, GetUserInfoDtoListener getUserInfoDtoListener){
        CollectionReference colRef = db.collection("user");
        //이거 잘 안되면 리스너 달아서 하기
        colRef.document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        GetUserInfoDto getUserInfoDto = new GetUserInfoDto();
                        User user = documentSnapshot.toObject(User.class);
                        getUserInfoDto = getUserInfoDtoFactory(user);
                        getUserInfoDtoListener.onSuccess(getUserInfoDto);

                        Log.w(TAG, "Success getUserInfoDto: " + getUserInfoDto);
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Success getUserInfoDto:", e);
                    }
                });
    }

    private GetUserInfoDto getUserInfoDtoFactory(User user){
        GetUserInfoDto getUserInfoDto = new GetUserInfoDto();

        getUserInfoDto.setUid(user.getUid());
        getUserInfoDto.setName(user.getName());
        getUserInfoDto.setLike(user.getLike());
        getUserInfoDto.setTags(user.getTags());
        getUserInfoDto.setLikeUser(user.getLikeUser());
        getUserInfoDto.setLikedUser(user.getLikedUser());

        return getUserInfoDto;
    }

    public void sendUserDto(SendUserDto sendUserDto){
        User user = userFactory(sendUserDto);

        //안되면 리스너 달기
        db.collection("user").document(user.getUid()).set(user);
    }

    private User userFactory(SendUserDto sendUserDto) {
        User user = new User();
        user.setUid(sendUserDto.getUid());
        user.setName(sendUserDto.getName());
        user.setLike(sendUserDto.getLike());
        user.setTags(sendUserDto.getTags());
        user.setLikeUser(sendUserDto.getLikeUser());
        user.setLikedUser(sendUserDto.getLikedUser());

        return user;
    }
}
