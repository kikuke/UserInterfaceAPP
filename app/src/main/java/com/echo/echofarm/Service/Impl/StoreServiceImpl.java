package com.echo.echofarm.Service.Impl;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.echo.echofarm.Service.StoreService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class StoreServiceImpl implements StoreService {
    private static final String TAG = "StoreService";
    FirebaseStorage storage;

    public StoreServiceImpl(){ storage = FirebaseStorage.getInstance(); }

    public void storeImage(Uri imageUri, String forderName, String photoName){
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("photo/" + forderName + "/" + photoName + ".png");
        UploadTask uploadTask = imageRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Failed Store Image", exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.w(TAG, "Success Store Image: " + imageUri);
            }
        });
    }

    public void getAllImageUrl(String forderName, List<Uri> uriList){
        StorageReference storageRef = storage.getReference();
        storageRef.child("photo/" + forderName).listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            getImageUrl(forderName, item.getName(), uriList);
                        }
                        Log.w(TAG, "Success get All Image: " + uriList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed get All Image: ", e);
                    }
                });
    }

    public void getImageUrl(String forderName, String photoName, List<Uri> uriList){
        StorageReference storageRef = storage.getReference();

        storageRef.child("photo/" + forderName + "/" + photoName + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uriList.add(uri);
                Log.w(TAG, "Success get Image: " + uri + "List: " + uriList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w(TAG, "Failed get Image: " + exception);
            }
        });
    }
}
