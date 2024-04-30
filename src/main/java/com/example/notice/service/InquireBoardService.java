package com.example.notice.service;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
import com.example.notice.entity.InquireBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;

/**
 * 문의 게시판 서비스
 */
public interface InquireBoardService {

    /**
     * 문의 게시글 생성
     *
     * @param inquireBoard 문의 게시글 생성 요청 파라미터
     * @return 생성된 문의 게시글 식별자
     */
    Long createBoard(InquireBoard inquireBoard, Long memberId);


    /**
     * 문의 게시판 검색
     *
     * @param inquireBoardSearchDTO 문의 게시판 검색 요청 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 검색 결과
     */
    PageResponse<InquireBoardSearchResponseDTO> searchInquireBoard(InquireBoardSearchDTO inquireBoardSearchDTO, PageRequest pageRequest);

    /**
     * 문의 게시글 조회
     *
     * @param inquireBoardId 문의 게시글 식별자
     * @param memberId 회원 식별자
     * @return 문의 게시글
     */
    InquireBoardResponseDTO getBoardById(Long inquireBoardId, Long memberId);

    /**
     * 문의 게시글 수정
     *
     * @param inquireBoard 문의 게시글 수정 파라미터
     * @param inquireBoardId 문의 게시글 식별자
     * @param memberId 게시글 수정 요청자의 식별자
     */
    void updateById(InquireBoard inquireBoard, Long inquireBoardId, Long memberId);

    /**
     * 문의 게시글 삭제
     *
     * @param inquireBoardId 삭제할 문의 게시글 식별자
     * @param memberId 삭제 요청하는 회원 식별자
     */
    void deleteById(Long inquireBoardId, Long memberId);

    /**
     * 관리자의 문의 게시글 삭제
     *
     * @param inquireBoardId 삭제할 문의 게시글 식별자
     */
    void deleteByAdmin(Long inquireBoardId);

    /**
     * 관리자의 문의 게시글 조회
     *
     * @param inquireBoardId 게시글 식별자
     * @return 문의 게시글
     */
    InquireBoardResponseDTO getBoardByAdmin(Long inquireBoardId);
}
