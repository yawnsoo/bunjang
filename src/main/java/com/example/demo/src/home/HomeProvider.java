package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.src.home.model.GetHomePostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class HomeProvider {

    private final HomeDao homeDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HomeProvider(HomeDao homeDao, JwtService jwtService) {
        this.homeDao = homeDao;
        this.jwtService = jwtService;
    }


    public List<GetHomePostsRes> getHomePosts(int userId) throws BaseException {
        try {
            List<GetHomePostsRes> getHomePostsRes = homeDao.getHomePosts(userId);
            return getHomePostsRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
