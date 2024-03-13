package com.example.notice.service;

import com.example.notice.entity.Comment;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .orElseThrow(() -> new AuthorizationException("댓글에 대한 권한이 없는 사용자입니다."));
    }

    @Override
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
