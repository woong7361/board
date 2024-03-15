package com.example.notice.repository;

import com.example.notice.dto.NoticeBoardSearchParam;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 알림 게시판 repository
 */
@Mapper
public interface NoticeBoardRepository {

    /**
     * 알림 게시판 게시글 저장
     * @param noticeBoard 알림 게시판 게시글 생성 인자
     * @param memberId 관리자 식별자
     */
    void save(@Param("noticeBoard") NoticeBoard noticeBoard, @Param("memberId") Long memberId);

    /**
     * 상단 고정된 공지 게시글들을 반환한다.
     * @return 상단 고정된 공지 게시글들 반환
     * //TODO modifiedAt 수정일 null이 아닌 db에는 작성일과 같이 저장이 편한듯? -> 검색이 용이 - null 검색 힘듬
     */
    List<NoticeBoard> findFixedNoticeBoardByLimit(@Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정된 공지 게시글을 제외한 나머지 공지 게시글을 반환
     * @param noticeBoardSearchParam 공지글 검색 파라미터
     * @param pageRequest 페이지 요청 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글들
     */
    List<NoticeBoard> findNoneFixedNoticeBoardBySearchParam(
            @Param("search") NoticeBoardSearchParam noticeBoardSearchParam,
            @Param("page") PageRequest pageRequest,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정된 공지 게시글을 제외한 나머지 공지 게시글 수를 반환
     * @param noticeBoardSearchParam 공지글 검색 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글 수
     */
    Integer findNoneFixedNoticeBoardCountBySearchParam(
            @Param("search") NoticeBoardSearchParam noticeBoardSearchParam,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 게시글 식별자를 통해 공지 게시글을 가져온다.
     * @param noticeBoardId 공지 게시글 식별자
     * @return 공지 게시글
     */
    Optional<NoticeBoard> findById(@Param("noticeBoardId") Long noticeBoardId);
}
