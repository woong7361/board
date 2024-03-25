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

import java.util.List;

import static com.example.notice.mock.database.MemoryDataBase.COMMENT_STORAGE;
import static com.example.notice.mock.repository.MockCommentRepository.NO_FK_COMMENT;


class CommentServiceTest {

    private CommentRepository commentRepository = new MockCommentRepository();
    private CommentService commentService = new CommentServiceImpl(commentRepository);

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
    @DisplayName("댓글 접근 권한 확인")
    public class CheckCommentAuthorization {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 12L;
            Long memberId = 13L;
            //when
            commentRepository.save(NO_FK_COMMENT, freeBoardId, memberId);
            //then
            commentService.checkAuthorization(NO_FK_COMMENT.getCommentId(), memberId);
        }

        @DisplayName("작성 권한이 없는 회원이 접근시")
        @Test
        public void invalidMemberAccess() throws Exception{
            //given
            Long freeBoardId = 12L;
            Long memberId = 13L;
            Long notOwnerMemberId = 14L;
            //when
            commentRepository.save(NO_FK_COMMENT, freeBoardId, memberId);
            //then
            Assertions.assertThatThrownBy(() -> commentService.checkAuthorization(NO_FK_COMMENT.getCommentId(), notOwnerMemberId))
                    .isInstanceOf(AuthorizationException.class);
        }
    }

    @Nested
    @DisplayName("댓글 삭제")
    public class DeleteComment {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            //when
            commentRepository.save(NO_FK_COMMENT, null, null);
            commentService.delete(NO_FK_COMMENT.getCommentId());
            //then
            Assertions.assertThat(COMMENT_STORAGE.size()).isEqualTo(0);
        }

        @DisplayName("다른 id로 삭제 시도")
        @Test
        public void anotherId() throws Exception{
            //given
            long anotherId = 48156313L;
            //when
            commentRepository.save(NO_FK_COMMENT, null, null);
            commentService.delete(anotherId);
            //then
            Assertions.assertThat(COMMENT_STORAGE.size()).isEqualTo(1);
        }
    }

}