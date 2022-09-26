package com.example.demo.src.jjim;

import com.example.demo.config.BaseException;
import com.example.demo.src.home.HomeDao;
import com.example.demo.src.home.model.GetHomePostsRes;
import com.example.demo.src.jjim.model.GetJjimCollectionPostRes;
import com.example.demo.src.jjim.model.GetJjimCollectionRes;
import com.example.demo.src.jjim.model.GetJjimPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class JjimProvider {

    private final JjimDao jjimDao ;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public JjimProvider(JjimDao jjimDao, JwtService jwtService) {
        this.jjimDao = jjimDao;
        this.jwtService = jwtService;
    }


    public List<List> getJjimPosts (int userId) throws BaseException {

//        try {
        List<List> jjim = new ArrayList<>();

        List<GetJjimPostsRes> getJjimPostsRes = jjimDao.getJjimPosts(userId);
        List<GetJjimCollectionRes> getJjimCollectionRes = jjimDao.getJjimCollections(userId);

        jjim.add(getJjimCollectionRes);
        jjim.add(getJjimPostsRes);

        return jjim;
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public String getJjimCollectionName (int jjim_collection_id) throws BaseException {
        try {
            String jjimCollectionName = jjimDao.getJjimCollectionName(jjim_collection_id);
            return jjimCollectionName;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetJjimCollectionPostRes> getJjimCollectionPosts(int userId, int jcId) throws BaseException{
        try {
            List<GetJjimCollectionPostRes> getJjimCollectionPostRes =jjimDao.getJjimCollectionPosts(userId, jcId);
            return getJjimCollectionPostRes;
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
