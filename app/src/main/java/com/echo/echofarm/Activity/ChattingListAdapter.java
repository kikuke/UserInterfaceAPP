package com.echo.echofarm.Activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class ChattingListAdapter extends RecyclerView.Adapter<ChattingListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> opponentNameList;
    private ArrayList<String> recentMessageList;

    public ChattingListAdapter(Context context, ArrayList<String> opponentNameList, ArrayList<String> recentMessageList) {
        this.context = context;
        this.opponentNameList = opponentNameList;
        this.recentMessageList = recentMessageList;
    }

    @NonNull
    @Override
    public ChattingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.chatting_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingListAdapter.ViewHolder holder, int position) {
        holder.opponentName.setText(opponentNameList.get(position));
        holder.recentMessage.setText(recentMessageList.get(position));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("my", "" + holder.getAdapterPosition(), null);
                Intent intent = new Intent();
                intent.setClass(context, ChattingActivity.class);
                //intent.putExtra("oppId", )
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return opponentNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView opponentName, recentMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.chatting_list_Linearlayout);
            opponentName = itemView.findViewById(R.id.opponent_name_textView);
            recentMessage = itemView.findViewById(R.id.recent_message_textView);
        }
    }
}
