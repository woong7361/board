package com.example.notice.service;

import com.example.notice.dto.FreeBoardSearchParam;
import com.example.notice.entity.FreeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;

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

    /**
     * 검색조건에 해당하는 게시글을 가져온다.
     * @param freeBoardSearchParam 게시글 검색 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 게시글 페이지
     */
    PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchParam freeBoardSearchParam, PageRequest pageRequest);

    /**
     * 자유게시판의 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteFreeBoardById(Long freeBoardId);

    /**
     * 자유게시판 게시글 수정
     *
     * @param freeBoard   게시글 수정 인자
     * @param freeBoardId 게시글 식별자
     */
    void updateFreeBoardById(FreeBoard freeBoard, Long freeBoardId);

    /**
     * 자유게시판 게시글의 권한 인증
     * @param freeBoardId 게시글 식별자
     * @param memberId 사용자 식별자
     */
    void checkFreeBoardAuthorization(Long freeBoardId, Long memberId);
}
