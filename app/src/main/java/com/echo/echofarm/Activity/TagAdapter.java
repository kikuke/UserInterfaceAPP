package com.echo.echofarm.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    Context context;
    ArrayList<ArrayList<String>> tagList;
    int requestCode;

    public TagAdapter(Context context, ArrayList<ArrayList<String>> tagList, int requestCode) {
        this.context = context;
        this.tagList = tagList;
        this.requestCode = requestCode;
    }

    @NonNull
    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.user_tag_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, int position) {
        int size = tagList.get(position).size();
        class ImpClickListener implements View.OnClickListener{

            @Override
            public void onClick(View view) {
                if(view == holder.delBtn1) {
                    Log.i("my", "remove : " + tagList.get(holder.getAdapterPosition()).get(0), null);
                    tagList.get(holder.getAdapterPosition()).remove(0);
                } else if(view == holder.delBtn2) {
                    Log.i("my", "remove : " + tagList.get(holder.getAdapterPosition()).get(1), null);
                    tagList.get(holder.getAdapterPosition()).remove(1);
                } else if(view == holder.delBtn3) {
                    Log.i("my", "remove : " + tagList.get(holder.getAdapterPosition()).get(2), null);
                    tagList.get(holder.getAdapterPosition()).remove(2);
                }
                context.startActivity(new Intent(context, SettingTagActivity.class).putExtra("S", tagList)
                        .putExtra("dummy", "dummy")
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        }

        ImpClickListener clickListener = new ImpClickListener();

            switch (size) {
                case 0: {
                    Log.i("my", "size is 0", null);
                }
                case 1: {
                    holder.lay1.setVisibility(View.VISIBLE);
                    holder.tag1.setText(tagList.get(holder.getAdapterPosition()).get(0));
                    if(requestCode == 0) {
                        holder.delBtn1.setVisibility(View.GONE);
                    } else {
                        holder.delBtn1.setOnClickListener(clickListener);
                    }
                    break;
                }

                case 2: {
                    holder.lay1.setVisibility(View.VISIBLE);
                    holder.tag1.setText(tagList.get(holder.getAdapterPosition()).get(0));
                    holder.lay2.setVisibility(View.VISIBLE);
                    holder.tag2.setText(tagList.get(holder.getAdapterPosition()).get(1));

                    if(requestCode == 0) {
                        holder.delBtn1.setVisibility(View.GONE);
                        holder.delBtn2.setVisibility(View.GONE);
                    } else {
                        holder.delBtn1.setOnClickListener(clickListener);
                        holder.delBtn2.setOnClickListener(clickListener);
                    }
                    break;
                }

                case 3: {
                    holder.lay1.setVisibility(View.VISIBLE);
                    holder.tag1.setText(tagList.get(holder.getAdapterPosition()).get(0));
                    holder.lay2.setVisibility(View.VISIBLE);
                    holder.tag2.setText(tagList.get(holder.getAdapterPosition()).get(1));
                    holder.lay3.setVisibility(View.VISIBLE);
                    holder.tag3.setText(tagList.get(holder.getAdapterPosition()).get(2));

                    if(requestCode == 0) {
                        holder.delBtn1.setVisibility(View.GONE);
                        holder.delBtn2.setVisibility(View.GONE);
                        holder.delBtn3.setVisibility(View.GONE);
                    } else {
                        holder.delBtn1.setOnClickListener(clickListener);
                        holder.delBtn2.setOnClickListener(clickListener);
                        holder.delBtn3.setOnClickListener(clickListener);
                    }
                    break;
                }
            }
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lay1, lay2, lay3;
        TextView tag1, tag2, tag3;
        ImageButton delBtn1, delBtn2, delBtn3;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lay1 = itemView.findViewById(R.id.tag_layout_1);
            lay2 = itemView.findViewById(R.id.tag_layout_2);
            lay3 = itemView.findViewById(R.id.tag_layout_3);

            tag1 = itemView.findViewById(R.id.user_tag_1);
            tag2 = itemView.findViewById(R.id.user_tag_2);
            tag3 = itemView.findViewById(R.id.user_tag_3);

            delBtn1 = itemView.findViewById(R.id.delete_tag_button_1);
            delBtn2 = itemView.findViewById(R.id.delete_tag_button_2);
            delBtn3 = itemView.findViewById(R.id.delete_tag_button_3);
        }
    }
}
