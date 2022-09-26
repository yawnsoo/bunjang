package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.home.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/home")
public class HomeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HomeProvider homeProvider;

    @Autowired
    private final HomeService homeService;

    @Autowired
    private final JwtService jwtService;

    public HomeController(HomeProvider homeProvider, HomeService homeService, JwtService jwtService) {
        this.homeProvider = homeProvider;
        this.homeService = homeService;
        this.jwtService = jwtService;
    }


    /*
     * 홈 판매글 조회 API
     * [Get] /:user_id
     * @return BaseResponse<GetHomePosts>
     * */

    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<GetHomePostsRes>> getHomePosts (@PathVariable("user_id") int userId) {

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetHomePostsRes> getHomePostRes = homeProvider.getHomePosts(userId);
            return new BaseResponse<>(getHomePostRes);


        }catch (BaseException exception){
                exception.printStackTrace();
                return new BaseResponse<>(exception.getStatus());
            }
    }


    /*
     * 홈 브랜드 목록 조회 API
     * [Get] /:user_id/brands
     * @return BaseResponse<List<GetBrandsRes>>
     * */
    @ResponseBody
    @GetMapping("/{user_id}/brands")
    public BaseResponse<List<GetBrandsRes>> getBrands (@PathVariable("user_id") int userId) {

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetBrandsRes> getBrandsRes = homeProvider.getBrands(userId);
            return new BaseResponse<>(getBrandsRes);


        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /*
     * 홈 브랜드 추가 API
     * [Post] /:user_id/brands
     * @return BaseResponse<List<GetBrandsRes>>
     * */
//    @ResponseBody
//    @PostMapping("/{user_id}/brands")
//    public BaseResponse<PostBrandFollowRes> postBrandFollow(@PathVariable("user_id") int userId ,@RequestBody PostBrandFollowReq postBrandFollowReq) {
//        try {
//
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if (userId != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//
//            PostBrandFollowRes postBrandFollowRes = homeService.postBrandFollow(userId, postBrandFollowReq);
//
//            return new BaseResponse<>(postBrandFollowRes);
//
//        } catch (BaseException exception){
//            exception.printStackTrace();
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }






    /*
     * 홈 브랜드별 판매글 조회 API
     * [Get] /:user_id/brands/:brand_id
     * @return BaseResponse<GetBrandPostsRes>
     * */
    @ResponseBody
    @GetMapping("/{user_id}/brandposts")
    public BaseResponse<List<GetBrandPostsRes>> getBrandPosts (@PathVariable("user_id") int userId) {

        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetBrandPostsRes> getBrandPostsRes = homeProvider.getBrandPosts(userId);
            return new BaseResponse<>(getBrandPostsRes);


        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }






}
