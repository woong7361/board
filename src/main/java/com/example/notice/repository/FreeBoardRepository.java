package com.example.notice.repository;

import com.example.notice.entity.FreeBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 게시글의 첨부파일 연관관계 저장
     * @param freeBoardId 게시글 식별자
     * @param fileIds 첨부파일 식별자들
     */
    void saveFileIds(@Param("freeBoardId") Long freeBoardId, @Param("fileId") Long fileIds);
}
