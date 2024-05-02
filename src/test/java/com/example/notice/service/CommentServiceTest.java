package com.example.notice.service;

import com.example.notice.entity.Comment;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.dummy.TestData.getSavedComment;
import static org.mockito.ArgumentMatchers.any;


class CommentServiceTest {
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private CommentService commentService = new CommentServiceImpl(commentRepository);

    @Nested
    @DisplayName("코멘트 생성/저장 테스트")
    public class CreateCommentTest {
        @DisplayName("insertComment")
        @Test
        public void insert() throws Exception {
            //given
            Comment commentCreateRequest = Comment.builder()
                    .content("create")
                    .build();

            long freeBoardId = 1L;
            long memberId = 2L;

            //when
            //then
            commentService.createComment(commentCreateRequest, freeBoardId, memberId);
        }
    }

    @Nested
    @DisplayName("게시글 식별자로 게시글들 가져오기")
    public class GetCommentsByBoardId {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 11L;
            Comment comment1 = getSavedComment(485641L, freeBoardId, 6874534L);
            Comment comment2 = getSavedComment(545634L, freeBoardId, 854646L);
            List<Comment> findComments = List.of(comment1, comment2);

            //when
            Mockito.when(commentRepository.getCommentsByFreeBoardId(freeBoardId))
                    .thenReturn(findComments);

            List<Comment> comments = commentService.getComments(freeBoardId);
            //then

            Assertions.assertThat(comments.size()).isEqualTo(findComments.size());
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
            Mockito.when(commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.of(Comment.builder().build()));
            //then
            commentService.delete(commentId, memberId);
        }

        @DisplayName("댓글 삭제에 대한 권한이 부족할때")
        @Test
        public void anotherId() throws Exception{
            //given
            Long memberId = 453145L;
            Long commentId = 564523L;

            //when
            Mockito.when(commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.empty());
            //then
            Assertions.assertThatThrownBy(() -> commentService.delete(commentId, memberId))
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

            Mockito.when(commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.of(Comment.builder().build()));

            //when
            //then
            commentService.deleteByAdmin(commentId, memberId);
        }

        @DisplayName("자신의 댓글이 아닌 댓글을 지울때")
        @Test
        public void deleteOther() throws Exception {
            //given
            Long memberId = 456L;
            Long commentId = 123L;

            Mockito.when(commentRepository.getCommentByCommentIdAndMemberId(commentId, memberId))
                    .thenReturn(Optional.empty());

            //when
            //then
            commentService.deleteByAdmin(commentId, memberId);
        }
    }

}