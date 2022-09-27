package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.RELATION_ERROR_LM;
import static com.example.demo.config.BaseResponseStatus.RELATION_ERROR_MS;

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
    public List<GetPostRes> getPostsByMC(Integer LCId, Integer MCId) throws BaseException {
        if (LCId != null) {
            int checkRelationLM = postDao.checkRelationLM(LCId, MCId);
            if (checkRelationLM == 0) {
                throw new BaseException(RELATION_ERROR_LM);
            }
        }
        List<GetPostRes> getPostsRes = postDao.getPostsByMC(MCId);
        return getPostsRes;
    }
    public List<GetPostRes> getPostsBySC(Integer LCId, Integer MCId, Integer SCId) throws BaseException {
        if (LCId != null && MCId != null) {
            int checkRelationLM = postDao.checkRelationLM(LCId, MCId);
            if (checkRelationLM == 0) {
                throw new BaseException(RELATION_ERROR_LM);
            }
        }
        if (MCId != null) {
            int checkRelationMS = postDao.checkRelationMS(MCId, SCId);
            if (checkRelationMS == 0) {
                throw new BaseException(RELATION_ERROR_MS);
            }
        }

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
