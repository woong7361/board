package com.example.notice.repository;

import com.example.notice.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 회원 repository
 */
@Mapper
public interface MemberRepository {

    /**
     * 회원 저장
     *
     * @param member 저장할 회원 정보
     */
    void save(@Param("member") Member member);

    /**
     * 회원 아이디 중복 검사
     *
     * @param loginId 검증할 회원 아이디
     * @return 참/거짓
     */
    boolean isDuplicateMemberLoginId(@Param("loginId") String loginId);

    /**
     * 회원 아이디와 비밀번호를 통해 회원 조회
     *
     * @param member 회원 요청 정보
     * @return 회원
     */
    Optional<Member> findMemberByLoginIdAndPassword(@Param("member") Member member);

    /**
     * 아이디와 비밀번호를 통해 관리자 검색
     *
     * @param member 관리자 정보
     * @return 관리자
     */
    Optional<Member> findAdminMemberByLoginIdAndPassword(@Param("member") Member member);

    /**
     * 로그인 아이디로 회원 관리자 모두 조회
     *
     * @param loginId 로그인 아이디
     */
    Optional<Member> findMemberAndAdminByLoginId(@Param("loginId") String loginId);
}
