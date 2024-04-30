package com.example.notice.service;

import com.example.notice.entity.Comment;

import java.util.List;

/**
 * 댓글 서비스 클래스
 */
public interface CommentService {

    /**
     * 댓글 생성
     *
     * @param comment 댓글 생성 파라미터
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @param memberId 댓글 작성 회원 식별자
     */
    void createComment(Comment comment, Long freeBoardId, Long memberId);

    /**
     * 게시글에 해당하는 댓글 조회
     *
     * @param freeBoardId 게시글 식별자
     */
    List<Comment> getComments(Long freeBoardId);

    /**
     * 댓글을 삭제
     *
     * @param commentId 댓글 식별자
     * @param memberId 삭제 요청한 회원 식별자
     */
    void delete(Long commentId, Long memberId);

    /**
     * 관리자의 댓글 삭제
     *
     * @param commentId 댓글 식별자
     * @param memberId 관리자 식별자
     */
    void deleteByAdmin(Long commentId, Long memberId);
}
