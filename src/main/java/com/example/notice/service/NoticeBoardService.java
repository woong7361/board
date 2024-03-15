package com.example.notice.service;

import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;

import java.util.List;


/**
 * 공지 게시판 서비스
 */
public interface NoticeBoardService {

    /**
     * 공지 게시판 생성 서비스
     * @param noticeBoard 공지 게시판
     * @param memberId 관리자 식별자
     * @return 생성된 공지 게시판 식별자
     */
    Long createNoticeBoard(NoticeBoard noticeBoard, Long memberId);

    /**
     * 상단 고정된 공지 게시글들을 가져온다.
     * @return 상단 고정된 공지 게시글들
     */
    List<NoticeBoard> getFixedNoticeBoardWithoutContent();

    /**
     * 고정 공지글이 아닌 공지글들을 반환
     *
     * @param noticeBoardSearchParam 공지글 검색 파라미터
     * @param pageRequest            페이지 요청 파라미터
     * @return 고정 공지글이 아닌 공지글들
     */
    PageResponse<NoticeBoard> getNoneFixedNoticeBoards(NoticeBoardSearchParam noticeBoardSearchParam, PageRequest pageRequest);
}
