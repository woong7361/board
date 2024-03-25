package com.example.notice.repository;

import com.example.notice.entity.InquireBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 문의 게시판 repository
 */
@Mapper
public interface InquireBoardRepository {

    /**
     * 문의 게시판 게시글 생성
     *
     * @param inquireBoard 문의 게시판 게시글 생성 파라미터
     * @param memberId 게시글 작성자의 식별자
     * @return 문의 게시판 게시글 식별자
     */
    Long save(@Param("inquireBoard") InquireBoard inquireBoard, @Param("memberId") Long memberId);
}
