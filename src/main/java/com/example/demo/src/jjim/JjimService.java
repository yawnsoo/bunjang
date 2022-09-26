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

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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


    public void modifyJjimPostCollection(PatchPostCollectionReq patchPostCollectionReq) throws BaseException {

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

}
