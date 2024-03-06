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
     * 회원 생성/저장
     * @param member 생성할 회원 정보
     */
    void save(@Param("member") Member member);

    /**
     * 회원 이름 중복 검사
     * @param memberName 검증할 회원 이름
     * @return 참/거짓
     */
    boolean isDuplicateMemberName(@Param("name") String memberName);

    /**
     * 회원 아이디와 비밀번호를 통해 회원 검색
     * @param member 회원 정보
     * @return nullable 회원
     */
    Optional<Member> findMemberByLoginIdAndPassword(@Param("member") Member member);
}
