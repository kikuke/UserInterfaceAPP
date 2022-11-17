package com.echo.echofarm.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.echo.echofarm.Data.Dto.GetPostListDto;
import com.echo.echofarm.Interface.GetPostInfoListener;
import com.echo.echofarm.R;
import com.echo.echofarm.Service.Impl.PostServiceImpl;
import com.echo.echofarm.Service.PostService;

import java.util.ArrayList;
import java.util.List;

public class MakePostActivity extends AppCompatActivity {
    PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postService = new PostServiceImpl();

        setContentView(R.layout.activity_make_post);

        //Test용 작업들.
        //테스트 이미지들 업로드임! 실제로는 리소스가 아닌 갤러기같은데서 받도록하기!
        //일단 png만 업로드 되도록함ㅅ
        /*
        Uri uri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.test1);
        Uri uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.test2);

        SendPostDto sendPostDto = new SendPostDto("TestUid", "TestTitle", Arrays.asList(uri1, uri2), "테스트 콘텐츠 입니다.",
                "Pencil", Arrays.asList("tag1", "tag2"), "ball", Arrays.asList("tag3", "tag4"), false);

        //어디까지나 사용 예시임! 아래는 각 함수 사용법 활용을 위한 예제. 리스너에 인텐트 달아서 연결시키거나 하는방식으로 사용
        //작성한 포스트 전송하는 함수
        postService.sendPostDto(sendPostDto, new StoreImgListener() {
            //각 이미지들의 저장이 끝났을 때 호출되는 콜백
            @Override
            public void onSuccess(String postId) {
                //getPostDto의 사용예시임! postId는 랜덤?생성임.
                //작성한 포스트 불러오는 함수.
                postService.getPostDto(postId, new GetImgUrlListener() {
                    //각 이미지의 url을 불러오는것이 성공했을 때 호출되는 콜백
                    @Override
                    public void onSuccess(GetPostDto getPostDto) {
                        //getPostDto를 이용해 ㅅ인텐트로 새창 열거나 액티비티에 대입해 활용하는 형식으로 사용하면 됨.
                        //그런데 uri리스트가 객체 반환 이후에 이미지가 조금씩 추가되서 늦게 화면을 띄우거나 새로고침이 필요함.
                        System.out.println(getPostDto);
                    }

                    @Override
                    public void onFailed() {

                    }
                });

            }

            @Override
            public void onFailed() {

            }
        });
        */

        List<PostInfo> postInfoList = new ArrayList<>();

        System.out.println("###getPostList###");

        //메인에서 또는 프로필에서 게시글 리스트 불러올때.
        //초기 실행시 beforePostId를 null로.
        //limit인자로 페이지당 조회수를 지정해야 한다. 현재는 3으로 설정.
        //원활한 테스트를 위해 주석처리. 각 테스트를 원하면 풀어보기
        /*
        //전부 null이므로 아무 조건없이 검색이 실행됨. getPostListDto는 검색 조건임.
        GetPostListDto getPostListDto = new GetPostListDto();
        postService.getPostList(getPostListDto,
                null, 3, postInfoList, new GetPostInfoListener() {
                    //데이터는 일괄 로딩되지만, 사진은 한장씩 로딩됨.
                    @Override
                    public void onSuccess(PostInfo postInfo) {
                        //현재 postInfoList 다 다운 되있을거임
                        //System.out.println(postInfoList);
                        //현재 한장씩 다운로드 되는 사진들. 각 사진마다 해당 사진에 대해 다시 액티비티에 띄워야 함.
                        System.out.println("first PostList: " + postInfo);



                        //포스트 리스트 불러오기 버튼을 만들지 않아 일단 임시 테스트를 위해 여기에 함수를 재 호출함.
                        //이전의 포스트 리스트 실행에서 마지막 리스트의 postInfo.getPostId를 실행해 Id를 가지고 있는다.
                        //얘를 인자로 집어넣어 그 이후부터 조회.
                        //limit인자로 페이지당 조회수를 지정할 수 있다. 현재는 3으로 설정.
                        //이 명령으로 인해 그 다음 페이지(3개 이후) 포스트를  조회할 수 있다.
                        //리스트에 추가된 형태임. 리스트를 새로 만드는게 아니라. 따라서 이전 3개에 지금 불러오는것 3개 총 리스트에는 6개의 데이터가 담기게됨.
                        //지금은 따로 실행버튼을 만들수 없어 이렇게 함수 안에 넣어서 실행했음. 원래 이렇게 쓰는게 아님. 내부에서 돌리기에 이미지 하나 받아올때마다 조회가 여러번 실행되는것.
                        String beforePostId = postInfoList.get(postInfoList.size()-1).getPostId();

                        //메인에서 또는 프로필에서 게시글 리스트 불러올때.
                        postService.getPostList(getPostListDto,
                                beforePostId, 3, postInfoList, new GetPostInfoListener() {
                                    //데이터는 일괄 로딩되지만, 사진은 한장씩 로딩됨.
                                    @Override
                                    public void onSuccess(PostInfo postInfo) {
                                        //현재 postInfoList 다 다운 되있을거임
                                        //System.out.println(postInfoList);
                                        //현재 한장씩 다운로드 되는 사진들. 각 사진마다 해당 사진에 대해 다시 액티비티에 띄워야 함.
                                        System.out.println("Second PostList: " + postInfo);
                                    }

                                    @Override
                                    public void onFailed() {
                                        System.out.println("getPostInfo Failed");
                                    }
                                });
                        //재호출 끝
                    }

                    @Override
                    public void onFailed() {
                        System.out.println("getPostInfo Failed");
                    }
                });

         */

        /*
        //유저 이름 조건 검색//나중에 유저 정보창의 거래내역 검색할떄 이걸로 하면 될듯.
        GetPostListDto getPostListDto2 = new GetPostListDto();
        getPostListDto2.setUid("Test3");

        postService.getPostList(getPostListDto2,
                null, 3, postInfoList, new GetPostInfoListener() {
                    //데이터는 일괄 로딩되지만, 사진은 한장씩 로딩됨.
                    @Override
                    public void onSuccess(PostInfo postInfo) {
                        //현재 postInfoList 다 다운 되있을거임
                        //System.out.println(postInfoList);
                        //현재 한장씩 다운로드 되는 사진들. 각 사진마다 해당 사진에 대해 다시 액티비티에 띄워야 함.
                        System.out.println("SearchUser PostList: " + postInfo);

                    }

                    @Override
                    public void onFailed() {
                        System.out.println("getPostInfo Failed");
                    }
                });*/
    }
}