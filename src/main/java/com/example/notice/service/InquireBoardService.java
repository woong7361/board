package com.example.notice.service;

import com.example.notice.dto.InquireBoardSearchParam;
import com.example.notice.dto.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;

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


    /**
     * 문의 게시판 검색
     *
     * @param inquireBoardSearchParam 문의 게시판 검색 파라미터
     * @param pageRequest             페이지 요청 파라미터
     * @param memberId                검색 회원 식별자
     * @return 검색 결과
     */
    PageResponse<InquireBoardSearchResponseDTO> searchInquireBoard(InquireBoardSearchParam inquireBoardSearchParam, PageRequest pageRequest, Long memberId);
}