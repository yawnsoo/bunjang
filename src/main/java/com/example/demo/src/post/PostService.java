package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;

    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;
    }

    public PostPostRes createPost(PostPostReq postPostReq, int user_id, List<String> img) throws BaseException {
        //validation

//        try {
            int userId = user_id;

//            PostPostReq postPostReq = postPostAssemble.getPostPostReq();
//            PostPhotoReq postPhotoReq = postPostAssemble.getPostPhotoReq();
//            PostTagReq postTagReq = postPostAssemble.getPostTagReq();

            int post_id = postDao.createPost(postPostReq, userId);

            GetPostDetailRes postDetail = postDao.getPostDetailRes(post_id);

            List<String> photos = postDao.addPhoto(img, post_id);

            List<String> tags = postDao.addTag(postPostReq, post_id);

            PostPostRes postPostRes = new PostPostRes(postDetail.getPost_id(),img,postDetail.getTitle(),postDetail.getRegion(), postDetail.getCreated_at(),postDetail.getCategory_large(),postDetail.getCategory_middle(),postDetail.getCategory_small(),tags,postDetail.getPrice(),postDetail.getContent(),postDetail.getCount(),postDetail.getIs_exchangable(),postDetail.getSafepay(),postDetail.getDelivery_fee(),postDetail.getPcondition());

            return postPostRes;
//        } catch (Exception exception) {
//            throw new BaseException(DATABAS_ERROR);
//        }
    }

//    public PostPhotoRes addPhoto(PostPhotoReq postPhotoReq) throws BaseException{
//
//        int post_photo_id = postDao.addPhoto(postPhotoReq);
//        return new PostPhotoRes(post_photo_id);
//    }

//    public PostTagRes addTag(PostTagReq postTagReq) throws BaseException{
//
//        int post_tag_id = postDao.addTag(postTagReq);
//        return new PostTagRes(post_tag_id);
//    }

}
