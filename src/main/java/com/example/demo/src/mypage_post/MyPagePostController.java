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

import static com.example.demo.config.BaseResponseStatus.*;

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

    /*
     * 마이페이지 판매글 조회 API
     * [GET] /:user_id?status=&safepay=
     * @return BaseResponse<GetPostDetailRes>
     * */
    @ResponseBody
    @GetMapping("/{user_id}")
    public BaseResponse<List<GetMyPostsRes>> getMyPosts(@PathVariable("user_id") int userId, @RequestParam(required = false) Integer status, @RequestParam(required = false) Integer safepay) {

        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (status != null && safepay != null) {
                return new BaseResponse<>(QUERY_STRING_ERROR);
            }

            if (status != null && (-1 < status && status < 4)) { // 판매완, 판매중, 예약중, 광고중
                List<GetMyPostsRes> getMyPostsRes = myPagePostProvider.getMyPostsWStatus(userId,status);
                return new BaseResponse<>(getMyPostsRes);
            } else if(status != null && (0 > status || status > 3)) {
                return new BaseResponse<>(Check_Status);
            }

            if (safepay != null && safepay == 1){ //페이결제 가능
                List<GetMyPostsRes> getMyPostsRes = myPagePostProvider.getMyPostsWSafepay(userId,safepay);
                return new BaseResponse<>(getMyPostsRes);
            } else if (safepay != null && safepay != 1) {
                return new BaseResponse<>(Check_Safepay);
            }



            //전체
            List<GetMyPostsRes> getMyPostsRes = myPagePostProvider.getMyPosts(userId);
            return new BaseResponse<>(getMyPostsRes);



        }catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }


}
