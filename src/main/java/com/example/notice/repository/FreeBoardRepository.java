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
     * 자유게시판 게시글 저장
     *
     * @param freeBoard 자유게시판 게시글
     * @param memberId 회원 식별자
     */
    void save(@Param("freeBoard") FreeBoard freeBoard, @Param("memberId") Long memberId);

    /**
     * 자유게시판 게시글 조회
     *
     * @param freeBoardId 게시글 식별자
     * @return 게시글
     */
    Optional<FreeBoard> findBoardById(@Param("freeBoardId") Long freeBoardId);

    /**
     * 자유게시판 게시글 검색
     *
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 검색 결과
     */
    List<FreeBoard> findBoardsBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO, @Param("page") PageRequest pageRequest);

    /**
     * 검색 조건에 맞는 게시글 총 개수 조회
     *
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @return 검색된 게시글 총 개수
     */
    Integer getTotalCountBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO);

    /**
     * 게시글 조회수를 증가
     *
     * @param freeBoardId 게시글 식별자
     */
    void increaseViewsByBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 게시글에 댓글이 달려있는지 확인
     *
     * @param freeBoardId 게시글 식별자
     */
    boolean hasCommentByBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 게시글 내용과 제목을 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteContentAndTitleByBoardId(@Param("freeBoardId")Long freeBoardId);

    /**
     * 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteByBoardId(@Param("freeBoardId")Long freeBoardId);

    /**
     * 게시글의 식별자와 작성의 식별자를 통해 게시글 조회
     *
     * @param freeBoardId 게시글 식별자
     * @param memberId 사용자 식별자
     * @return 게시글
     */
    Optional<FreeBoard> findBoardByIdAndMemberId(@Param("freeBoardId") Long freeBoardId, @Param("memberId") Long memberId);

    /**
     * 게시글 수정
     *
     * @param freeBoard 게시글 수정 파라미터
     * @param freeBoardId 게시글 식별자
     */
    void update(@Param("freeBoard") FreeBoard freeBoard, @Param("freeBoardId") Long freeBoardId);

    /**
     * 관리자의 게시글 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteByAdmin(@Param("freeBoardId") Long freeBoardId);

    /**
     * 자유게시판 카테고리 조회
     *
     * @return 카테고리 리스트
     */
    List<String> getCategory();
}
