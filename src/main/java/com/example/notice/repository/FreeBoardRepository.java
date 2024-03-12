package com.example.notice.repository;

import com.example.notice.entity.FreeBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    Optional<FreeBoard> findBoardById(@Param("freeBoardId") Long freeBoardId);
}
