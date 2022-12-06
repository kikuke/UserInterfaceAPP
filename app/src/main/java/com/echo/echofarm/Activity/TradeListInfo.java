package com.echo.echofarm.Activity;

import android.net.Uri;

public class TradeListInfo {
    private Uri poster;
    private String title;
    private String tradeTag;

    public TradeListInfo(Uri poster, String title, String tradeTag){
        this.poster=poster;
        this.title = title;
        this.tradeTag = tradeTag;
    }
    public Uri getPoster(){
        return poster;
    }
    public String getPostTitle(){
        return title;
    }
    public String getTradeTag(){
        return tradeTag;
    }
}
