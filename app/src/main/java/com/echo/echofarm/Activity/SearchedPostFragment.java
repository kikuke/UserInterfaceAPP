package com.echo.echofarm.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Data.Entity.Post;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.Impl.UserServiceImpl;
import com.echo.echofarm.Service.PostService;
import com.echo.echofarm.Service.UserService;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchedPostFragment extends Fragment {

    private Context context;
    private String tag;
    private ArrayList<PostInfo> postInfoArrayList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PostService postService;
    private GetPostListDto getPostListDto;
    private PostAdapter adapter;

    public SearchedPostFragment(Context context, String tag) {
        super();
        this.context = context;
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.post_fragment_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        postService = new PostServiceImpl();
        getPostListDto = new GetPostListDto(null, null, null, Arrays.asList(tag), null, null, null);

        postInfoArrayList = new ArrayList<>();

        getTagData(null);

        if(postInfoArrayList.size() != 0 && postInfoArrayList.size() < 5) {
            Log.i("my", "size is " + postInfoArrayList.size());
            getTagData(postInfoArrayList.get(postInfoArrayList.size()-1).getPostId());
        }

        Log.i("my", "onCreateView tag : " + tag, null);
    }

    private int countIter;
    private boolean isFirstTime = true;

    private void getTagData(String beforeId) {

        int beforeSize = postInfoArrayList.size();
        countIter = beforeSize;

        postService.getPostList(getPostListDto, beforeId, 100, postInfoArrayList, new GetPostInfoListener() {
            @Override
            public void onSuccess(PostInfo postInfo) {
                if(beforeId == null) {
                    Log.i("my", "onSuccess", null);
                    adapter = new PostAdapter(context, postInfoArrayList);
                    Log.i("my", "size : " + postInfoArrayList.size(), null);
                    recyclerView.setAdapter(adapter);
                    countIter++;
                    Log.i("my", "countIter : " + countIter, null);
                } else {
                    adapter.notifyItemRangeChanged(beforeSize, postInfoArrayList.size() - beforeSize);
                    countIter++;
                    Log.i("my", "not null post size : " + postInfoArrayList.size(), null);
                }


                if(countIter == postInfoArrayList.size()  && postInfoArrayList.size() < 5) {
                    Log.i("my", "Iter called", null);
                    getTagData(postInfoArrayList.get(postInfoArrayList.size()-1).getPostId());
                }

                if(postInfoArrayList.size() >= 5) {
                    setListener();
                }
            }

            @Override
            public void onFailed() {

            }
        });
    }

    private void setListener() {
        if(isFirstTime) {
            recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    LinearLayoutManager layoutManager =
                            LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                    // 스크롤의 끝
                    if ((lastVisible == totalItemCount - 1)) {
                        Log.i("my", "scroll called, lastVisivle : " + lastVisible + ", total Item count -1 : " + totalItemCount, null);
                        String beforeId = postInfoArrayList.get(postInfoArrayList.size() - 1).getPostId();
                        getTagData(beforeId);
                    }
                }
            });
        }
        isFirstTime = false;
    }
}
