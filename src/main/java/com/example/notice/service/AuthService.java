package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * 인증 서비스 로직
 */
public interface AuthService {

    /**
     * 사용자 인증 서비스
     *
     * @param member 회원 인증 요청 파라미터
     * @return 성공시 인증 문자열을 반환한다.
     */
    String userAuthentication(Member member);

    /**
     * 관리자 인증 서비스
     *
     * @param member 관리자 인증 요청 파라미터
     * @return 반환할 관리자 정보
     */
    Member adminAuthentication(Member member);

    /**
     * 로그인 아이디 중복 확인 서비스
     *
     * @param loginId 중복 확인하려는 로그인 아이디
     */
    void checkDuplicateLoginId(String loginId);
}
