package com.example.notice.service;

import com.example.notice.entity.Comment;

import java.util.List;

/**
 * 댓글 서비스 클래스
 */
public interface CommentService {

    /**
     * 댓글 생성
     * @param comment 댓글 생성 파라미터
     * @param freeBoardId 댓글 부모 게시글 식별자
     * @param memberId 댓글 작성 회원 식별자
     */
    void createComment(Comment comment, Long freeBoardId, Long memberId);

    /**
     * 게시글에 해당하는 댓글 가져오기
     * @param freeBoardId 댓글 부모 게시글 식별자
     */
    List<Comment> getComments(Long freeBoardId);

    /**
     * 댓글의 관리 권한을 확인한다.
     * @param commentId 댓글 식별자
     * @param memberId 회원 식별자
     */
    void checkAuthorization(Long commentId, Long memberId);

    /**
     * 댓글을 삭제한다.
     * @param commentId 댓글 식별자
     */
    void delete(Long commentId);
}
