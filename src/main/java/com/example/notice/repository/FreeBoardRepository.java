package com.example.notice.repository;

import com.example.notice.dto.request.FreeBoardSearchDTO;
import com.example.notice.entity.FreeBoard;
import com.example.notice.page.PageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 자유 게시판 repository
 */
@Mapper
public interface FreeBoardRepository {
    /**
     * 게시글 저장
     * @param freeBoard 자유게시판 게시글
     */
    void save(@Param("freeBoard") FreeBoard freeBoard, @Param("memberId") Long memberId);

    /**
     * 게시글 식별자에 맞는 게시글 검색
     * @param freeBoardId 게시글 식별자
     * @return 게시글
     */
    Optional<FreeBoard> findBoardById(@Param("freeBoardId") Long freeBoardId);

    /**
     * 게시글 검색조건에 맞는 게시글들 검색
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 게시글들
     */
    List<FreeBoard> findBoardsBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO, @Param("page") PageRequest pageRequest);

    /**
     * 게시글 검색조건에 맞는 게시글의 총 개수를 가져온다.
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @return 조건에 맞는 게시글의 총 개수
     */
    Integer getTotalCountBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO);

    /**
     * 게시글 조회수를 올린다.
     * @param freeBoardId 게시글 식별자
     */
    void increaseViewsByBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 게시글에 댓글이 있는지 확인한다.
     * @param freeBoardId 게시글 식별자
     */
    boolean hasCommentByBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 내용과 작성자를 게시글에서 삭제한다.
     * @param freeBoardId 게시글 식별자
     */
    void deleteContentAndMemberByBoardId(@Param("freeBoardId")Long freeBoardId);

    /**
     * 게시글을 삭제한다.
     * @param freeBoardId 게시글 식별자
     */
    void deleteByBoardId(@Param("freeBoardId")Long freeBoardId);

    /**
     * 게시글을 작성한 사용자가 맞는지 검색한다.
     * @param freeBoardId 게시글 식별자
     * @param memberId 사용자 식별자
     * @return 게시글
     */
    Optional<FreeBoard> findBoardByIdAndMemberId(@Param("freeBoardId") Long freeBoardId, @Param("memberId") Long memberId);

    /**
     * 게시글을 수정한다.
     *
     * @param freeBoard   게시글 수정 인자
     * @param freeBoardId 게시글 식별자
     */
    void update(@Param("freeBoard") FreeBoard freeBoard, @Param("freeBoardId") Long freeBoardId);

    /**
     * 관리자의 다른 회원 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteByAdmin(@Param("freeBoardId") Long freeBoardId);
}
