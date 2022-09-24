package com.example.demo.src.search;

import com.example.demo.src.search.model.GetPostListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/search")
public class SearchController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;

    public SearchController (SearchProvider searchProvider ,SearchService searchService ,JwtService jwtService )
    {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }



    /**
     * 단어검색 api (정확도순 == > 검색한 단어가 제목에 있는지 체크하여 상품 리스트 ,정보 뿌리기)
     * */

    @ResponseBody
    @GetMapping("/accuracy/{words}")
    public BaseResponse<List<GetPostListRes>> searchAccuracy(@PathVariable("words")String words)
    {
        //검색헀는데 상품리스트가 하나도없는경우

        //검색어가 빈값인경우.
        if(words.equals(null) )
        {
            return new BaseResponse<>(EMPTY_SEARCH_WORDS);
        }


        //그냥 검색되는경우.
        try{

            List<GetPostListRes> getPostListRes = searchProvider.searchAccuracy(words);
            return new BaseResponse<>(getPostListRes);

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 단어검색 api (낮은가격순)
     * */

    @ResponseBody
    @GetMapping("/lowprice/{words}")
    public BaseResponse<List<GetPostListRes>> searchLowPrice(@PathVariable("words")String words)
    {
        //검색헀는데 상품리스트가 하나도없는경우

        //검색어가 빈값인경우.
        if(words.equals(null) )
        {
            return new BaseResponse<>(EMPTY_SEARCH_WORDS);
        }


        //그냥 검색되는경우.
        try{
            List<GetPostListRes> getPostListRes = searchProvider.searchLowPrice(words);
            return new BaseResponse<>(getPostListRes);


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 단어검색 api (높은가격순)
     * */

    @ResponseBody
    @GetMapping("/highprice/{words}")
    public BaseResponse<List<GetPostListRes>> searchHighPirce(@PathVariable("words")String words)
    {


        //그냥 검색되는경우.
        try{
            List<GetPostListRes> getPostListRes = searchProvider.searchHighPrice(words);
            return new BaseResponse<>(getPostListRes);


        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

    }




}
