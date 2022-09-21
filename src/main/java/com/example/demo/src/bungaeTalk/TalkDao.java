package com.example.demo.src.bungaeTalk;

import com.example.demo.src.bungaeTalk.model.*;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TalkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




    @Transactional
    public PostTalkRoomRes createRoom(PostTalkRoomReq postTalkRoomReq)
    {
//        String selectSellerQuery = "select user_id from post where post_id = ?";
//        int selectSellerParam = postTalkRoomReq.getPost_id();
//
//        int seller_id = this.jdbcTemplate.queryForObject(selectSellerQuery,int.class,selectSellerParam);
        int seller_id = postTalkRoomReq.getSeller_id();

        String createRoomQuery = "insert into chatting_room(buyer_id,seller_id) VALUES(?,?)";
        Object[] createRoomParams = new Object[]{postTalkRoomReq.getBuyer_id(),seller_id};

        this.jdbcTemplate.update(createRoomQuery,createRoomParams);
        String roomIDQuery = "select last_insert_id()";
        int room_id = this.jdbcTemplate.queryForObject(roomIDQuery,int.class);

        //방을 생성해서 받은 룸아이디를 이용해서 맨처음 받은 메시지도 메시지 테이블에 추가해준다
        String messageCreateQuery = "insert into chat(user_id,room_id,message) VALUES(?,?,?)";
        Object[] messageCreateParams = new Object[]{postTalkRoomReq.getBuyer_id(),room_id,postTalkRoomReq.getMessage()};

        this.jdbcTemplate.update(messageCreateQuery,messageCreateParams); //메시지 생성.


        PostTalkRoomRes postTalkRoomRes = new PostTalkRoomRes(room_id, postTalkRoomReq.getBuyer_id(),seller_id);
        return  postTalkRoomRes;

    }

    //해당 post_id가 존재하는 post_id인지 체크.
    public int checkPost(int post_id)
    {
        String checkPostQuery ="select exists(select post_id from post where post_id =?)";
        return this.jdbcTemplate.queryForObject(checkPostQuery,int.class,post_id);
    }

    public int checkUserStatus(int user_id) // 존재하는 유저인지 , 탈퇴한 유저인지 체크하는 함수.
    //없는유저라면 0, 탈퇴한유저라면 -1 , 존재하는 유저라면 해당 유저아이디를 리턴해준다.
    {
        String existUserQuery = "select user_id from user where user_id = ?";
        String result = "";
        try{
            result = this.jdbcTemplate.queryForObject(existUserQuery,String.class,user_id);
        }
        catch (EmptyResultDataAccessException e)
        {
            result ="empty";
        }
        if(result.equals("empty")) //해당 정보를 가지고있는 유저가없어 catch문에 걸려서 empty가 저장됬다면.
        {
            return 0; //0을 주어 그런유저가 없다는것을 알린다.
        }
        else{ // 아니면 해당 유저의 index를 줌, 그리고 그 유저의 status를 체크한다.0이면 1로바꿔주고 로그인 시켜줌.

            String checkStatusQuery = "select status from user where user_id =?";
            int checkStatusParam = Integer.parseInt(result);

            if(this.jdbcTemplate.queryForObject(checkStatusQuery,int.class,checkStatusParam) == 0)
            { //탈퇴한 회원이라 status가 0이라면
                return -1; // -1을 리턴하여 탈퇴한 회원임을 알린다. ( 재회원가입을 유도하고 , 상점명만 바꿔주고 , status바꿔주면될듯)
            }

            return Integer.parseInt(result);
        }
    }
    public int checkAnotherUserStatus(int room_id) // 대화하는 상대방이 존재하는 유저인지 , 탈퇴한 유저인지 체크하는 함수.
    //없는유저라면 0, 탈퇴한유저라면 -1 , 존재하는 유저라면 해당 유저아이디를 리턴해준다.
    {
        String existUserQuery = "select seller_id from chatting_room where room_id = ?";
        String result = "";
        try{
            result = this.jdbcTemplate.queryForObject(existUserQuery,String.class,room_id);
        }
        catch (EmptyResultDataAccessException e)
        {
            result ="empty";
        }
        if(result.equals("empty")) //해당 정보를 가지고있는 유저가없어 catch문에 걸려서 empty가 저장됬다면.
        {
            return 0; //0을 주어 그런유저가 없다는것을 알린다.
        }
        else{ // 아니면 해당 유저의 index를 줌, 그리고 그 유저의 status를 체크한다.0이면 1로바꿔주고 로그인 시켜줌.

            String checkStatusQuery = "select status from user where user_id =?";
            int checkStatusParam = Integer.parseInt(result);

            if(this.jdbcTemplate.queryForObject(checkStatusQuery,int.class,checkStatusParam) == 0)
            { //탈퇴한 회원이라 status가 0이라면
                return -1; // -1을 리턴하여 탈퇴한 회원임을 알린다. ( 재회원가입을 유도하고 , 상점명만 바꿔주고 , status바꿔주면될듯)
            }

            return Integer.parseInt(result);
        }
    }

//    public int checkOppositeExitRoom(int room_id,int user_id) // 대화 상대방이 나갔는지 확인하는 쿼리 ,방아아디,내아이디를 전달
//    {
//        String checkOppositeExitRoomQuery = "select buyer_id from chatting_room where room_id =?";
//        int result = this.jdbcTemplate.queryForObject(checkOppositeExitRoomQuery,int.class,room_id);
//
//        if(result == user_id) //메시지를 보낸 유저가 판매자이라는뜻 이므로 상대방은 구매자이다.
//        { // 구매자의 status를 체크해서 방을 나갔는지 확인
//            String checkUserInRoomQuery = "select buyer_status from chatting_room where room_id = ?";
//            int result0 = this.jdbcTemplate.queryForObject(checkUserInRoomQuery,int.class,room_id);
//
//        }
//
//    }

    //이미 생성된 채팅방이 있는지 확인
    public int existRoom(int buyer_id,int seller_id){
        String existRoomQuery = "select exists(select room_id from chatting_room where buyer_id =? and seller_id =?)";
        Object[] existRoomParams = new Object[]{buyer_id,seller_id};

        int result = this.jdbcTemplate.queryForObject(existRoomQuery,int.class,existRoomParams);

        String existRoomQuery2 = "select exists(select room_id from chatting_room where buyer_id =? and seller_id =?)";
        Object[] existRoomParams2 = new Object[]{seller_id,buyer_id};

        int result2 = this.jdbcTemplate.queryForObject(existRoomQuery2,int.class,existRoomParams2); //그 반대도 체크


        if(result == 1 || result2 == 1){
            return 1;
        }
        else{
            return 0;
        }
    }


    //메시지생성 함수
    @Transactional
    public PostTalkMessageRes createMessage(PostTalkMessageReq postTalkMessageReq)
    {
        String messageCreateQuery = "insert into chat(user_id,room_id,message) VALUES(?,?,?)";
        Object[] messageCreateQueryParams = new Object[]{postTalkMessageReq.getUser_id(),postTalkMessageReq.getRoom_id(),postTalkMessageReq.getMessage()};

        this.jdbcTemplate.update(messageCreateQuery,messageCreateQueryParams);

        String messageIDQuery = "select last_insert_id()";

        int chat_id = this.jdbcTemplate.queryForObject(messageIDQuery,int.class);

        PostTalkMessageRes postTalkMessageRes = new PostTalkMessageRes(chat_id,postTalkMessageReq.getUser_id(),postTalkMessageReq.getRoom_id(),postTalkMessageReq.getMessage());
        return postTalkMessageRes;
    }

    public int checkRoomByRoomId(int room_id)
    {
        String checkRoomQuery = "select exists(select room_id from chatting_room where room_id = ?)";
        int result = this.jdbcTemplate.queryForObject(checkRoomQuery,int.class,room_id);
        return result;
    }


    //채팅방 메시지조회
    public List<GetMessageRes> getMessages(int room_id)
    {
        String getMessagesQuery = "select user_id,message,created_at from chat where room_id = ?";
        return this.jdbcTemplate.query(getMessagesQuery,
                (rs, rowNum) -> new GetMessageRes(
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at")
                ),room_id);

    }

    //내가 속한 채팅방 조회
    public List<GetRoomListRes> getRoom(int user_id)
    {
        String getRoomQuery = ""
            + "SELECT c.room_id    AS room_id, "
            + "       u.name       AS 상대방이름, "
            + "       u.image_path AS image_path, "
            + "       (SELECT message "
            + "        FROM   chat "
            + "        WHERE  room_id = c.room_id "
            + "        ORDER  BY created_at DESC "
            + "        LIMIT  1)   AS last_message, "
            + "       (SELECT created_at "
            + "        FROM   chat "
            + "        WHERE  room_id = c.room_id "
            + "        ORDER  BY created_at DESC "
            + "        LIMIT  1)   AS last_message_time "
            + "FROM   chatting_room AS c "
            + "       LEFT JOIN user u "
            + "              ON u.user_id = c.seller_id "
            + "WHERE  buyer_id = ? "
            + "UNION "
            + "SELECT c.room_id    AS room_id, "
            + "       u.name       AS 상대방이름, "
            + "       u.image_path AS image_path, "
            + "       (SELECT message "
            + "        FROM   chat "
            + "        WHERE  room_id = c.room_id "
            + "        ORDER  BY created_at DESC "
            + "        LIMIT  1)   AS last_message, "
            + "       (SELECT created_at "
            + "        FROM   chat "
            + "        WHERE  room_id = c.room_id "
            + "        ORDER  BY created_at DESC "
            + "        LIMIT  1)   AS last_message_time "
            + "FROM   chatting_room AS c "
            + "       LEFT JOIN user u "
            + "              ON u.user_id = c.buyer_id "
            + "WHERE  seller_id = ?";

        Object[] getRoomParams = new Object[]{user_id,user_id};

        return this.jdbcTemplate.query(getRoomQuery,
                (rs, rowNum) -> new GetRoomListRes(
                        rs.getInt("room_id"),
                        rs.getString("상대방이름"),
                        rs.getString("image_path"),
                        rs.getString("last_message"),
                        rs.getTimestamp("last_message_time")
                ),getRoomParams);

    }


}
