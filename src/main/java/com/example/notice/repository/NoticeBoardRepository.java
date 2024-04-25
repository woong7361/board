package com.example.notice.repository;

import com.example.notice.dto.request.NoticeBoardSearchDTO;
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
     */
    List<NoticeBoard> findFixedNoticeBoardByLimit(@Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정된 공지 게시글을 제외한 나머지 공지 게시글을 반환
     * @param noticeBoardSearchDTO 공지글 검색 파라미터
     * @param pageRequest 페이지 요청 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글들
     */
    List<NoticeBoard> findNoneFixedNoticeBoardBySearchParam(
            @Param("search") NoticeBoardSearchDTO noticeBoardSearchDTO,
            @Param("page") PageRequest pageRequest,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정된 공지 게시글을 제외한 나머지 공지 게시글 수를 반환
     * @param noticeBoardSearchDTO 공지글 검색 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글 수
     */
    Integer findNoneFixedNoticeBoardCountBySearchParam(
            @Param("search") NoticeBoardSearchDTO noticeBoardSearchDTO,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 게시글 식별자를 통해 공지 게시글을 가져온다.
     * @param noticeBoardId 공지 게시글 식별자
     * @return 공지 게시글
     */
    Optional<NoticeBoard> findById(@Param("noticeBoardId") Long noticeBoardId);

    /**
     * 게시글 식별자를 통해 게시글 삭제
     * @param noticeBoardId 공지 게시글 식별자
     */
    void deleteById(@Param("noticeBoardId") Long noticeBoardId);

    /**
     * 게시글 식별자를 통해 게시글 수정
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @param noticeBoard 공지 게시글 수정 요청 파라미터
     */
    void updateBoardById(@Param("noticeBoardId") Long noticeBoardId, @Param("noticeBoard") NoticeBoard noticeBoard);

    /**
     * 공지 게시글 조회수 추가
     * @param noticeBoardId 공지 게시글 식별자
     */
    void increaseViewsById(@Param("noticeBoardId") Long noticeBoardId);
}
