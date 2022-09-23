package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.src.s3.S3UploadController;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private final S3UploadController s3UploadController;

    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService, S3UploadController s3UploadController) {
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
        this.s3UploadController = s3UploadController;
    }


   /*
   * 판매글 등록 API
   * [Post] /post
   * @return BaseResponse<PostPostRes>
   * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostRes> createPost(@RequestPart(value = "json") PostPostReq postPostReq,
                                                @RequestPart(value = "img") MultipartFile[] img) throws Exception {

        //FIXME
        // - 카테고리 입력 받는거로 수정해야함
        // - (post 테이블) condition : sql문 syntaxerror 때문에 condition -> pcondition으로 바꿈, 타입 varchar >>> int로 수정
        // - 테스트 때문에 post 테이블 L,M,S category null로 수정함 >>> not null로 다시 수정할 예정


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

//        ResponseEntity<Object> imgUrl = s3UploadController.upload(img);
        List<String> imgUrl = s3UploadController.upload(img);


        try {
            int userId = jwtService.getUserIdx();
            PostPostRes postPostRes = postService.createPost(postPostReq, userId, imgUrl);
            return new BaseResponse<>(postPostRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    * 판매글 카테고리별 조회 API
    * [GET] /post/category?LCId=&MCId=&SCId=
    * @return BaseResponse<List<GetPostRes>>
    * */
    @ResponseBody
    @GetMapping("/category")
    public BaseResponse<List<GetPostRes>> getPostsByCategory(@RequestParam(required = false) Integer LCId, @RequestParam(required = false) Integer MCId, @RequestParam(required = false) Integer SCId) {

        //TODO
        // - Query String 이렇게 설정 하는게 맞을까..? 더 고민해보자.

        try{
            if (LCId != null && MCId == null && SCId == null) {
                List<GetPostRes> getPostsRes = postProvider.getPostsByLC(LCId);
                return new BaseResponse<>(getPostsRes);
            } else if (MCId != null && SCId == null) {
                List<GetPostRes> getPostsRes = postProvider.getPostsByMC(MCId);
                return new BaseResponse<>(getPostsRes);
            } else if (SCId != null) {
                List<GetPostRes> getPostsRes = postProvider.getPostsBySC(SCId);
                return new BaseResponse<>(getPostsRes);
            }

            List<GetPostRes> getPostsRes = postProvider.getPosts();
            return new BaseResponse<>(getPostsRes);

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
//    @ResponseBody
//    @PostMapping("/{post_id}/photo")
//    public BaseResponse<PostPhotoRes> addPhoto(@PathVariable("post_id") int post_id , @RequestBody PostPhotoReq postPhotoReq) {
//
//        //TODO
//        // - validation
//        // - 사진 1개만 등록 가능.... 한번에 여러개 추가 가능하도록 수정
//
//        try {
//            postPhotoReq.setPost_id(post_id);
//
//            PostPhotoRes postPhotoRes = postService.addPhoto(postPhotoReq);
//            return new BaseResponse<>(postPhotoRes);
//        }
//        catch (BaseException exception){
//            exception.printStackTrace();
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//    }


    /*
     * 판매글 태그 등록 API
     * [Post] /post/:post_id/tag
     * @return BaseResponse<PostTagRes>
     * */
//    @ResponseBody
//    @PostMapping("/{post_id}/tag")
//    public BaseResponse<PostTagRes> addTag(@PathVariable("post_id") int post_id, @RequestBody PostTagReq postTagReq) {
//
//        //TODO
//        // - validation
//        // - 태그 1개만 등록 가능.... 한번에 여러개 추가 가능하도록 수정
//
//        try{
//            postTagReq.setPost_id(post_id);
//
//            PostTagRes postTagRes = postService.addTag(postTagReq);
//            return new BaseResponse<>(postTagRes);
//        }
//        catch (BaseException exception){
//            exception.printStackTrace();
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }

    /*
     * 판매글 카테고리 대분류 선택 API
     * [Get] /post/category-large
     * @return BaseResponse<List<GetLCategoryRes>>
     * */
    @ResponseBody
    @GetMapping("/category-large")
    public BaseResponse<List<GetLCategoryRes>> getLCategory() {
        try {
            List<GetLCategoryRes> getLCategoryRes = postProvider.getLCategory();
            return new BaseResponse<>(getLCategoryRes);
        }
        catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /*
     * 판매글 카테고리 중분류 선택 API
     * [Get] /post/category-middle?LCId=
     * @return BaseResponse<List<GetMCategoryRes>>
     * */
    @ResponseBody
    @GetMapping("/category-middle")
    public BaseResponse<List<GetMCategoryRes>> getMCategory(@RequestParam() int LCId) {
        try {
            List<GetMCategoryRes> getMCategoryRes = postProvider.getMCategory(LCId);
            return new BaseResponse<>(getMCategoryRes);
        }
        catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /*
     * 판매글 카테고리 소분류 선택 API
     * [Get] /post/category-small?MCId=
     * @return BaseResponse<List<GetSCategoryRes>>
     * */
    @ResponseBody
    @GetMapping("/category-small")
    public BaseResponse<List<GetSCategoryRes>> getSCategory(@RequestParam() int MCId) {
        try {
            List<GetSCategoryRes> getSCategoryRes = postProvider.getSCategory(MCId);
            return new BaseResponse<>(getSCategoryRes);
        }
        catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }



    /*
    * 옵션 선택 API
    * 지금은 createPost에서 한번에 입력하는데,
    * 수량,상품상태,교환 Post 방식 API를 따로 빼서 만들어야하나??
    * */




}
