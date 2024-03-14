package com.example.notice.controller;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.entity.Comment;
import com.example.notice.entity.Member;
import com.example.notice.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void createMember() {
        Member member = Member.builder()
                .memberId(1L)
                .build();

        AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
    }

    @Nested
    @DisplayName("댓글 생성 컨트롤러 테스트")
    public class CommentCreateTest {
        private static final String COMMENT_CREATE_URI = "/api/boards/free/%s/comments";
        @DisplayName("정상 처리")
        @Test
        public void createCommentTest() throws Exception {
            //given
            Long freeBoardId = 1L;
            Comment comment = Comment.builder()
                    .freeBoardId(freeBoardId)
                    .content("content")
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_CREATE_URI.formatted(freeBoardId))
                            .content(mapper.writeValueAsString(comment))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

        }

        @DisplayName("comment 생성 파라미터에 혀용되지 않은 입력값이 들어왔을때")
        @ParameterizedTest
        @MethodSource("invalidCommentParam")
        public void invalidCommentParameter(String content) throws Exception{
            //given
            Long freeBoardId = 1L;
            Comment comment = Comment.builder()
                    .freeBoardId(freeBoardId)
                    .content(content)
                    .build();
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_CREATE_URI.formatted(freeBoardId))
                            .content(mapper.writeValueAsString(comment))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect((result -> Assertions.assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)));
        }

        private static Stream<Arguments> invalidCommentParam() {
            return Stream.of(
                    Arguments.of((Object) null), // null일때
                    Arguments.of(""), // 빈 문자열일때
                    Arguments.of("   ") // 공백일때
            );
        }

    }

    @Nested
    @DisplayName("댓글 조회 컨트롤러 테스트")
    public class GetCommentTest {
        private static final String GET_COMMENT_URI = "/api/boards/free/%s/comments";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 1L;

            Comment comment1 = Comment.builder()
                    .commentId(3L)
                    .content("content1")
                    .build();
            Comment comment2 = Comment.builder()
                    .commentId(4L)
                    .content("content2")
                    .build();
            //when
            List<Comment> result = List.of(comment1, comment2);
            Mockito.when(commentService.getComments(freeBoardId))
                    .thenReturn(result);

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_COMMENT_URI.formatted(freeBoardId)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$." + ResponseConstant.COMMENTS_PARAM)
                            .isArray());

        }

        @DisplayName("게시글에 댓글이 없을때")
        @Test
        public void returnBlankComments() throws Exception{
            //given
            Long freeBoardId = 1L;
            //when
            Mockito.when(commentService.getComments(freeBoardId))
                    .thenReturn(List.of());
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_COMMENT_URI.formatted(freeBoardId)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$." + ResponseConstant.COMMENTS_PARAM)
                            .isEmpty());
        }
    }

    @Nested
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    public class DeleteCommentTest {
        private static final String DELETE_COMMENT_URI = "/api/boards/free/comments/%s";
        @DisplayName("정상 처리")
        @Test
        public void deleteComment() throws Exception {
            //given
            Long commentId = 1L;

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_COMMENT_URI.formatted(commentId)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

}