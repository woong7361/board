package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * 회원 서비스
 */
public interface MemberService {

    /**
     * 회원 생성
     *
     * @param member 회원 생성 요청 파라미터
     */
    void createUserRoleMember(Member member);
}
