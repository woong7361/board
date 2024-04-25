package com.example.notice.controller;

import com.example.notice.auth.provider.AuthProvider;
import com.example.notice.constant.ResponseConstant;
import com.example.notice.entity.Comment;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.CommentService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.example.notice.mock.util.LoginTestUtil.getMockAdminSession;
import static com.example.notice.mock.util.LoginTestUtil.getMockSessionCookie;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = {CommentController.class})
class CommentControllerTest extends RestDocsHelper {

    @MockBean
    CommentService commentService;

    @Autowired
    AuthProvider authProvider;

    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Nested
    @DisplayName("댓글 생성 컨트롤러 테스트")
    public class CommentCreateTest {
        private static final String COMMENT_CREATE_URI = "/api/boards/free/{freeBoardId}/comments";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Comment comment = Comment.builder()
                    .content("content")
                    .build();

            Long freeBoardId = 1L;
            long memberId = 541341L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post(COMMENT_CREATE_URI, freeBoardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .content(mapper.writeValueAsString(comment))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    //rest docs
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("content")
                                            .description("코멘트 내용")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            ),
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("freeBoardId")
                                            .description("게시글 식별자")
                            )
                    ));

        }

        @DisplayName("comment 생성 파라미터에 혀용되지 않은 입력값이 들어왔을때")
        @ParameterizedTest
        @MethodSource("invalidCommentParam")
        public void invalidCommentParameter(String content) throws Exception {
            //given
            Long freeBoardId = 1L;
            Comment comment = Comment.builder()
                    .freeBoardId(freeBoardId)
                    .content(content)
                    .build();

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post(COMMENT_CREATE_URI, (freeBoardId))
                    .headers(authenticationTestUtil.getLoginTokenHeaders(541341L))
                    .content(mapper.writeValueAsString(comment))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            action
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
        private static final String GET_COMMENT_URI = "/api/boards/free/{freeBoardId}/comments";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 1L;

            Comment comment1 = Comment.builder()
                    .commentId(3L)
                    .freeBoardId(freeBoardId)
                    .memberId(5L)
                    .memberName("memberName1")
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .content("content1")
                    .build();

            Comment comment2 = Comment.builder()
                    .commentId(4L)
                    .freeBoardId(freeBoardId)
                    .memberId(6L)
                    .memberName("memberName2")
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .content("content2")
                    .build();

            //when
            List<Comment> result = List.of(comment1, comment2);
            Mockito.when(commentService.getComments(freeBoardId))
                    .thenReturn(result);

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .get(GET_COMMENT_URI, freeBoardId));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + ResponseConstant.COMMENTS_PARAM).isArray())
                    .andExpect(jsonPath("$.comments[0].commentId" ).value(comment1.getCommentId()))
                    .andExpect(jsonPath("$.comments[0].freeBoardId" ).value(comment1.getFreeBoardId()))
                    .andExpect(jsonPath("$.comments[0].content" ).value(comment1.getContent()))
                    .andExpect(jsonPath("$.comments[0].memberId" ).value(comment1.getMemberId()))
                    .andExpect(jsonPath("$.comments[0].memberName" ).value(comment1.getMemberName()))

            //rest docs
                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("freeBoardId")
                                            .description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            PayloadDocumentation.responseFields(
                                    PayloadDocumentation.fieldWithPath("comments.[].commentId")
                                            .description("댓글 식별자"),
                                    PayloadDocumentation.fieldWithPath("comments.[].freeBoardId")
                                            .description("게시글 식별자"),
                                    PayloadDocumentation.fieldWithPath("comments.[].content")
                                            .description("댓글 내용"),
                                    PayloadDocumentation.fieldWithPath("comments.[].createdAt")
                                            .description("생성 일자"),
                                    PayloadDocumentation.fieldWithPath("comments.[].modifiedAt")
                                            .description("수정 일자"),
                                    PayloadDocumentation.fieldWithPath("comments.[].memberId")
                                            .description("작성자 식별자"),
                                    PayloadDocumentation.fieldWithPath("comments.[].memberName")
                                            .description("작성자 이름")
                            )
                    ));

        }

        @DisplayName("게시글에 댓글이 없을때")
        @Test
        public void returnBlankComments() throws Exception {
            //given
            Long freeBoardId = 1L;

            //when
            Mockito.when(commentService.getComments(freeBoardId))
                    .thenReturn(List.of());

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_COMMENT_URI, freeBoardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(541341L)));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + ResponseConstant.COMMENTS_PARAM)
                            .isEmpty());

        }
    }

    @Nested
    @DisplayName("댓글 삭제 컨트롤러 테스트")
    public class DeleteCommentTest {
        private static final String DELETE_COMMENT_URI = "/api/boards/free/comments/{commentId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long commentId = 1L;
            Long memberId = 541341L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .delete(DELETE_COMMENT_URI, commentId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId)));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

            //rest docs
                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("commentId")
                                            .description("댓글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));
        }


    }

    @Nested
    @DisplayName("관리자의 댓글 생성 테스트")
    public class CreateCommentByAdminTest {

        private static final String CREATE_COMMENT_BY_ADMIN_URL = "/admin/boards/free/{freeBoardId}/comments";
        @DisplayName("정상 처리")
        @Test

        public void success() throws Exception {
            //given
            Comment comment = Comment.builder()
                    .content("content")
                    .build();

            Long freeBoardId = 1L;
            Long memberId = 541341L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post(CREATE_COMMENT_BY_ADMIN_URL, freeBoardId)
                    .cookie(getMockSessionCookie())
                    .session(getMockAdminSession(memberId))
                    .content(mapper.writeValueAsString(comment))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    //rest docs
                    .andDo(restDocs.document(
                            PayloadDocumentation.requestFields(
                                    PayloadDocumentation.fieldWithPath("content")
                                            .description("코멘트 내용")
                            )
                    ))
                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("freeBoardId")
                                            .description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID")
                                    .description("세션 쿠키")
                    )));
        }

        @DisplayName("comment 생성 파라미터에 혀용되지 않은 입력값이 들어왔을때")
        @ParameterizedTest
        @MethodSource("invalidCommentParam")
        public void invalidCommentParameter(String content) throws Exception {
            //given
            Long freeBoardId = 1L;
            Long memberId = 3413163L;
            Comment comment = Comment.builder()
                    .freeBoardId(freeBoardId)
                    .content(content)
                    .build();

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.post(CREATE_COMMENT_BY_ADMIN_URL, (freeBoardId))
                    .cookie(getMockSessionCookie())
                    .session(getMockAdminSession(memberId))
                    .content(mapper.writeValueAsString(comment))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            action
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
    @DisplayName("관리자의 댓글 삭제")
    public class DeleteCommentByAdminTest {
        private static final String DELETE_COMMENT_BY_ADMIN_URL = "/admin/boards/free/comments/{commentId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long commentId = 541341L;
            Long memberId = 45341L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.delete(DELETE_COMMENT_BY_ADMIN_URL, commentId)
                    .cookie(getMockSessionCookie())
                    .session(getMockAdminSession(memberId))
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            RequestDocumentation.pathParameters(
                                    RequestDocumentation.parameterWithName("commentId")
                                            .description("댓글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID")
                                    .description("세션 쿠키")
                    )));
        }
    }

}