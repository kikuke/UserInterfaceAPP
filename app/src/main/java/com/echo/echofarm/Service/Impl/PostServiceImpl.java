package com.echo.echofarm.Service.Impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.SendPostDto;
import com.echo.echofarm.Data.Entity.Post;
import com.echo.echofarm.Service.PostService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostServiceImpl implements PostService {
    private static final String TAG = "PostService";
    private FirebaseFirestore db;

    public PostServiceImpl(){
        db = FirebaseFirestore.getInstance();
    }

    public void sendPostDto(SendPostDto sendPostDto){
        Post post = new Post();

        post.setUid(sendPostDto.getUid());
        post.setTitle(sendPostDto.getTitle());
        post.setImgSrc(sendPostDto.getImgSrc());//이미지 업로드 후 저장작업 필요함. 이미지 자체를 담고있기?
        post.setContents(sendPostDto.getContents());
        post.setOwnProduct(sendPostDto.getOwnProduct());
        post.setOwnTag(sendPostDto.getOwnTag());
        post.setWantProduct(sendPostDto.getWantProduct());
        post.setWantTag(sendPostDto.getWantTag());
        post.setAllowOther(sendPostDto.isAllowOther());

        db.collection("post").add(post)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    getPostDto(documentReference.getId());
                }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void getPostDto(String postId){
        DocumentReference docRef = db.collection("post").document(postId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GetPostDto getPostDto = new GetPostDto();
                Post post = documentSnapshot.toObject(Post.class);

                getPostDto.setUid(post.getUid());
                getPostDto.setTitle(post.getTitle());
                getPostDto.setImgSrc(post.getImgSrc());
                getPostDto.setContents(post.getContents());
                getPostDto.setOwnProduct(post.getOwnProduct());
                getPostDto.setOwnTag(post.getOwnTag());
                getPostDto.setWantProduct(post.getWantProduct());
                getPostDto.setWantTag(post.getWantTag());
                getPostDto.setAllowOther(post.isAllowOther());

                Log.w(TAG, "getPostDto" + getPostDto);
                //포스트액티비티로 이동하기
            }
        });
    }
}
