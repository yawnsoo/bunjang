package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.PostPostReq;
import com.example.demo.src.post.model.PostPostRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/post")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;

    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }


   /*
   * 판매글 등록 API
   * [Post] /post/new
   * @return BaseResponse<PostPostRes>
   * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostRes> createPost(@RequestBody PostPostReq postPostReq) {

        //FIXME
        // - condition 타입 varchar >>> int로 수정
        // - 테스트 때문에 post 테이블 L,M,S category null로 수정함 >>> not null로 다시 수정
        // - sql문 syntaxerror 때문에 condition -> pcondition으로 바꿈

        //TODO
        // - user_id를 어떻게 이어주지..?
        //  >>> 임의로 user_id:1 로 진행, 해더에 X-ACCESS-TOKEN으로 진행해야 하는데, user 테이블에 토큰 저장하는 column 추가 필요할듯.
        // - validation 추가
        //  > content 10자 이상, 2000자 이하
        //  > 수량 : 0~999개 까지 입력 가능
        // - 판매글 등록시,
        //  1. 사진(post_photos)
        //  2. 태그(post_tags)
        //  3. 카테고리(category)
        //  테이블 분리 되어 있음 >>> 각 입력 api 만들고 transaction 사용?

        try {
            int userId = jwtService.getUserIdx();
            PostPostRes postPostRes = postService.createPost(postPostReq, userId);
            return new BaseResponse<>(postPostRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
