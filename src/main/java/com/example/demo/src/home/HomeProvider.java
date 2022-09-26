package com.example.demo.src.home;


import com.example.demo.config.BaseException;
import com.example.demo.src.home.model.GetBrandPostsRes;
import com.example.demo.src.home.model.GetBrandsRes;
import com.example.demo.src.home.model.GetHomePostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandsRes> getBrands(int userId) throws BaseException {
        try {
            List<GetBrandsRes> getBrandsRes = homeDao.getBrands(userId);
            return getBrandsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetBrandPostsRes> getBrandPosts(int userId) throws BaseException {
        try {
            List<GetBrandPostsRes> getBrandPostsResList = new ArrayList<>();

            List<GetBrandsRes> getBrandRes = homeDao.getBrand(userId);

            for( GetBrandsRes b : getBrandRes){
                String brand_kor = b.getName_kr();
                String brand_eng = b.getName_eng();

                List<GetHomePostsRes> getPostsRes = homeDao.getPosts(brand_kor, brand_eng);
                GetBrandPostsRes getBrandPostsRes = new GetBrandPostsRes(b, getPostsRes);
                getBrandPostsResList.add(getBrandPostsRes);
            }

            return getBrandPostsResList;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public int checkFollowInfo(int userId, int brandId) throws  BaseException {
//        try {
//            return homeDao.checkFollowInfo(userId, brandId);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


}
