package com.echo.echofarm.Service.Impl;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetUserInfoDto;
import com.echo.echofarm.Data.Dto.SendUserDto;
import com.echo.echofarm.Data.Entity.Post;
import com.echo.echofarm.Data.Entity.User;
import com.echo.echofarm.Interface.GetChatDtoListener;
import com.echo.echofarm.Interface.GetUserInfoDtoListener;
import com.echo.echofarm.Service.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserServiceImpl implements UserService {

    private static final String TAG = "UserService";
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
    public void detectUserInfo(String uid, GetUserInfoDtoListener getUserInfoDtoListener){
        DocumentReference docRef = db.collection("user").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    getUserInfoDto(uid, getUserInfoDtoListener);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public void addUserTag(GetUserInfoDto targetUserInfo, String tag){
        if(targetUserInfo.getTags().contains(tag))
            return;
        targetUserInfo.getTags().add(tag);

        sendUserDto(sendUserFactory(targetUserInfo));
    }

    @Override
    public void deleteUserTag(GetUserInfoDto targetUserInfo, String tag){
        if(!targetUserInfo.getTags().contains(tag))
            return;
        int idx = targetUserInfo.getTags().indexOf(tag);
        targetUserInfo.getTags().remove(idx);

        sendUserDto(sendUserFactory(targetUserInfo));
    }

    @Override
    public void addUserLike(String sourceUid, GetUserInfoDto targetUserInfo){
        if(targetUserInfo.getLikedUser().contains(sourceUid))
            return;
        targetUserInfo.getLikedUser().add(sourceUid);

        sendUserDto(sendUserFactory(targetUserInfo));
    }

    @Override
    public void deleteUserLike(String sourceUid, GetUserInfoDto targetUserInfo){
        if(!targetUserInfo.getLikedUser().contains(sourceUid))
            return;
        int idx = targetUserInfo.getLikedUser().indexOf(sourceUid);
        targetUserInfo.getLikedUser().remove(idx);

        sendUserDto(sendUserFactory(targetUserInfo));
    }

    private SendUserDto sendUserFactory(GetUserInfoDto in){
        return new SendUserDto(in.getUid(), in.getName(), in.getTags(), in.getLikedUser());
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
                        Log.w(TAG, "Success getUserInfoDto:" + getUserInfoDto);
                        getUserInfoDtoListener.onSuccess(getUserInfoDto);

                        Log.w(TAG, "Success getUserInfoDto: " + getUserInfoDto);
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed getUserInfoDto:", e);
                    }
                });
    }

    private GetUserInfoDto getUserInfoDtoFactory(User user){
        GetUserInfoDto getUserInfoDto = new GetUserInfoDto();

        getUserInfoDto.setUid(user.getUid());
        getUserInfoDto.setName(user.getName());
        getUserInfoDto.setTags(user.getTags());
        getUserInfoDto.setLikedUser(user.getLikedUser());

        return getUserInfoDto;
    }

    @Override
    public void sendUserDto(SendUserDto sendUserDto){
        User user = userFactory(sendUserDto);

        //안되면 리스너 달기
        db.collection("user").document(user.getUid()).set(user);
    }

    private User userFactory(SendUserDto sendUserDto) {
        User user = new User();
        user.setUid(sendUserDto.getUid());
        user.setName(sendUserDto.getName());
        user.setTags(sendUserDto.getTags());
        user.setLikedUser(sendUserDto.getLikedUser());

        return user;
    }
}
