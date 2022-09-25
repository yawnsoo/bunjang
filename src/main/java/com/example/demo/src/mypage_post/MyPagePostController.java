package com.example.demo.src.mypage_post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.mypage_post.model.GetMyPostsRes;
import com.example.demo.src.post.PostProvider;
import com.example.demo.src.post.PostService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/app/mypage")
public class MyPagePostController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MyPagePostProvider myPagePostProvider;
//    @Autowired
//    private final MyPagePostService postService;
    @Autowired
    private final JwtService jwtService;


    public MyPagePostController(MyPagePostProvider myPagePostProvider, JwtService jwtService) {
        this.myPagePostProvider = myPagePostProvider;
        this.jwtService = jwtService;
    }


    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<GetMyPostsRes>> getMyPosts(@PathVariable("user_id") int userId) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetMyPostsRes> getMyPostsRes = myPagePostProvider.getMyPosts(userId);
            return new BaseResponse<>(getMyPostsRes);

        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
