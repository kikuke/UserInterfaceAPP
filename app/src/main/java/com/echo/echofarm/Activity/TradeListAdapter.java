package com.echo.echofarm.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.echo.echofarm.R;

import org.w3c.dom.Text;

import java.util.List;

public class TradeListAdapter extends BaseAdapter {
    // 보여줄 Item 목록을 저장할 List
    List<TradeListInfo> items = null;
    Context context;

    // Adapter 생성자 함수
    public TradeListAdapter(Context context, List<TradeListInfo> items) {
        this.items = items;
        this.context = context;
    }

    // Adapter.getCount(), 아이템 개수 반환 함수
    @Override
    public int getCount() {
        return items.size();
    }

    // Adapter.getItem(int position), 해당 위치 아이템 반환 함수
    @Override
    public TradeListInfo getItem(int position) {
        return items.get(position);
    }

    // Adapter.getItemId(int position), 해당 위치 반환 함수
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Adapter.getView() 해당위치 뷰 반환 함수
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Infalter 구현 방법 1
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.trade_list_layout, parent, false);

        // ListView의 Item을 구성하는 뷰 연결
        TextView title = view.findViewById(R.id.pTitle);
        ImageView image = view.findViewById(R.id.picture);
        TextView tag = view.findViewById(R.id.pTag);

        // ListView의 Item을 구성하는 뷰 세팅
        TradeListInfo item = items.get(position);
        tag.setText(item.getTradeTag());		// 해당위치 +1 설정, 배열순으로 0부터 시작
        title.setText(item.getPostTitle());					// item 객체 내용을 가져와 세팅
        Glide.with(context).load(item.getPoster()).into(image);

        // 설정한 view를 반환해줘야 함
        return view;
    }


}