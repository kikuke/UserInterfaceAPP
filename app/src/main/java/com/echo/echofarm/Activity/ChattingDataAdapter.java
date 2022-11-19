package com.echo.echofarm.Activity;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.R;

import java.util.ArrayList;

public class ChattingDataAdapter extends RecyclerView.Adapter<ChattingDataAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ChattingData> chattingDataArrayList;
    private String opponentName;

    public ChattingDataAdapter(Context context, ArrayList<ChattingData> chattingDataArrayList, String opponentName) {
        this.context = context;
        this.chattingDataArrayList = chattingDataArrayList;
        this.opponentName = opponentName;

        Log.i("my", "size : " + chattingDataArrayList.size(), null);
    }

    @NonNull
    @Override
    public ChattingDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("my","oncreate", null);
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.chatting_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingDataAdapter.ViewHolder holder, int position) {
        ChattingData chattingData = chattingDataArrayList.get(position);
        Log.i("my", chattingData.getMessage(), null);
        Log.i("my", "code : " + chattingData.getChatCode(), null);
        // opponent
        if(chattingData.getChatCode() == 0) {
            Log.i("my", "opponent called", null);
            holder.userChat.setVisibility(View.GONE);

            holder.opponentLayout.setVisibility(View.VISIBLE);
            holder.opponentName.setVisibility(View.VISIBLE);
            holder.opponentChat.setVisibility(View.VISIBLE);

            holder.opponentName.setText(opponentName);
            holder.opponentChat.setText(chattingData.getMessage());
        }
        // user
        else if(chattingData.getChatCode() == 1) {
            Log.i("my", "user called", null);
            holder.opponentLayout.setVisibility(View.GONE);
            holder.userChat.setVisibility(View.VISIBLE);
            holder.userChat.setText(chattingData.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chattingDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout opponentLayout;
        TextView opponentName, opponentChat, userChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i("my", "viewHolder", null);
            opponentLayout = itemView.findViewById(R.id.opponent_layout);
            opponentName = itemView.findViewById(R.id.opponent_name_textView);
            opponentChat = itemView.findViewById(R.id.opponent_chat_textView);

            userChat = itemView.findViewById(R.id.user_chat_textView);
        }
    }
}
