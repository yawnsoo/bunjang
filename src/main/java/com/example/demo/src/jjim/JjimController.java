package com.example.demo.src.jjim;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.home.model.GetBrandPostsRes;
import com.example.demo.src.jjim.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/jjim")
public class JjimController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final JjimProvider jjimProvider;
    @Autowired
    private final JjimService jjimService;
    @Autowired
    private final JwtService jwtService;


    public JjimController(JjimProvider jjimProvider, JjimService jjimService, JwtService jwtService) {
        this.jjimProvider = jjimProvider;
        this.jjimService = jjimService;
        this.jwtService = jwtService;
    }

    /*
     * 찜한 판매글, 컬렉션 조회 API
     * [Get] /:user_id
     * @return BaseResponse<List<GetJjimPostsRes>>
     * */
    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<List>> getJjimPosts(@PathVariable("user_id") int userId) {

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<List> getJjimPostsRes = jjimProvider.getJjimPosts(userId);
            return new BaseResponse<>(getJjimPostsRes);


        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /*
     * 찜한 판매글 삭제 API
     * [Patch] /:user_id
     * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/{user_id}")
    public BaseResponse<String> deleteJjimPost(@PathVariable("user_id") int userId, @RequestBody PatchJjimPostReq patchJjimPostReq) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            jjimService.deleteJjimPost(patchJjimPostReq);

            String result = "찜 해제가 완료되었습니다.";
            return new BaseResponse<>(result);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /*
     * 찜컬렉션 생성 API
     * [Post] /:user_id
     * @return BaseResponse<PostJjimCollectionRes>
     * */
    @ResponseBody
    @PostMapping("/{user_id}")
    public BaseResponse<PostJjimCollectionRes> createJjimCollection(@PathVariable("user_id") int userId, @RequestBody PostJjimCollectionReq postJjimCollectionReq) {


        if (postJjimCollectionReq.getName().length()>10) {
            return new BaseResponse<>(COLLECTION_LARGE_NAME);
        }

        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostJjimCollectionRes postJjimCollectionRes = jjimService.createJjimCollection(userId,postJjimCollectionReq);
            return new BaseResponse<>(postJjimCollectionRes);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /*
     * 찜컬렉션의 판매글 조회 API
     * [Get] /:user_id/:jjim_collection_id
     * @return BaseResponse<String>
     * */
    @ResponseBody
    @GetMapping("/{user_id}/{jjim_collection_id}")
    public BaseResponse<List<GetJjimCollectionPostRes>> getJjimCollectionPosts(@PathVariable("user_id") int userId, @PathVariable("jjim_collection_id") int jcId) {

        //FIXME
        // - BaseResponse 추가하기

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetJjimCollectionPostRes> getJjimCollectionPostRes = jjimProvider.getJjimCollectionPosts(userId, jcId);
            return new BaseResponse<>(getJjimCollectionPostRes);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }




    /*
     * 찜컬렉션의 판매글 수정 API
     * [Patch] /:user_id/:jjim_collection_id
     * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/{user_id}/{jjim_collection_id}")
    public BaseResponse<String> modifyJjimPostCollection(@PathVariable("user_id") int userId, @PathVariable("jjim_collection_id") int jcId, @RequestBody PatchPostCollectionReq patchPostCollectionReq) {


        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

//            PatchPostCollectionReq patchPostCollectionReq = new PatchPostCollectionReq(post.getJjim_id(), post.getTo_jjim_collection_id());
            jjimService.modifyJjimPostCollection(userId, jcId, patchPostCollectionReq);

//            String jjimCollectionName = jjimProvider.getJjimCollectionName(post.getTo_jjim_collection_id());
            String jjimCollectionName = jjimProvider.getJjimCollectionName(patchPostCollectionReq.getTo_jjim_collection_id());

            String result = jjimCollectionName + " 컬렉션으로 이동했습니다";

            return new BaseResponse<>(result);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
     * 찜컬렉션 수정 API
     * [Patch] /:user_id/:jjim_collection_id/edit
     * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/{user_id}/{jjim_collection_id}/edit")
    public BaseResponse<String> editJjimCollectionName(@PathVariable("user_id") int userId, @PathVariable("jjim_collection_id") int jcId, @RequestBody PostJjimCollectionReq postJjimCollectionReq) {

        if (postJjimCollectionReq.getName().length()>10) {
            return new BaseResponse<>(COLLECTION_LARGE_NAME);
        }

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int editJjimCollectionName = jjimService.editJjimCollectionName(postJjimCollectionReq, userId, jcId);

            if (editJjimCollectionName == 0) {
                throw new BaseException(NOT_MY_COLLECTION);
            } else {
                return new BaseResponse<>("컬렉션 이름 변경 성공");
            }

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
     * 찜컬렉션 삭제 API
     * [Patch] /:user_id/:jjim_collection_id/delete
     * @return BaseResponse<String>
     * */

    @ResponseBody
    @DeleteMapping("/{user_id}/{jjim_collection_id}")
    public BaseResponse<String> deleteJjimCollection(@PathVariable("user_id") int userId, @PathVariable("jjim_collection_id") int jcId){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int deleteJjimCollection = jjimService.deleteJjimCollection(userId, jcId);

            if (deleteJjimCollection == 0) {
                throw new BaseException(NOT_MY_COLLECTION);
            } else {
                return new BaseResponse<>("컬렉션 삭제 성공");
            }

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
