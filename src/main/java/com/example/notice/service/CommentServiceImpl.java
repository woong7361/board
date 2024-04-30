package com.example.notice.service;

import com.example.notice.entity.Comment;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.notice.constant.ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void createComment(Comment comment, Long freeBoardId, Long memberId) {
        commentRepository.save(comment, freeBoardId, memberId);
    }

    @Override
    public List<Comment> getComments(Long freeBoardId) {
        return commentRepository.getCommentsByFreeBoardId(freeBoardId);
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long memberId) {
        checkAuthorization(commentId, memberId);

        commentRepository.deleteById(commentId);
    }

    /**
     * @implNote  댓글이 관리자의 댓글이면 삭제, 다른 사용자의 댓글이라면 내용만 지운다.
     */
    @Override
    @Transactional
    public void deleteByAdmin(Long commentId, Long memberId) {
        commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId)
                .ifPresentOrElse(
                        (c) -> {commentRepository.deleteById(commentId);},
                        () -> {commentRepository.deleteContentById(commentId);});
    }

    private void checkAuthorization(Long commentId, Long memberId) {
        commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE));
    }
}
