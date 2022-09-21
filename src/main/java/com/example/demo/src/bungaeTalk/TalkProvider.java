package com.example.demo.src.bungaeTalk;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.bungaeTalk.model.GetMessageRes;
import com.example.demo.src.bungaeTalk.model.GetRoomListRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class TalkProvider {
    private final TalkDao talkDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TalkProvider(TalkDao talkDao, JwtService jwtService) {
        this.talkDao = talkDao;
        this.jwtService = jwtService;
    }

    //해당 post_id가 존재하는 post_id인지 체크
    public int checkPost(int post_id) throws BaseException
    {
        try{
            return talkDao.checkPost(post_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUser(int user_id) throws BaseException
    {
        try{
            return talkDao.checkUserStatus(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkAnotherUser(int room_id) throws BaseException
    {
        try{
            return talkDao.checkUserStatus(room_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRoom(int buyer_id,int seller_id) throws BaseException
    {
        try{
            return talkDao.existRoom(buyer_id,seller_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRoomByRoomID(int room_id) throws BaseException
    {
        try{
            return talkDao.checkRoomByRoomId(room_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //해당 채팅방 메시지 조회
    public List<GetMessageRes> getMessages(int room_id)throws BaseException
    {
        try{
            return talkDao.getMessages(room_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }


    //내가 속한 채팅방 리스트 조회
    public List<GetRoomListRes> getRoomList(int user_id)throws BaseException
    {
        try{
            return talkDao.getRoom(user_id);

        }catch (Exception exception)
        {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }




}
