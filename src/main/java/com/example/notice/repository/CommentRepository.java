package com.example.notice.repository;

import com.example.notice.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 댓글 repository
 */
@Mapper
public interface CommentRepository {


    /**
     * 댓글 저장
     * @param comment 코멘트 생성 인자
     * @param freeBoardId 코멘트의 부모 게시글 식별자
     * @param memberId 코멘트를 작성한 회원 식별자
     */
    void save(@Param("comment") Comment comment, @Param("freeBoardId") Long freeBoardId, @Param("memberId") Long memberId);

    /**
     * 게시글에 해당하는 댓글 리스트 가져오기
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @return 게시글에 속한 댓글들
     */
    List<Comment> getCommentsByFreeBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 댓글을 댓글 식별자와 사용자 식별자를 통해 가져온다.
     * @param commentId 댓글 식별자
     * @param memberId 사용자 식별자
     * @return 댓글
     */
    Optional<Comment> getCommentByCommentIdAndMemberId(@Param("commentId") Long commentId, @Param("memberId") Long memberId);

    /**
     * 식별자에 해당하는 댓글을 삭제한다.
     * @param commentId 댓글 식별자
     */
    void deleteById(@Param("commentId") Long commentId);

    /**
     * 관리자가 다른 사용자가 생성한 댓글을 삭제 처리한다.
     * @param commentId 댓글 식별자
     */
    void deleteContentById(@Param("commentId") Long commentId);
}
