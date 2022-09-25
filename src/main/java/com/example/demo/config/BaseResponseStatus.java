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

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2036,"중복된 이메일입니다."),
    POST_USERS_EMPTY_LOGIN_ID(false, 2017, "아이디를 입력해주세요."),
    POST_USERS_EXISTS_LOGIN_ID(false,2037,"중복된 아이디입니다."),
    POST_USERS_EMPTY_PASSWORD(false, 2018, "패스워드를 입력해주세요."),
    POST_USERS_EMPTY_NAME(false, 2019, "이름을 입력해주세요."),
    POST_USERS_EMPTY_PHONE_NUMBER(false, 2020, "전화번호를 입력해주세요."),
    POST_USERS_EMPTY_NICKNAME(false, 2021, "닉네임을 입력해주세요."),

    POST_USERS_EXISTS_NICKNAME(false,2031,"중복된 닉네임입니다."),

    post_MARKETS_invalid_category_name(false,2032,"유효하지않은 카테고리이름입니다."),

    //Status
    Check_Status(false,2050,"Status 값을 다시 한번 확인해주세요(0:판매완료 1:판매중 2:예약중 3:광고중)."),

    //Safepay
    Check_Safepay(false,2060,"Safepay 값을 다시 한번 확인해주세요(1:페이 가능)."),

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

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USER_NICKNAME(false,4014,"유저닉네임 수정 실패"),
    MODIFY_FAIL_USER_IMAGE(false,4015,"유저이미지 수정 실패"),
    MODIFY_FAIL_USER_PASSWORD(false,4016,"유저패스워드 수정 실패"),
    MODIFY_FAIL_USER_PHONE_NUMBER(false,4017,"유저휴대폰번호 수정 실패"),
    MODIFY_FAIL_USER_AGREE_ON_MAIL(false,4018,"유저 메일수신동의 수정 실패"),
    MODIFY_FAIL_USER_AGREE_ON_SMS(false,4019,"유저 메일수신동의 수정 실패"),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),


    DELETE_USER_ERROR(false,4100,"회원탈퇴에 실패하였습니다."),

    DELETE_ADDRESS_ERROR(false,4101,"주소삭제에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
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
