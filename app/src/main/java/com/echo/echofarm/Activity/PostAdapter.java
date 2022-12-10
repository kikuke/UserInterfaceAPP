package com.echo.echofarm.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.echo.echofarm.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostInfo> postInfoArrayList;

    public PostAdapter(Context context, ArrayList<PostInfo> postInfoArrayList) {
        this.context = context;
        this.postInfoArrayList = postInfoArrayList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.post_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        PostInfo postInfo = postInfoArrayList.get(position);
        holder.title.setText(postInfo.getTitle());
        holder.tags.setText(postInfo.getTags());
        if(postInfo.isComplete())
            holder.exchanged.setVisibility(View.VISIBLE);
        Glide.with(context).load(postInfo.getImageUri()).into(holder.postImage);
    }

    @Override
    public int getItemCount() {
        return postInfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, tags, exchanged;
        ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.imageView_postLayout);
            title = itemView.findViewById(R.id.title_postLayout);
            tags = itemView.findViewById(R.id.tags_postLayout);
            exchanged = itemView.findViewById(R.id.exchanged_info_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ViewPostActivity.class);
                    intent.putExtra("postId", postInfoArrayList.get(position).getPostId());
                    intent.putExtra("userId", postInfoArrayList.get(position).getId());
                    intent.putExtra("postTitle", postInfoArrayList.get(position).getTitle());
                    context.startActivity(intent);
                }
            });
        }



    }
}

