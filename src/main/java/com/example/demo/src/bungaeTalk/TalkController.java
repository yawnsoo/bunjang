package com.example.demo.src.bungaeTalk;

import com.example.demo.src.bungaeTalk.model.*;
import com.example.demo.src.oauth.model.PostKakaoLoginRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;
import com.example.demo.src.user.UserDao;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/bungae")
public class TalkController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final TalkProvider talkProvider;
    @Autowired
    private final TalkService talkService;
    @Autowired
    private final JwtService jwtService;




    public TalkController(TalkProvider talkProvider, TalkService talkService, JwtService jwtService){
        this.talkProvider = talkProvider;
        this.talkService = talkService;
        this.jwtService = jwtService;
    }

    /**
     * 번개톡방 만들기 api
     * jwt는 헤더로 전달.
     */
    @Transactional
    @ResponseBody
    @PostMapping("/room/{user_id}")
    public BaseResponse<PostTalkRoomRes> createRoom(@PathVariable("user_id") int user_id,@RequestBody PostTalkRoomReq postTalkRoomReq)
    {
        postTalkRoomReq.setBuyer_id(user_id); //구매자만 채팅방을 만들수있음.

        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = talkProvider.checkUser(user_id);

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //판매자가 탈퇴한 유저인지, 존재하지않은 유저아이디를 적은건지 체크하는 Validation
        try{
            int user_result = talkProvider.checkUser(postTalkRoomReq.getSeller_id());

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_OPPOSITE_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_OPPOSITE_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //이미 존재하는 채팅방이있는지 체크
        try{
            int result = talkProvider.checkRoom(postTalkRoomReq.getBuyer_id(), postTalkRoomReq.getSeller_id()) ;

            if(result ==1)
            {
                return new BaseResponse<>(EXIST_CHAT_ROOM);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


        try{

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }


            PostTalkRoomRes postTalkRoomRes  = talkService.createRoom(postTalkRoomReq);
            return new BaseResponse<>(postTalkRoomRes);

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 번개톡 메시지 생성 api
     * */

    @Transactional
    @ResponseBody
    @PostMapping("/message/{user_id}")
    public BaseResponse<PostTalkMessageRes> createMessage(@PathVariable("user_id") int user_id ,@RequestBody PostTalkMessageReq postTalkMessageReq)
    {
        //메시지를 입력하는 내가 활성화된 회원인지 체크하는 validation
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = talkProvider.checkUser(user_id);

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }



        //room_id를 이용하여 상대방이 방에서 나갔는지 체크하는 validation -> 킵
        //자신 혹은 상대방이 나간방에 메시지를 남기려고했는지 validation ? -> 킵

        //존재하는 room_id를 전달했는지  validation
        try{
            int result = talkProvider.checkRoomByRoomID(postTalkMessageReq.getRoom_id());

            if(result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_CHAT_ROOM);
            }

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //room_id를 이용하여 상대방의 user_id를 얻고 , 상대방이 회원탈퇴했는지 체크하는 validation
        try{
            int user_result = talkProvider.checkAnotherUser(postTalkMessageReq.getRoom_id());

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_OPPOSITE_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_OPPOSITE_USER);
            }

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


        //메시지 생성
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            postTalkMessageReq.setUser_id(user_id);

            PostTalkMessageRes postTalkMessageRes = talkService.createMessage(postTalkMessageReq);
            return new BaseResponse<>(postTalkMessageRes);

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }

    /**
     * 해당 채팅방의 메시지 가져오기 api
     * */
    @ResponseBody
    @GetMapping("/message/{user_id}/{room_id}")
    public BaseResponse<List<GetMessageRes>> getMessages (@PathVariable ("user_id") int user_id, @PathVariable("room_id")int room_id)
    {

        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = talkProvider.checkUser(user_id);

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


        //존재하는 room_id를 전달했는지  validation
        try{
            int result = talkProvider.checkRoomByRoomID(room_id);

            if(result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_CHAT_ROOM);
            }

        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //메시지 조회
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetMessageRes> getMessageRes = talkProvider.getMessages(room_id);
            return new BaseResponse<>(getMessageRes);





        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }



    }

    /**
     * 내가 속한 채팅방 리스트 조회
     * */
    @ResponseBody
    @GetMapping("/room/{user_id}")
    public BaseResponse<List<GetRoomListRes>> getRoomList (@PathVariable("user_id")int user_id)
    {
        //pathvariable로 입력받는 user_id에 대한 validation처리 (활성화된 아이디인지)
        try{
            int user_result = talkProvider.checkUser(user_id);

            if(user_result ==0)
            {
                return new BaseResponse<>(NOT_EXIST_USER);
            }else if(user_result ==-1)
            {
                return new BaseResponse<>(WITHDRAW_USER);
            }
        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }

        //내가속한 채팅방 리스트 조회.
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(user_id != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetRoomListRes> getRoomListRes = talkProvider.getRoomList(user_id);

            return new BaseResponse<>(getRoomListRes);





        }catch (BaseException exception)
        {
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }


    }










}
