package com.example.notice.mock.repository;

import com.example.notice.entity.Comment;
import com.example.notice.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.notice.mock.database.MemoryDataBase.COMMENT_STORAGE;

public class MockCommentRepository implements CommentRepository {

    public static Comment NO_FK_COMMENT = Comment.builder()
            .commentId(1L)
            .content("in Mock Comment")
            .createdAt(LocalDateTime.now())
            .build();

    @Override
    public void save(Comment comment, Long freeBoardId, Long memberId) {
        Comment newComment = MockCommentRepository
                .commentBuilderMapper(comment)
                .freeBoardId(freeBoardId)
                .memberId(memberId)
                .build();

        COMMENT_STORAGE.add(newComment);
    }

    @Override
    public List<Comment> getCommentsByFreeBoardId(Long freeBoardId) {
        return COMMENT_STORAGE.stream()
                .filter(c -> c.getFreeBoardId().equals(freeBoardId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> getCommentByCommentIdAndMemberId(Long commentId, Long memberId) {
        return COMMENT_STORAGE.stream()
                .filter(c -> c.getCommentId().equals(commentId))
                .filter(c -> c.getMemberId().equals(memberId))
                .findFirst();
    }

    @Override
    public void deleteById(Long commentId) {
        COMMENT_STORAGE
                .removeIf(c -> c.getCommentId().equals(commentId));
    }

    @Override
    public void deleteContentById(Long commentId) {
        COMMENT_STORAGE
                .removeIf(c -> c.getCommentId().equals(commentId));
    }

    public static Comment.CommentBuilder commentBuilderMapper(Comment comment) {
        return Comment.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .freeBoardId(comment.getFreeBoardId())
                .memberId(comment.getMemberId());

    }
}
