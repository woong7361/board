package com.example.notice.service;

import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.FreeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 자유게시판 서비스
 */
public interface FreeBoardService {

    /**
     * 자유게시판 게시글 생성
     *
     * @param freeBoard 게시글 생성 인자
     * @param files 게시글 첨부파일들
     * @param memberId 게시글 생성자
     * @return 자유게시판 게시글 식별자
     */
    Long createFreeBoard(FreeBoard freeBoard, List<MultipartFile> files, Long memberId);

    /**
     * 자유게시판 게시글 조회
     *
     * @param freeBoardId 게시글 식별자
     * @return 게시글 내용
     */
    FreeBoard getBoardById(Long freeBoardId);

    /**
     * 자유게시판 게시글 검색
     *
     * @param freeBoardSearchDTO 게시글 검색 요청 파라미터
     * @param pageRequest 게시글 페이지네이션 요청 파라미터
     * @return 게시글 페이지
     */
    PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest);

    /**
     * 자유게시판의 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     * @param memberId 삭제 요청하는 회원 식별자
     */
    void deleteFreeBoardById(Long freeBoardId, Long memberId);

    /**
     * 자유게시판 게시글 수정
     *
     * @param freeBoard 게시글 수정 파라미터
     * @param saveFiles 추가할 첨부파일들
     * @param deleteFileIds 삭제할 첨부파일 식별자들
     * @param freeBoardId 게시글 식별자
     * @param memberId 수정 요청하는 회원 식별자
     */
    void updateFreeBoardById(FreeBoard freeBoard, List<MultipartFile> saveFiles, List<Long> deleteFileIds, Long freeBoardId, Long memberId);

    /**
     * 관리자의 자유게시판 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     * @param memberId 관리자 식별자
     */
    void deleteFreeBoardByAdmin(Long freeBoardId, Long memberId);

    /**
     * 자유게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    List<String> getCategories();

}
