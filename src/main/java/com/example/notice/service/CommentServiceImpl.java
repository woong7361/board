package com.example.notice.service;

import com.example.notice.entity.Comment;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void checkAuthorization(Long commentId, Long memberId) {
        commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new AuthorizationException(AUTHORIZATION_EXCEPTION_MESSAGE));
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId, Long memberId) {
        commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId)
                .ifPresentOrElse(
                        (c) -> {commentRepository.deleteById(commentId);},
                        () -> {commentRepository.deleteByIdToAdmin(commentId);});
    }
}
