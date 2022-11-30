package com.echo.echofarm.Service.Impl;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.echo.echofarm.Activity.PostInfo;
import com.echo.echofarm.Data.Dto.GetPostDto;
import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Data.Dto.SendPostDto;
import com.echo.echofarm.Data.Entity.Post;
import com.echo.echofarm.Interface.GetImgUrlListener;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.Interface.StoreImgListener;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.StoreService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    private static final String TAG = "PostService";
    private FirebaseFirestore db;
    private StoreService storeService;

    public PostServiceImpl(){
        db = FirebaseFirestore.getInstance();
        storeService = new StoreServiceImpl();
    }

    @Override
    public void getPostList(GetPostListDto getPostListDto, String beforePostId, Integer limitSize, List<PostInfo> postInfoList, GetPostInfoListener getPostInfoListener){
        CollectionReference colRef = db.collection("post");

        if(beforePostId != null) {
            colRef.document(beforePostId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Query query = postQueryFactory(getPostListDto, limitSize);
                            query = query.startAfter(document);
                            Log.d(TAG, "getStartDocument" + document.getId());

                            getPostList(query, getPostListDto, postInfoList, getPostInfoListener);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            return;
        }
        Query query = postQueryFactory(getPostListDto, limitSize);

        getPostList(query, getPostListDto, postInfoList, getPostInfoListener);
    }

    private Query postQueryFactory(GetPostListDto getPostListDto, Integer limitSize){
        CollectionReference colRef = db.collection("post");

        Query query = colRef.orderBy("nowTime", Query.Direction.DESCENDING).limit(limitSize);
        if(getPostListDto.getUid() != null)
            query = query.whereEqualTo("uid", getPostListDto.getUid());
        if(getPostListDto.getOwnProduct() != null)
            query = query.whereEqualTo("ownProduct", getPostListDto.getOwnProduct());
        if(getPostListDto.getWantProduct() != null)
            query = query.whereEqualTo("wantProduct", getPostListDto.getWantProduct());

        return query;
    }

    private void getPostList(Query query, GetPostListDto getPostListDto, List<PostInfo> postInfoList, GetPostInfoListener getPostInfoListener){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().size() <= 0) {
                        //데이터 끝
                    } else {
                        for(DocumentSnapshot document : task.getResult()) {
                            Post post = document.toObject(Post.class);

                            if(getPostListDto.getTitle() != null){
                                if(!compareContainWord(post.getTitle(), getPostListDto.getTitle())){
                                    continue;
                                }
                            }
                            if(!compareContainTags(post.getOwnTag(), getPostListDto.getOwnTag()))
                                continue;
                            if(!compareContainTags(post.getWantTag(), getPostListDto.getWantTag()))
                                continue;

                            PostInfo postInfo = new PostInfo(document.getId(), post.getUid(), post.getTitle(), post.getOwnTag().toString());
                            postInfoList.add(postInfo);
                            storeService.getImageUrl(document.getId(), "0.png", postInfo, getPostInfoListener);
                            Log.d(TAG, "PostInfo: " + postInfo);
                        }
                    }
                }}});
    }

    private boolean compareContainTags(List<String> source, List<String> target){
        boolean isContain;
        if(target == null)
            return true;
        if(source == null)
            return false;

        for(String sTag: target){
            isContain = false;
            for(String pTag: source){
                if(compareContainWord(pTag, sTag)){
                    isContain=true;
                    break;
                }
            }
            if(!isContain)
                return false;
        }

        return true;
    }

    private boolean compareContainWord(String source, String target){
        return source.toLowerCase().contains(target.toLowerCase());
    }

    //중간 인터페이스 만들어서 서비스 연결시켜주기
    //uid는 외부에서 빼기. sendPostDto가 아닌 별개 인자로 받기
    public void sendPostDto(SendPostDto sendPostDto, StoreImgListener storeImgListener){
        Post post = new Post();

        post.setUid(sendPostDto.getUid());
        post.setTitle(sendPostDto.getTitle());
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

                    sendImgUri(documentReference.getId(), sendPostDto.getImgSrc(), storeImgListener);
                }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void sendImgUri(String docID, List<Uri> uriList, StoreImgListener storeImgListener){
        int i=0;
        for(Uri uri : uriList){
            System.out.println(uri+ docID+ Integer.toString(i));

            storeService.storeImage(uri, docID, Integer.toString(i), storeImgListener);
            i++;
        }
    }

    public void getPostDto(String postId, GetImgUrlListener getImgUrlListener){
        DocumentReference docRef = db.collection("post").document(postId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                GetPostDto getPostDto = new GetPostDto();
                Post post = documentSnapshot.toObject(Post.class);

                getPostDto.setUid(post.getUid());
                getPostDto.setTitle(post.getTitle());
                getPostDto.setContents(post.getContents());
                getPostDto.setOwnProduct(post.getOwnProduct());
                getPostDto.setOwnTag(post.getOwnTag());
                getPostDto.setWantProduct(post.getWantProduct());
                getPostDto.setWantTag(post.getWantTag());
                getPostDto.setAllowOther(post.isAllowOther());
                getPostDto.setNowTime(post.getNowTime());

                getPostDto.setImgSrc(new ArrayList<>());
                storeService.getAllImageUrl(postId, getPostDto, getImgUrlListener);

                Log.w(TAG, "Success getPostDto: " + getPostDto);
            }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Success getPostDto:", e);
                    }
                });;
    }
}
