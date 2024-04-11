package com.example.notice.constant;

/**
 * 에러 메시지 상수 클래스
 */
public class ErrorMessageConstant {

    public static final String NOT_SESSION_MESSAGE = "세션이 없습니다.";
    public static final String INVALID_SESSION_MESSAGE = "잘못된 세션입니다.";

    public static final String BEYOND_MAX_SEARCH_RANGE_MESSAGE = "최대 날짜 검색 범위를 넘어섰습니다.";

    public static final String LOGIN_ID_MUST_NOT_SAME_AS_PASSWORD_MESSAGE = "로그인 아이디와 비밀번호가 같으면 안됩니다.";
    public static final String DUPLICATE_LOGIN_ID_MESSAGE = "중복된 로그인 아이디가 존재합니다.";

    public static final String MEMBER_NOT_EXIST_MESSAGE = "해당하는 회원이 존재하지 않습니다.";
    public static final String FILE_NOT_EXIST_MESSAGE = "해당하는 파일이 존재하지 않습니다.";
    public static final String BOARD_NOT_EXIST_MESSAGE = "해당하는 게시글이 존재하지 않습니다.";

    public static final String AUTHORIZATION_EXCEPTION_MESSAGE = "해당 작업에 권한이 부족합니다.";

    public static final String NOT_ALLOWED_FILE_EXTENSION_MESSAGE = "허용되지 않은 파일 확장자 입니다.";
    public static final String FILE_IO_EXCEPTION_MESSAGE = "파일 I/O 관련 문제입니다.";

}
