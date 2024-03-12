package com.example.notice.repository;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 자유 게시판 repository
 */
@Mapper
public interface FreeBoardRepository {
    /**
     * 게시글 저장
     * @param freeBoard 자유게시판 게시글
     */
    void save(@Param("freeBoard") FreeBoard freeBoard);

    /**
     * 게시글 식별자에 맞는 게시글 검색
     * @param freeBoardId 게시글 식별자
     * @return 게시글
     */
    Optional<FreeBoard> findBoardById(@Param("freeBoardId") Long freeBoardId);

    /**
     * 게시글 검색조건에 맞는 게시글들 검색
     * @param freeBoardSearchParam 게시글 검색 조건 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 게시글들
     */
    List<FreeBoard> findBoardsBySearchParam(@Param("search") FreeBoardSearchParam freeBoardSearchParam, @Param("page") PageRequest pageRequest);

    /**
     * 게시글 검색조건에 맞는 게시글의 총 개수를 가져온다.
     * @param freeBoardSearchParam 게시글 검색 조건 파라미터
     * @return 조건에 맞는 게시글의 총 개수
     */
    Integer getTotalCountBySearchParam(@Param("search") FreeBoardSearchParam freeBoardSearchParam);

    /**
     * 게시글 조회수를 올린다.
     * @param freeBoardId 게시글 식별자
     */
    void increaseViewsByBoardId(@Param("freeBoardId") Long freeBoardId);
}
