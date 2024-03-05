package com.example.notice.service;

import com.example.notice.entity.Member;

/**
 * 회원에 관련된 서비스 클래스
 */
public interface MemberService {

    /**
     * 회원 생성
     */
    public void createUserRoleMember(Member member);

    /**
     * 회원 이름 중복 검사
     * @param memberName 검사할 회원 이름
     */
    public void isDuplicateMemberName(String memberName);
}
