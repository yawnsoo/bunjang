package com.example.demo.src.jjim;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.jjim.model.PatchJjimPostReq;
import com.example.demo.src.jjim.model.PatchPostCollectionReq;
import com.example.demo.src.jjim.model.PostJjimCollectionReq;
import com.example.demo.src.jjim.model.PostJjimCollectionRes;
import com.example.demo.src.post.PostDao;
import com.example.demo.src.post.PostProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class JjimService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JjimDao jjimDao;
    private final JjimProvider jjimProvider;
    private final JwtService jwtService;

    @Autowired
    public JjimService(JjimDao jjimDao, JjimProvider jjimProvider, JwtService jwtService) {
        this.jjimDao = jjimDao;
        this.jjimProvider = jjimProvider;
        this.jwtService = jwtService;
    }


    public void modifyJjimPostCollection(int userId, int jcId, PatchPostCollectionReq patchPostCollectionReq) throws BaseException {

        int checkUserAuth = jjimDao.checkUserAuth(userId, jcId);
        if (checkUserAuth == 0) {
            throw new BaseException(INVALID_USER_COLLECTION);
        }

        int checkCollectionPost = jjimDao.checkCollectionPost(jcId,patchPostCollectionReq.getJjim_id());
        if (checkCollectionPost == 0) {
            throw new BaseException(WRONG_COLLECTION_JJIMID);
        }

        int result = jjimDao.modifyJjimPostCollection(patchPostCollectionReq);
        if (result == 0) {
            throw new BaseException(BaseResponseStatus.MODIFY_FAIL_JJIM_COLLECTION);
        }
    }

    public void deleteJjimPost(PatchJjimPostReq patchJjimPostReq) throws BaseException {
        int result = jjimDao.deleteJjimPost(patchJjimPostReq);
        if (result == 0) {
            throw new BaseException(BaseResponseStatus.DELETE_FAIL_JJIM);
        }
    }

    public PostJjimCollectionRes createJjimCollection(int userId,PostJjimCollectionReq postJjimCollectionReq) throws BaseException{
        try {
            int jjim_collectin_id = jjimDao.createJjimCollection(userId, postJjimCollectionReq);

            return new PostJjimCollectionRes(jjim_collectin_id, postJjimCollectionReq.getName());

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }

    public int editJjimCollectionName(PostJjimCollectionReq postJjimCollectionReq,int userId,int jcId) throws BaseException {
        try {
            int checkUserAuth = jjimDao.checkUserAuth(userId, jcId);
            if (checkUserAuth == 0) {
                return 0;
            } else {
                jjimDao.editJjimCollectionName(postJjimCollectionReq, jcId);
                return 1;
            }
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }


    public int deleteJjimCollection(int userId, int jcId) throws BaseException {
        try {
            int checkUserAuth = jjimDao.checkUserAuth(userId, jcId);
            if (checkUserAuth == 0) {
                return 0;
            } else {
                jjimDao.deleteJjimCollection(jcId);
                return 1;
            }
        }catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);

        }
    }


}
