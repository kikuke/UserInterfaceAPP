package com.echo.echofarm.Activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class PhotoViewPageAdapter extends RecyclerView.Adapter<PhotoViewPageAdapter.ViewHolderPage> {

    private ArrayList<UploadedPhotoData> listData;

    PhotoViewPageAdapter(Context context, ArrayList<UploadedPhotoData> list) {
        this.listData = list;
    }

    @Override
    public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.uploaded_photo_layout, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderPage holder, int position) {
        if(holder instanceof ViewHolderPage){
            ViewHolderPage viewHolder = (ViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolderPage extends RecyclerView.ViewHolder {

        private ImageView image;
        private Button deleteBtn;

        UploadedPhotoData data;

        ViewHolderPage(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.uploaded_imageView);
            deleteBtn = itemView.findViewById(R.id.delete_photo_btn);
        }

        public void onBind(UploadedPhotoData data){
            this.data = data;
            image.setImageURI(data.getPhotoUri());
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }
    }
}
