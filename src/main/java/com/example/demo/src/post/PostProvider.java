package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostProvider {
    private final PostDao postDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService;
    }

    public List<GetLCategoryRes> getLCategory() throws BaseException {

            List<GetLCategoryRes> getLCategoryRes = postDao.getLCategory();
            return getLCategoryRes;
    }

    public List<GetMCategoryRes> getMCategory(int LCategory_id) throws BaseException {

        List<GetMCategoryRes> getMCategoryRes = postDao.getMCategory(LCategory_id);
        return getMCategoryRes;
    }

    public List<GetSCategoryRes> getSCategory(int MCategory_id) throws BaseException {

        List<GetSCategoryRes> getSCategoryRes = postDao.getSCategory(MCategory_id);
        return getSCategoryRes;
    }

    public List<GetPostRes> getPosts() throws BaseException {
        List<GetPostRes> getPostsRes = postDao.getPosts();
        return getPostsRes;
    }

    public List<GetPostRes> getPostsByLC(Integer LCId) throws BaseException {
        List<GetPostRes> getPostsRes = postDao.getPostsByLC(LCId);
        return getPostsRes;
    }
    public List<GetPostRes> getPostsByMC(Integer MCId) throws BaseException {
        List<GetPostRes> getPostsRes = postDao.getPostsByMC(MCId);
        return getPostsRes;
    }
    public List<GetPostRes> getPostsBySC(Integer SCId) throws BaseException {
        List<GetPostRes> getPostsRes = postDao.getPostsBySC(SCId);
        return getPostsRes;
    }


    public GetPostDetailRes getPostDetail(int postId) {
        PostDetailRes postDetailRes = postDao.PostDetailRes(postId);
        List<String> photos = postDao.getPhotos(postId);
        List<String> tags = postDao.getTags(postId);

        GetPostDetailRes getPostDetailRes = new GetPostDetailRes(postDetailRes.getPost_id(), photos, postDetailRes.getUser_id(), postDetailRes.getTitle(), postDetailRes.getRegion(), postDetailRes.getCreated_at(), postDetailRes.getCategory_large(), postDetailRes.getCategory_middle(), postDetailRes.getCategory_small(), tags, postDetailRes.getPrice(), postDetailRes.getContent(), postDetailRes.getCount(), postDetailRes.getIs_exchangable(), postDetailRes.getSafepay(), postDetailRes.getDelivery_fee(), postDetailRes.getPcondition());

        return getPostDetailRes;
    }
}
