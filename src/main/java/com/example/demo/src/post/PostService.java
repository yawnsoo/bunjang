package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.PostPhotoReq;
import com.example.demo.src.post.model.PostPhotoRes;
import com.example.demo.src.post.model.PostPostReq;
import com.example.demo.src.post.model.PostPostRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public PostPostRes createPost(PostPostReq postPostReq, int user_id) throws BaseException {
        //validation

//        try {
            int userId = user_id;
            int post_id = postDao.createPost(postPostReq, userId);
            return new PostPostRes(post_id);
//        } catch (Exception exception) {
//            throw new BaseException(DATABAS_ERROR);
//        }
    }

    public PostPhotoRes addPhoto(PostPhotoReq postPhotoReq) throws BaseException{

        int post_photo_id = postDao.addPhoto(postPhotoReq);
        return new PostPhotoRes(post_photo_id);
    }

}
