package com.example.notice.repository;

import com.example.notice.entity.NoticeBoard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<NoticeBoard> findFixedNoticeBoardWithoutContentByLimit(@Param("limit") Integer limit);

}
