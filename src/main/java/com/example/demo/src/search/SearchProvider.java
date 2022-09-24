package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.GetPostListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.demo.utils.JwtService;

import static com.example.demo.config.BaseResponseStatus.*;


@Service
public class SearchProvider {
    private final SearchDao searchDao;
    private final JwtService jwtService;

    @Autowired
    public SearchProvider(SearchDao searchDao , JwtService jwtService)
    {
        this.searchDao = searchDao;
        this.jwtService = jwtService;

    }


    //검색_정확도순

    public List<GetPostListRes> searchAccuracy(String words) throws BaseException
    {
        try{
            return searchDao.searchAccuracy(words);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //검색_낮은 가격순

    public List<GetPostListRes> searchLowPrice(String words) throws BaseException
    {
        try{
            return searchDao.searchLowPrice(words);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //검색_높은 가격순

    public List<GetPostListRes> searchHighPrice(String words) throws BaseException
    {
        try{
            return searchDao.searchHighPrice(words);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
