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
     * 식별자에 해당하는 게시글을 가져온다.
     * @param freeBoardId 게시글 식별자
     * @return 게시글 내용
     */
    FreeBoard getBoardById(Long freeBoardId);

    /**
     * 검색조건에 해당하는 게시글을 가져온다.
     * @param freeBoardSearchDTO 게시글 검색 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 게시글 페이지
     */
    PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest);

    /**
     * 자유게시판의 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteFreeBoardById(Long freeBoardId);

    /**
     * 자유게시판 게시글 수정
     *
     * @param freeBoard     게시글 수정 인자
     * @param saveFiles 추가할 첨부파일들
     * @param deleteFileIds 삭제할 첨부파일 식별자들
     * @param freeBoardId   게시글 식별자
     */
    void updateFreeBoardById(FreeBoard freeBoard, List<MultipartFile> saveFiles, List<Long> deleteFileIds, Long freeBoardId);

    /**
     * 자유게시판 게시글의 권한 인증
     * @param freeBoardId 게시글 식별자
     * @param memberId 사용자 식별자
     */
    void checkFreeBoardAuthorization(Long freeBoardId, Long memberId);

    /**
     * 관리자의 자유게시판 게시글 삭제
     * @param freeBoardId 게시글 식별자
     * @param memberId 관리자 식별자
     */
    void deleteFreeBoardByAdmin(Long freeBoardId, Long memberId);

    /**
     * 자유게시판 카테고리 가져오기
     * @return 카테고리 리스트
     */
    List<String> getCategory();

}
