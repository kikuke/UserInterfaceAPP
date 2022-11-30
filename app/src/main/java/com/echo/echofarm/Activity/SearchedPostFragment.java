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

    Context context;
    String tag;
    ArrayList<PostInfo> postInfoArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

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

        PostService  postService = new PostServiceImpl();
        GetPostListDto getPostListDto = new GetPostListDto(null, null, null, Arrays.asList(tag), null, null, null);

        postInfoArrayList = new ArrayList<>();

        Log.i("my", "onCreateView", null);
        postService.getPostList(getPostListDto, null, 3, postInfoArrayList, new GetPostInfoListener() {
            @Override
            public void onSuccess(PostInfo postInfo) {
                Log.i("my", "onSuccess", null);
                PostAdapter adapter = new PostAdapter(context ,postInfoArrayList);
                Log.i("my", "size : " + postInfoArrayList.size(), null);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailed() {

            }
        });

        /*
        recyclerView.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager layoutManager =
                        LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                // 스크롤의 끝
                if (lastVisible >= totalItemCount - 1) {
                    String beforeId = postInfoArrayList.get(postInfoArrayList.size() - 1).getPostId();
                    postInfoArrayList = new ArrayList<>();

                    postService.getPostList(getPostListDto, beforeId, 2, postInfoArrayList, new GetPostInfoListener() {
                        @Override
                        public void onSuccess(PostInfo postInfo) {
                            PostAdapter adapter = new PostAdapter(context ,postInfoArrayList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onFailed() {

                        }
                    });
                }
            }
        });

         */
    }
}
