package com.example.notice.service;

import com.example.notice.dto.request.InquireBoardSearchDTO;
import com.example.notice.dto.response.InquireBoardResponseDTO;
import com.example.notice.dto.response.InquireBoardSearchResponseDTO;
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
     * @param inquireBoardSearchDTO 문의 게시판 검색 파라미터
     * @param pageRequest 페이지 요청 파라미터
     * @return 검색 결과
     */
    PageResponse<InquireBoardSearchResponseDTO> searchInquireBoard(InquireBoardSearchDTO inquireBoardSearchDTO, PageRequest pageRequest);

    /**
     * 문의 게시판 게시글 상세보기
     *
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param memberId 요청한 사용자 식별자
     * @return 해당하는 게시글의 내용
     */
    InquireBoardResponseDTO getBoardById(Long inquireBoardId, Long memberId);

    /**
     * 문의 게시판 게시글 수정
     * @param inquireBoard 문의 게시판 게시글 수정 파라미터
     * @param inquireBoardId 문의 게시판 게시글 식별자
     * @param memberId 게시글 수정 요청자의 식별자
     */
    void updateById(InquireBoard inquireBoard, Long inquireBoardId, Long memberId);

    /**
     * 문의 게시판 게시글 삭제
     * @param inquireBoardId 삭제할 문의 게시글 식별자
     * @param memberId 삭제 요청하는 회원 식별자
     */
    void deleteById(Long inquireBoardId, Long memberId);

    /**
     * 문의 게시판 게시글 관리자에 의해 삭제
     * @param inquireBoardId 삭제할 문의 게시글 식별자
     */
    void deleteByAdmin(Long inquireBoardId);

    /**
     * 관리자의 문의 게시판 게시글 조회
     * @param inquireBoardId 게시글 식별자
     * @return 게시글 조회 결과
     */
    InquireBoardResponseDTO getBoardByAdmin(Long inquireBoardId);
}
