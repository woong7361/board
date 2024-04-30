package com.example.notice.service;

import com.example.notice.entity.Comment;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockCommentRepository;
import com.example.notice.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.database.MemoryDataBase.COMMENT_STORAGE;
import static com.example.notice.mock.repository.MockCommentRepository.NO_FK_COMMENT;
import static org.mockito.ArgumentMatchers.any;


class CommentServiceTest {

    private CommentRepository commentRepository = new MockCommentRepository();
    private CommentService commentService = new CommentServiceImpl(commentRepository);

    private CommentRepository mockitoCommentRepository = Mockito.mock(CommentRepository.class);
    private CommentService mockitoCommentService = new CommentServiceImpl(mockitoCommentRepository);

    @BeforeEach
    public void clearRepository() {
        MemoryDataBase.initRepository();
    }

    @Nested
    @DisplayName("코멘트 생성/저장 테스트")
    public class CreateCommentTest {
        @DisplayName("insertComment")
        @Test
        public void insert() throws Exception {
            //given

            //when
            long memberId = 2L;
            long freeBoardId = 1L;
            commentService.createComment(NO_FK_COMMENT, freeBoardId, memberId);
            //then

            Comment findComment = COMMENT_STORAGE.stream()
                    .filter(comment -> comment.getCommentId().equals(NO_FK_COMMENT.getCommentId()))
                    .findFirst()
                    .get();

            Assertions.assertThat(findComment.getContent()).isEqualTo(NO_FK_COMMENT.getContent());
            Assertions.assertThat(findComment.getFreeBoardId()).isEqualTo(freeBoardId);
            Assertions.assertThat(findComment.getMemberId()).isEqualTo(memberId);
        }
    }

    @Nested
    @DisplayName("게시글 식별자로 게시글들 가져오기")
    public class GetCommentsByBoardId {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long findFreeBoardId = 11L;
            Long notFindFreeBoardId = 12L;
            int findSize = 5;
            int notFindSize = 3;

            for (int i = 0; i < findSize; i++) {
                Comment comment = Comment.builder()
                        .build();

                commentRepository.save(comment, findFreeBoardId, null);
            }
            for (int i = 0; i < notFindSize; i++) {
                Comment comment = Comment.builder()
                        .build();

                commentRepository.save(comment, notFindFreeBoardId, null);
            }


            //when
            List<Comment> comments = commentService.getComments(findFreeBoardId);
            //then

            Assertions.assertThat(comments.size()).isEqualTo(findSize);
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    public class DeleteComment {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long memberId = 453145L;
            Long commentId = 564523L;

            //when
            Mockito.when(mockitoCommentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.of(Comment.builder().build()));
            //then
            mockitoCommentService.delete(commentId, memberId);
        }

        @DisplayName("댓글 삭제에 대한 권한이 부족할때")
        @Test
        public void anotherId() throws Exception{
            //given
            Long memberId = 453145L;
            Long commentId = 564523L;

            //when
            Mockito.when(mockitoCommentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.empty());
            //then
            Assertions.assertThatThrownBy(() -> mockitoCommentService.delete(commentId, memberId))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("관리자의 댓글 삭제")
    public class DeleteCommentByAdmin {
        @DisplayName("자신의 댓글을 지울때")
        @Test
        public void deleteMine() throws Exception {
            //given
            Long memberId = 456L;
            Long commentId = 123L;

            Mockito.when(mockitoCommentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.of(Comment.builder().build()));

            //when
            //then
            mockitoCommentService.deleteByAdmin(commentId, memberId);
        }

        @DisplayName("자신의 댓글이 아닌 댓글을 지울때")
        @Test
        public void deleteOther() throws Exception {
            //given
            Long memberId = 456L;
            Long commentId = 123L;

            Mockito.when(mockitoCommentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.empty());

            //when
            //then
            mockitoCommentService.deleteByAdmin(commentId, memberId);
        }
    }

}