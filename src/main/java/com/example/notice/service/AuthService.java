package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * 인증과 관련된 서비스 로직
 */
public interface AuthService {

    /**
     * 로그인 시도
     * @param member 회원 정보
     * @return 성공시 jwt token을 반환한다
     */
    String login(Member member);

}
