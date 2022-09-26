package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.src.home.model.PostBrandFollowReq;
import com.example.demo.src.home.model.PostBrandFollowRes;
import com.example.demo.src.post.PostDao;
import com.example.demo.src.post.PostProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HomeService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HomeDao homeDao;
    private final HomeProvider homeProvider;
    private final JwtService jwtService;

    public HomeService(HomeDao homeDao, HomeProvider homeProvider, JwtService jwtService) {
        this.homeDao = homeDao;
        this.homeProvider = homeProvider;
        this.jwtService = jwtService;
    }

//    public PostBrandFollowRes postBrandFollow(int userId, PostBrandFollowReq postBrandFollowReq) throws BaseException {
//
//        if (homeProvider.checkFollowInfo(userId, postBrandFollowReq.getBrand_id()) == 1) {
//
//        }
//
//    }

}
