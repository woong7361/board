package com.example.notice.auth.provider;

import com.example.notice.entity.Member;

/**
 * 인증에 필요한 객체에 대한 암호화&복호화 를 제공하는 클래스
 */
public interface AuthProvider {

    /**
     * 암호화된 인증 객체를 생성한다.
     *
     * @param member 인증 객체에 담길 회원 정보
     * @return 암호화된 인증 문자열
     */
    public String createAuthentication(Member member);

    /**
     * 인증 객체를 복호화하여 검증한다.
     * @param authentication 암호화된 인증 문자열
     * @return 검증에 성공하면 인증 문자열에 담긴 정보를 반환한다.
     */
    Member verify(String authentication);
}
