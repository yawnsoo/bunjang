package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    NOT_EXIST_USER(false,2004,"존재하지않는 유저입니다."),
    WITHDRAW_USER(false,2005,"탈퇴한 유저입니다."),
    WRONG_AUTH_NUMBER(false,2006,"잘못된 인증번호입니다."),
    NOT_EXIST_POST_ID(false,2007,"존재하지 POST_ID(판매글아이디)입니다."),
    NOT_EXIST_OPPOSITE_USER(false,2008,"상대방이 존재하지않는 유저입니다."),
    WITHDRAW_OPPOSITE_USER(false,2009,"상대방은 탈퇴한 유저입니다."),
    EXIST_CHAT_ROOM(false, 2010, "이미 채팅방이 존재합니다."),
    NOT_EXIST_CHAT_ROOM(false, 2011, "존재하지않는 방아이디입니다."),
    CANT_START_MINUS(false, 2012, "포인트 기록이없습니다. 포인트 변화량을 마이너스로 시작할수없습니다."),
    OVER_POINT(false, 2013, "가지고 있는 포인트보다 더많이 사용할수없습니다."),
    STATUS_ZERO_POST(false, 2014, "판매완료된 글(status ==0)입니다."),
    CANT_ZERO_POINT_USE(false, 2015, "0포인트는 추가,사용 될수없습니다."),
    CANT_FOLLOW_MYSELF(false, 2016, "자기자신을 팔로우할 수 없습니다."),
    EMPTY_SEARCH_WORDS(false, 2017, "검색어를 입력해주세요"),



    QUERY_STRING_ERROR(false,2030,"Parameter값을 확인해 주세요."),

    // POST user info EMPTY 2010~
    USERS_EMPTY_USER_NAME(false, 2010, "이름을 입력해주세요."),
    USERS_EMPTY_USER_PHONE_NUMBER(false, 2011, "전화번호를 입력해주세요."),
    USERS_EMPTY_USER_REGISTRATION_NUMBER(false, 2012, "주민등록번호를 입력해주세요."),
    USERS_EMPTY_USER_MOBILE_CARRIER_ID(false, 2013, "통신사를 선택해주세요."),
    USERS_EMPTY_USER_MARKET_NAME(false, 2014, "상점이름을 입력해주세요."),

    //Post
    POST_EMPTY_IMG(false,2040,"이미지를 입력해주세요."),
    POST_EMPTY_TITLE(false,2041,"제목을 입력해주세요."),
    POST_SHORT_TITLE(false,2042,"제목을 2글자 이상 입력해주세요."),
    POST_LOW_PRICE(false,2043,"가격을 100원 이상 입력해주세요."),
    POST_SHORT_CONTENT(false,2044,"본문을 10글자 이상 입력해주세요."),
    POST_LONG_CONTENT(false,2045,"본문을 2000글자 이하로 입력해주세요."),
    POST_RANGE_COUNT(false,2046,"수량을 1개 이상 999개 이하로 입력해주세요."),

    RELATION_ERROR_LM(false,2047,"대분류와 중분류 관계를 다시 확인해주세요."),
    RELATION_ERROR_MS(false,2048,"중분류와 소분류 관계를 다시 확인해주세요."),
    ERASE_LARGEorSMALL_CATEGORY_ID(false,2049,"대분류ID 혹은 소분류ID를 지우고 다시 시도해주세요."),

    //Status
    Check_Status(false,2050,"Status 값을 다시 한번 확인해주세요(0:판매완료 1:판매중 2:예약중 3:광고중)."),


    // Post user info 잘못된 입력 2100 ~
    USERS_WRONG_USER_NAME(false, 2100, "이름이 잘못 입력되었습니다."),
    USERS_WRONG_PHONE_NUMBER(false, 2101, "휴대폰번호가 잘못 입력되었습니다."),
    USERS_WRONG_MARKET_NAME(false, 2102, "상점이름이 잘못 입력되었습니다."),


    //Post user info 중복값 입력 2200 ~
    USERS_EXIST_MARKET_NAME(false, 2200, "존재하는 상점이름입니다."),


    //Safepay
    Check_Safepay(false,2060,"Safepay 값을 다시 한번 확인해주세요(1:페이 가능)."),

    COLLECTION_LARGE_NAME(false,2070,"제목을 10글자 이하로 입력해주세요."),
    INVALID_USER_COLLECTION(false,2071,"컬렉션에 접근 권한이 없는 사용자입니다."),
    WRONG_COLLECTION_JJIMID(false,2072,"해당 컬렉션에는 해당 판매글이 존재하지 않습니다."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    //[POST] /users/login
    Withdrawal_USER(false,3015,"탈퇴한 회원입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),

    MODIFY_FAIL_JJIM_COLLECTION(false,4030,"jjim_id를 다시 확인해주세요"),
    DELETE_FAIL_JJIM(false,4031,"post_id를 다시 확인해주세요"),

    DELETE_USER_ERROR(false,4100,"회원탈퇴에 실패하였습니다."),

    DELETE_ADDRESS_ERROR(false,4101,"주소삭제에 실패하였습니다."),


    // 5000 : 필요시 만들어서 쓰세요
    NOT_MY_COLLECTION(false,5000,"본인이 생성한 컬렉션이 아닙니다.");
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
