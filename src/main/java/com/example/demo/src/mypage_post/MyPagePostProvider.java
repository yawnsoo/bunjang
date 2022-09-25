package com.example.demo.src.mypage_post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.mypage_post.model.GetMyPostsRes;
import com.example.demo.src.post.PostDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPagePostProvider {

    private final MyPagePostDao myPagePostDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MyPagePostProvider(MyPagePostDao myPagePostDao, JwtService jwtService) {
        this.myPagePostDao = myPagePostDao;
        this.jwtService = jwtService;
    }

    public List<GetMyPostsRes> getMyPosts(int userId) throws BaseException {

        List<GetMyPostsRes> getMyPostsRes = myPagePostDao.getMyPosts(userId);

        return getMyPostsRes;

    }

    public List<GetMyPostsRes> getMyPostsWStatus(int userId, Integer status) throws BaseException {

        List<GetMyPostsRes> getMyPostsRes = myPagePostDao.getMyPostsWStatus(userId,status);

        return getMyPostsRes;

    }

    public List<GetMyPostsRes> getMyPostsWSafepay(int userId, Integer safepay) throws BaseException {

        List<GetMyPostsRes> getMyPostsRes = myPagePostDao.getMyPostsWSafepay(userId,safepay);

        return getMyPostsRes;

    }


}
