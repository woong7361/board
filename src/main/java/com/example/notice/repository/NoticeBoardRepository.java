package com.example.notice.repository;

import com.example.notice.dto.request.NoticeBoardSearchDTO;
import com.example.notice.entity.NoticeBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 공지 게시판 repository
 */
@Mapper
public interface NoticeBoardRepository {

    /**
     * 공지 게시글 저장
     * @param noticeBoard 공지 게시글 생성 파라미터
     * @param memberId 작성자 식별자
     */
    void save(@Param("noticeBoard") NoticeBoard noticeBoard, @Param("memberId") Long memberId);

    /**
     * 상단 고정 공지 게시글 조회
     * @param maxFixedNoticeCount 조회할 개수
     *
     * @return 고정 공지 게시글들
     */
    List<NoticeBoard> findFixedNoticeBoardByLimit(@Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정 공지게시글을 제외한 게시글 검색
     *
     * @param noticeBoardSearchDTO 공지글 검색 요청 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글들
     */
    List<NoticeBoard> findNoneFixedNoticeBoardBySearchParam(
            @Param("search") NoticeBoardSearchDTO noticeBoardSearchDTO,
            @Param("page") PageRequest pageRequest,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 상단 고정 공지게시글을 제외한 게시글 검색 총 개수
     *
     * @param noticeBoardSearchDTO 공지글 검색 파라미터
     * @param maxFixedNoticeCount 최대 상단 고정 공지 게시글 수
     * @return 상단 고정된 공지글들을 제외한 공지글 수
     */
    Integer findNoneFixedNoticeBoardCountBySearchParam(
            @Param("search") NoticeBoardSearchDTO noticeBoardSearchDTO,
            @Param("fixedNoticeLimit") Integer maxFixedNoticeCount);

    /**
     * 공지 게시글 조회
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @return 공지 게시글
     */
    Optional<NoticeBoard> findById(@Param("noticeBoardId") Long noticeBoardId);

    /**
     * 공지 게시글 삭제
     *
     * @param noticeBoardId 공지 게시글 식별자
     */
    void deleteById(@Param("noticeBoardId") Long noticeBoardId);

    /**
     * 공지 게시글 수정
     *
     * @param noticeBoardId 공지 게시글 식별자
     * @param noticeBoard 공지 게시글 수정 요청 파라미터
     */
    void updateBoardById(@Param("noticeBoardId") Long noticeBoardId, @Param("noticeBoard") NoticeBoard noticeBoard);

    /**
     * 공지 게시글 조회수 증가
     *
     * @param noticeBoardId 공지 게시글 식별자
     */
    void increaseViewsById(@Param("noticeBoardId") Long noticeBoardId);

    /**
     * 공지 게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    List<String> getCategory();
}
