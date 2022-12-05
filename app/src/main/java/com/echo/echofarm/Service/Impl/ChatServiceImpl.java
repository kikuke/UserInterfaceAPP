package com.echo.echofarm.Service.Impl;

import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.echo.echofarm.Data.Dto.GetChatDto;
import com.echo.echofarm.Data.Dto.GetChatResultDto;
import com.echo.echofarm.Data.Dto.SendChatDto;
import com.echo.echofarm.Data.Entity.Chat;
import com.echo.echofarm.Interface.GetChatDtoListener;
import com.echo.echofarm.Service.ChatService;
import com.echo.echofarm.Service.FcmService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatServiceImpl implements ChatService {
    private static final String TAG = "ChatService";
    private FirebaseFirestore db;
    private FcmService fcmService;
    //나중에 유저서비스 만들어서 이미지랑 아이디 넣기?

    public ChatServiceImpl(){
        db = FirebaseFirestore.getInstance();
        fcmService = new FcmService();
    }

    @Override
    public void detectChat(String uid1, String uid2, String beforeChatId, GetChatDtoListener getChatDtoListener){
        CollectionReference colRef = db.collection("chat").document(findChatRoomName(uid1,uid2)).collection("log");
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }

                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d(TAG, "Added: " + dc.getDocument().getData());
                            getChatList(uid1, uid2, beforeChatId, getChatDtoListener);
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed: " + dc.getDocument().getData());
                            break;
                    }
                }

            }
        });
    }

    //유저기능 개발되면 커버함수 만들기(자동으로 파라미터 입력)
    public void sendChat(String sender, String receiver, SendChatDto sendChatDto){
        Chat chat = new Chat(sender, sendChatDto.getMessage());

        db.collection("chat").document(findChatRoomName(sender, receiver)).collection("log").add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    //유저기능 개발되면 커버함수 만들기(자동으로 파라미터 입력)
    public void getChatList(String uid1, String uid2, String beforeChatId, GetChatDtoListener getChatDtoListener){
        CollectionReference colRef = db.collection("chat").document(findChatRoomName(uid1,uid2)).collection("log");
        if(beforeChatId!=null) {
            colRef.document(beforeChatId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Query query = colRef.orderBy("nowTime", Query.Direction.ASCENDING);
                            query = query.startAfter(document);
                            Log.d(TAG, "getStartDocument" + document.getId());

                            getChatList(query, getChatDtoListener);
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
        Query query = colRef.orderBy("nowTime", Query.Direction.ASCENDING);

        getChatList(query, getChatDtoListener);
    }

    private void getChatList(Query query, GetChatDtoListener getChatDtoListener){

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().size() <= 0) {
                    } else {
                        GetChatResultDto getChatResultDto = new GetChatResultDto();

                        for(DocumentSnapshot document : task.getResult()) {
                            Chat chat = document.toObject(Chat.class);
                            GetChatDto getChatDto = new GetChatDto(document.getId(), chat.getUid(), chat.getMessage(), chat.getNowTime(), chat.isRead());
                            //읽었을 경우 읽은 데이터 전송하는 기능 추가하기. 나중에 시간 되면.

                            getChatResultDto.getGetChatDtoList().add(getChatDto);
                        }
                        getChatDtoListener.onSuccess(getChatResultDto);

                        Log.d(TAG, "ChatResult: " + getChatResultDto);
                    }
                }
            }});
    }

    private String findChatRoomName(String uid1, String uid2){
        if(uid1.compareTo(uid2)>0){
            String temp = uid1; uid1 = uid2; uid2 = temp;
        }

        return uid1 + "&" + uid2;
    }
}
