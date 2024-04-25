package com.example.notice.auth.provider;

import com.example.notice.entity.Member;

/**
 * 인증에 필요한 암호를 제공하는 클래스
 */
public interface AuthProvider {

    /**
     * 인증 객체를 생성한다.
     * @param member 회원 정보
     * @return 인증 문자열
     */
    public String createAuthentication(Member member);

    /**
     * 인증 객체를 인증한다.
     * @param authentication 인증 문자열
     * @return 인증 객체를 해독해 Member 정보를 가져온다.
     */
    Member verify(String authentication);
}
