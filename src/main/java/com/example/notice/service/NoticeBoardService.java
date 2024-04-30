package com.example.notice.service;

import com.example.notice.dto.request.NoticeBoardSearchDTO;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;

import java.util.List;


/**
 * 공지 게시판 서비스
 */
public interface NoticeBoardService {

    /**
     * 공지 게시글 생성
     *
     * @param noticeBoard 공지 게시글 생성 파라미터
     * @param memberId 작성자 식별자
     * @return 생성된 공지 게시판 식별자
     */
    Long createNoticeBoard(NoticeBoard noticeBoard, Long memberId);

    /**
     * 상단 고정 공지 게시글 조회
     *
     * @return 상단 고정된 공지 게시글들
     */
    List<NoticeBoard> getFixedNoticeBoardWithoutContent();

    /**
     * 공지 게시글 검색
     *
     * @param noticeBoardSearchDTO 공지글 검색 요청 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 검색 결과
     */
    PageResponse<NoticeBoard> getNoneFixedNoticeBoardSearch(NoticeBoardSearchDTO noticeBoardSearchDTO, PageRequest pageRequest);

    /**
     * 공지 게시글 조회
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @return 공지 게시글
     */
    NoticeBoard getNoticeBoardById(Long noticeBoardId);

    /**
     * 공지 게시글 삭제
     *
     * @param noticeBoardId 공지 게시글 식별자
     */
    void deleteNoticeBoardById(Long noticeBoardId);

    /**
     * 공지 게시글 수정
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @param noticeBoard 공지 게시글 수정 요청 파라미터
     */
    void updateNoticeBoardById(Long noticeBoardId, NoticeBoard noticeBoard);

    /**
     * 공지 게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    List<String> getCategory();
}
