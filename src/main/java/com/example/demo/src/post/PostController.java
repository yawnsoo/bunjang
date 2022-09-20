package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
   * [Post] /post
   * @return BaseResponse<PostPostRes>
   * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostRes> createPost(@RequestBody PostPostReq postPostReq) {

        //FIXME
        // - (post 테이블) condition : sql문 syntaxerror 때문에 condition -> pcondition으로 바꿈, 타입 varchar >>> int로 수정
        // - 테스트 때문에 post 테이블 L,M,S category null로 수정함 >>> not null로 다시 수정할 예정
        //

        //TODO
        // - validation 추가
        //  > 사진, 제목, 카테고리, 가격, 본문내용 필수 입력
        //  > 본문(content) 10자 이상, 2000자 이하
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


    /*
     * 판매글 사진 등록 API
     * [Post] /post/:post_id/photo
     * @return BaseResponse<PostPhotoRes>
     * */
    @ResponseBody
    @PostMapping("/{post_id}/photo")
    public BaseResponse<PostPhotoRes> addPhoto(@PathVariable("post_id") int post_id , @RequestBody PostPhotoReq postPhotoReq) {

        //TODO
        // - validation
        // - 사진 1개만 등록 가능.... 한번에 여러개 추가 가능하도록 수정

        try {
            postPhotoReq.setPost_id(post_id);

            PostPhotoRes postPhotoRes = postService.addPhoto(postPhotoReq);
            return new BaseResponse<>(postPhotoRes);
        }
        catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /*
     * 판매글 태그 등록 API
     * [Post] /post/:post_id/tag
     * @return BaseResponse<PostTagRes>
     * */
    @ResponseBody
    @PostMapping("/{post_id}/tag")
    public BaseResponse<PostTagRes> addTag(@PathVariable("post_id") int post_id, @RequestBody PostTagReq postTagReq) {

        //TODO
        // - validation

        try{
            postTagReq.setPost_id(post_id);

            PostTagRes postTagRes = postService.addTag(postTagReq);
            return new BaseResponse<>(postTagRes);
        }
        catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }



}
