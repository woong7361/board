package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * 인증과 관련된 서비스 로직
 */
public interface AuthService {

    /**
     * 사용자 인증 서비스
     * @param member 회원 정보
     * @return 성공시 jwt token을 반환한다
     */
    String userAuthentication(Member member);

    /**
     * 관리자 인증 서비스
     *
     * @param member 관리자 정보
     * @return 관리자 정보
     */
    Member adminAuthentication(Member member);

    /**
     * 로그인 아이디 중복 확인
     * @param loginId 중복 확인하려는 로그인 아이디
     */
    void checkDuplicateLoginId(String loginId);
}
