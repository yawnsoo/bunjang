package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.home.model.GetHomePostsRes;
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
    private final JwtService jwtService;

    public HomeController(HomeProvider homeProvider, JwtService jwtService) {
        this.homeProvider = homeProvider;
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

}
