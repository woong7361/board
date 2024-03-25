package com.example.notice.service;

import com.example.notice.entity.InquireBoard;

/**
 * 문의 게시판 게시글 서비스 로직
 */
public interface InquireBoardService {

    /**
     * 문의 게시판 게시글 생성
     * @param inquireBoard 게시판 게시글 요청 파라미터
     * @return 문의 게시글 식별자
     */
    Long createBoard(InquireBoard inquireBoard, Long memberId);
}
