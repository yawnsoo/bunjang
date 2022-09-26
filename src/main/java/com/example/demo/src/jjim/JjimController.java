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

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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
     * 찜한 판매글 조회 API
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
     * 찜컬렉션의 판매글 조회 API
     * [Get] /:user_id/:jjim_collection_id
     * @return BaseResponse<String>
     * */
    @ResponseBody
    @GetMapping("/{user_id}/{jjim_collection_id}")
    public BaseResponse<List<GetJjimCollectionPostRes>> getJjimCollectionPosts(@PathVariable("user_id") int userId, @PathVariable("jjim_collection_id") int jcId) {

        //FIXME
        // - 판매중, 예약.판매완 나누기
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
    public BaseResponse<String> modifyJjimPostCollection(@PathVariable("user_id") int userId, @RequestBody PatchPostCollectionReq patchPostCollectionReq) {

        //TODO
        // - userId로 찜컬렉션Id, 찜Id, 찜post_Id validation 필요
        // - 같은 곳으로 이동할 경우 반환 값 없음

        //FIXME
        // - 지금은 하나만 됨, 여러개 선택해서 보낼 때......

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

//            PatchPostCollectionReq patchPostCollectionReq = new PatchPostCollectionReq(post.getJjim_id(), post.getTo_jjim_collection_id());
            jjimService.modifyJjimPostCollection(patchPostCollectionReq);

//            String jjimCollectionName = jjimProvider.getJjimCollectionName(post.getTo_jjim_collection_id());
            String jjimCollectionName = jjimProvider.getJjimCollectionName(patchPostCollectionReq.getTo_jjim_collection_id());

            String result = jjimCollectionName + " 컬렉션으로 이동했습니다";

            return new BaseResponse<>(result);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }







}
