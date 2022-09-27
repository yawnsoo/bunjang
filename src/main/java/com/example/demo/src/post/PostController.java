package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.*;
import com.example.demo.src.s3.S3UploadController;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    public BaseResponse<PostPostRes> createPost(@RequestBody PostPostReq postPostReq) throws Exception {


        if (postPostReq.getEncoded_image() == null) {
            return new BaseResponse<>(POST_EMPTY_IMG);
        }
        if (postPostReq.getTitle() == null) {
            return new BaseResponse<>(POST_EMPTY_TITLE);
        }
        if (postPostReq.getTitle().length() < 2) {
            return new BaseResponse<>(POST_SHORT_TITLE);
        }
        if ( postPostReq.getPrice() < 100) {
            return new BaseResponse<>(POST_LOW_PRICE);
        }
        if (postPostReq.getContent().length() < 10) {
            return new BaseResponse<>(POST_SHORT_CONTENT);
        }
        if (postPostReq.getContent().length() > 2000) {
            return new BaseResponse<>(POST_LONG_CONTENT);
        }
        if (postPostReq.getCount() < 0 || postPostReq.getCount() > 1000) {
            return new BaseResponse<>(POST_RANGE_COUNT);
        }


//        ResponseEntity<Object> imgUrl = s3UploadController.upload(img);
        String base64EncodedFile = postPostReq.getEncoded_image();
        byte[] decodedFile = Base64.getMimeDecoder().decode(base64EncodedFile);
        String imgUrl = s3UploadController.upload2(decodedFile);


        try {
            int userId = jwtService.getUserIdx();
            PostPostRes postPostRes = postService.createPost2(postPostReq, userId, imgUrl);
            return new BaseResponse<>(postPostRes);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
     * 판매글 상세 조회 API
     * [GET] /post/:post_id
     * @return BaseResponse<GetPostDetailRes>
     * */
    @ResponseBody
    @GetMapping("/{post_id}")
    public BaseResponse<GetPostDetailRes> getPostDetail(@PathVariable("post_id") int postId) {

        GetPostDetailRes getPostDetailRes = postProvider.getPostDetail(postId);
        return new BaseResponse<>(getPostDetailRes);

    }


    /*
     * 판매글 삭제 API
     * [Patch] /post/:post_id
     * @return BaseResponse<GetPostDetailRes>
     * */
    @ResponseBody
    @PatchMapping("/{post_id}")
    public BaseResponse<String> deletePost(@PathVariable("post_id") int post_id) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int deletePost = postService.deletePost(userIdxByJwt, post_id);
            if (deletePost == 0) {
                throw new BaseException(INVALID_USER_JWT);
            }
            else{
                return new BaseResponse<>("삭제되었습니다.");
            }
        }catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }

    }



    /*
     * 판매글 수정 API
     * [Patch] /post/:post_id/edit
     * @return BaseResponse<GetPostDetailRes>
     * */
    @ResponseBody
    @PatchMapping("/{post_id}/edit")
    public BaseResponse<String> editPost(@PathVariable("post_id") int postId, @RequestBody PostPostReq postPostReq) throws Exception{

        if (postPostReq.getEncoded_image() == null) {
            return new BaseResponse<>(POST_EMPTY_IMG);
        }
        if (postPostReq.getTitle() == null) {
            return new BaseResponse<>(POST_EMPTY_TITLE);
        }
        if (postPostReq.getTitle().length() < 2) {
            return new BaseResponse<>(POST_SHORT_TITLE);
        }
        if ( postPostReq.getPrice() < 100) {
            return new BaseResponse<>(POST_LOW_PRICE);
        }
        if (postPostReq.getContent().length() < 10) {
            return new BaseResponse<>(POST_SHORT_CONTENT);
        }
        if (postPostReq.getContent().length() > 2000) {
            return new BaseResponse<>(POST_LONG_CONTENT);
        }
        if (postPostReq.getCount() < 0 || postPostReq.getCount() > 1000) {
            return new BaseResponse<>(POST_RANGE_COUNT);
        }



        String base64EncodedFile = postPostReq.getEncoded_image();
        byte[] decodedFile = Base64.getMimeDecoder().decode(base64EncodedFile);
        String imgUrl = s3UploadController.upload2(decodedFile);

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            int editPost = postService.editPost(postPostReq, userIdxByJwt, imgUrl, postId);
            if (editPost == 0) {
                throw new BaseException(INVALID_USER_JWT);
            } else {
                return new BaseResponse<>("상품수정이 완료되었습니다.");
            }
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

        try{
            if (LCId != null && MCId == null && SCId == null) {
                List<GetPostRes> getPostsRes = postProvider.getPostsByLC(LCId);
                return new BaseResponse<>(getPostsRes);
            } else if (MCId != null && SCId == null) {
                List<GetPostRes> getPostsRes = postProvider.getPostsByMC(LCId,MCId);
                return new BaseResponse<>(getPostsRes);
            } else if (SCId != null) {
                if (LCId != null && MCId == null) {
                    return new BaseResponse<>(ERASE_LARGEorSMALL_CATEGORY_ID);
                }
                List<GetPostRes> getPostsRes = postProvider.getPostsBySC(LCId,MCId,SCId);
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









}
