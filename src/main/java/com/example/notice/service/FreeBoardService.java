package com.example.notice.service;

import com.example.notice.dto.FreeBoardCreateRequest;
import com.example.notice.entity.FreeBoard;

import java.util.List;

/**
 * 자유게시판 서비스
 */
public interface FreeBoardService {

    /**
     * 자유게시판 게시글 생성
     * @param freeBoard 게시글 생성 인자
     * @return 자유게시판 식별자
     */
    Long createFreeBoard(FreeBoard freeBoard);

    /**
     * 식별자에 해당하는 게시글을 가져온다.
     * @param freeBoardId 게시글 식별자
     * @return 게시글 내용
     */
    FreeBoard getBoardById(Long freeBoardId);
}
