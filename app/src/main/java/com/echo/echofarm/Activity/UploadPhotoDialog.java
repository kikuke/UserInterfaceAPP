package com.echo.echofarm.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.echo.echofarm.Interface.UploadPhotoClickListener;
import com.echo.echofarm.R;

public class UploadPhotoDialog extends AlertDialog {
    private TextView takePicture;
    private TextView openGallery;
    private UploadPhotoClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo_dialog);
        takePicture = findViewById(R.id.takeAPictureTextViewId);
        openGallery = findViewById(R.id.galleryTextViewId);

        takePicture.setOnClickListener(view -> {
            this.listener.onTakePhotoClick();
            dismiss();
        });
        openGallery.setOnClickListener(view -> {
            this.listener.onOpenGalleryClick();
            dismiss();
        });
    }

    public UploadPhotoDialog(@NonNull Context context,
                             UploadPhotoClickListener uploadPhotoClickListener) {
        super(context);
        this.listener = uploadPhotoClickListener;
    }
}
