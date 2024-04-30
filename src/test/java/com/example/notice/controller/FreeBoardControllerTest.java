package com.example.notice.controller;

import com.example.notice.entity.FreeBoard;
import com.example.notice.exception.BadRequestParamException;
import com.example.notice.mock.util.LoginTestUtil;
import com.example.notice.page.PageRequest;
import com.example.notice.page.PageResponse;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.FreeBoardService;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.cookies.CookieDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;
import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(FreeBoardController.class)
class FreeBoardControllerTest extends RestDocsHelper {

    @MockBean
    FreeBoardService freeBoardService;

    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    @Nested
    @DisplayName("자유게시판 카테고리 조회")
    public class GetFreeBoardCategories {

        private static final String GET_FREE_BOARD_CATEGORIES_URL = "/api/boards/free/category";

        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            List<String> categories = List.of("A", "B", "C");
            //when
            Mockito.when(freeBoardService.getCategories())
                    .thenReturn(categories);

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_FREE_BOARD_CATEGORIES_URL));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.categories").isArray())
                    .andExpect(jsonPath("$.categories[0]").value(categories.get(0)))

                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("categories").description("카테고리 리스트")
                            )
                    ));
        }
    }


    @Nested
    @DisplayName("자유게시판 생성 테스트")
    public class FreeBoardCreateTest {

        public String FREE_BOARD_CREATE_URI = "/api/boards/free";


        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("title", "title");
            params.add("content", "content");
            params.add("category", "category");

            MockMultipartFile file = new MockMultipartFile("files", "fileBytes".getBytes());
            MockMultipartFile title = new MockMultipartFile("freeBoard", "",
                    "application/json", mapper.writeValueAsString(params.toSingleValueMap()).getBytes());
            long boardId = 10L;
            when(freeBoardService.createFreeBoard(any(), any(), anyLong()))
                    .thenReturn(boardId);

            //when
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(FREE_BOARD_CREATE_URI)
                    .file(file)
                    .file(title)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(45354L))
                    .params(params));

            //then
            action

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId))


                    .andDo(restDocs.document(
                            requestParts(
                                    partWithName("freeBoard").description("게시글 생성 파라미터들"),
                                    partWithName("files").description("업로드할 파일들")
                            )))
                    .andDo(restDocs.document(
                            requestPartFields("freeBoard",
                                    fieldWithPath("category").description("카테고리"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("freeBoardId").description("생성한 게시글 식별자")
                            )
                    ));
        }

        @DisplayName("파일이 비어있을때")
        @Test
        public void emptyFileRequest() throws Exception{
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("title", "title");
            params.add("content", "content");
            params.add("category", "category");

            MockMultipartFile title = new MockMultipartFile("freeBoard", "",
                    "application/json", mapper.writeValueAsString(params).getBytes());
            long boardId = 10L;
            when(freeBoardService.createFreeBoard(any(), any(), anyLong()))
                    .thenReturn(boardId);

            //when
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(FREE_BOARD_CREATE_URI)
                    .file(title)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(45354L))
                    .params(params));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId));

        }

        @Nested
        @DisplayName("게시글 인자 검증 테스트")
        public class FreeBoardParameterTest {

            @DisplayName("허용되지 않은 게시글 파라미터가 들어올때")
            @ParameterizedTest
            @MethodSource("invalidFreeBoardParam")
            public void invalidCreateInput(String title, String content, String category) throws Exception {
                //given
                FreeBoard board = FreeBoard.builder()
                        .title(title)
                        .content(content)
                        .category(category)
                        .build();

                //when
                ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(FREE_BOARD_CREATE_URI)
                        .headers(authenticationTestUtil.getLoginTokenHeaders(45354L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(board)));

                //then
                action
                        .andExpect(result -> assertThat(result.getResolvedException())
                                .isInstanceOf(MethodArgumentNotValidException.class))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

            }

            private static Stream<Arguments> invalidFreeBoardParam() {
                return Stream.of(
                        Arguments.of(null, "content", "category"), // null일때
                        Arguments.of("title", null, "category"),
                        Arguments.of("title", "content", null),
                        Arguments.of("", "content", "category"), // 빈 문자열일때
                        Arguments.of("title", "", "category"),
                        Arguments.of("title", "content", "")

                );
            }
        }


    }

    @Nested
    @DisplayName("자유게시판 게시글 조회 테스트")
    public class getFreeBoardTest {
        private final String GET_FREE_BOARD_URI = "/api/boards/free/{freeBoardId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String freeBoardId = String.valueOf(1L);

            //when
            Mockito.when(freeBoardService.getBoardById(Long.valueOf(freeBoardId)))
                    .thenReturn(SAVED_FREE_BOARD);

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .get(GET_FREE_BOARD_URI, freeBoardId));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.freeBoardId").value(SAVED_FREE_BOARD.getFreeBoardId()))
                    .andExpect(jsonPath("$.title").value(SAVED_FREE_BOARD.getTitle()))
                    .andExpect(jsonPath("$.content").value(SAVED_FREE_BOARD.getContent()))
                    .andExpect(jsonPath("$.category").value(SAVED_FREE_BOARD.getCategory()))
                    .andExpect(jsonPath("$.views").value(SAVED_FREE_BOARD.getViews()))
                    .andExpect(jsonPath("$.memberId").value(SAVED_FREE_BOARD.getMemberId()))
                    .andExpect(jsonPath("$.memberName").value(SAVED_FREE_BOARD.getMemberName()))

                    //rest docs
                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("freeBoardId").description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("freeBoardId").description("게시글 식별자"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용"),
                                    fieldWithPath("category").description("카테고리"),
                                    fieldWithPath("views").description("조회수"),
                                    fieldWithPath("createdAt").description("생성 일자"),
                                    fieldWithPath("modifiedAt").description("수정 일자"),
                                    fieldWithPath("memberId").description("작성자 식별자"),
                                    fieldWithPath("memberName").description("작성자 이름")
                            )));

        }
    }

    @Nested
    @DisplayName("자유게시판 게시글 검색 테스트")
    public class FreeBoardSearchTest {
        private static final String GET_BOARDS_URI = "/api/boards/free";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("startDate", LocalDateTime.now().minusMonths(2L).toString());
            params.add("endDate", LocalDateTime.now().toString());
            params.add("category", "category");
            params.add("keyword", "keyword");
            params.add("size", "5");
            params.add("currentPage", "0");
            params.add("orderColumn", "created_at");
            params.add("orderType", "modified_at");



            //when
            Mockito.when(freeBoardService.getBoardsBySearchParams(any(), any()))
                    .thenReturn(new PageResponse<>(
                            List.of(SAVED_FREE_BOARD, SAVED_FREE_BOARD),
                            new PageRequest(5, 0, "1", "2"),
                            2));

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARDS_URI)
                    .params(params));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

//            rest docs
                    .andDo(restDocs.document(
                            queryParameters(
                                    parameterWithName("startDate").description("검색 시작 일자"),
                                    parameterWithName("endDate").description("검색 종료 일자"),
                                    parameterWithName("category").description("카테고리"),
                                    parameterWithName("keyword").description("검색 키워드"),
                                    parameterWithName("size").description("검색 페이지 크기"),
                                    parameterWithName("orderColumn").description("정렬 인자"),
                                    parameterWithName("orderType").description("정렬 조건"),
                                    parameterWithName("currentPage").description("현재 페이지")
                            )
                    ))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("contents[].freeBoardId").description("게시글 식별자"),
                                    fieldWithPath("contents[].title").description("제목"),
                                    fieldWithPath("contents[].content").description("내용"),
                                    fieldWithPath("contents[].category").description("카테고리"),
                                    fieldWithPath("contents[].views").description("조회수"),
                                    fieldWithPath("contents[].createdAt").description("생성 일자"),
                                    fieldWithPath("contents[].modifiedAt").description("수정 일자"),
                                    fieldWithPath("contents[].memberId").description("작성자 식별자"),
                                    fieldWithPath("contents[].memberName").description("작성자 이름"),
                                    fieldWithPath("pageOffset").description("offset"),
                                    fieldWithPath("currentPage").description("현재 페이지"),
                                    fieldWithPath("totalCount").description("전체 게시글 수"),
                                    fieldWithPath("contentSize").description("현재 페이지의 게시글 수"),
                                    fieldWithPath("pageSize").description("현재 페이지 크기")
                            )));
        }

        @DisplayName("정렬 인자에 특수문자가 들어갈때")
        @ParameterizedTest
        @MethodSource("specialWordSortParams")
        public void specialWordInSortParam(String orderColumn, String orderType) throws Exception{
            //given
            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("orderColumn", orderColumn);
            params.add("orderType", orderType);

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.get(GET_BOARDS_URI)
                            .params(params))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect((result -> assertThat(result
                            .getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class)));
        }

        public static Stream<Arguments> specialWordSortParams() {
            return Stream.of(
                    Arguments.of("aaa", "a;b"),
                    Arguments.of("dsaf;", "abc"),
                    Arguments.of("dsa//z,,.", "abc"),
                    Arguments.of("dsaf;", "fdsa;;,./.,/"));
        }
    }

    @Nested
    @DisplayName("자유게시판 게시글 삭제 테스트")
    public class FreeBoardDeleteTest {

        private static final String BOARD_DELETE_URI = "/api/boards/free/{freeBoardId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 1L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.delete(BOARD_DELETE_URI, boardId)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(45354L))
            );

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("freeBoardId").description("게시글 식별자")
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
    @DisplayName("자유게시판 게시글 수정 테스트")
    public class FreeBoardUpdateTest {

        private static final String FREE_BOARD_UPDATE_URI = "/api/boards/free/{freeBoardId}";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            LinkedMultiValueMap<String, String> dParam = new LinkedMultiValueMap<>();
            dParam.add("deleteFileIds", "1");
            dParam.add("deleteFileIds", "2");
            dParam.add("deleteFileIds", "3");

            MockMultipartFile file = new MockMultipartFile("saveFiles", "fdsafds".getBytes());
            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam.toSingleValueMap()).getBytes());
            MockMultipartFile deleteIds = new MockMultipartFile(
                    "deleteIds",
                    "",
                    "application/json",
                    mapper.writeValueAsString(dParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, FREE_BOARD_UPDATE_URI, freeBoardId)
                    .file(file)
                    .file(freeBoard)
                    .file(deleteIds)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .params(fParam)
                    .params(dParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            requestParts(
                                    partWithName("freeBoard").description("게시글 수정 파라미터들"),
                                    partWithName("saveFiles").description("업로드할 파일들"),
                                    partWithName("deleteIds").description("삭제할 파일 식별자들")
                            )))
                    .andDo(restDocs.document(
                            requestPartFields("freeBoard",
                                    fieldWithPath("category").description("카테고리"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용")
                            ),
                            requestPartFields("deleteIds",
                                    fieldWithPath("deleteFileIds").type(JsonFieldType.ARRAY).description("삭제할 파일 식별자 리스트")
                            )
                    ))
                    .andDo(restDocs.document(
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT token")
                            )
                    ));
        }

        @DisplayName("게시글 인자 테스트")
        @ParameterizedTest
        @MethodSource("invalidFreeBoardParam")
        public void invalidUpdateInput(String title, String content, String category) throws Exception {
            //given
            FreeBoard board = FreeBoard.builder()
                    .title(title)
                    .content(content)
                    .category(category)
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.put(FREE_BOARD_UPDATE_URI, 10L)
                            .headers(authenticationTestUtil.getLoginTokenHeaders(45354L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board)))
                    .andExpect(result -> assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

        private static Stream<Arguments> invalidFreeBoardParam() {
            return Stream.of(
                    Arguments.of(null, "content", "category"), // null일때
                    Arguments.of("title", null, "category"),
                    Arguments.of("title", "content", null),
                    Arguments.of("", "content", "category"), // 빈 문자열일때
                    Arguments.of("title", "", "category"),
                    Arguments.of("title", "content", "")
            );
        }

        @DisplayName("삭제할 파일이 없을때")
        @Test
        public void emptyDeleteFile() throws Exception{
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            MockMultipartFile file = new MockMultipartFile("saveFiles", "fdsafds".getBytes());
            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, FREE_BOARD_UPDATE_URI, freeBoardId)
                    .file(file)
                    .file(freeBoard)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .params(fParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("생성할 파일이 없을때")
        @Test
        public void emptyCreateFile() throws Exception{
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            LinkedMultiValueMap<String, String> dParam = new LinkedMultiValueMap<>();
            dParam.add("deleteFileIds", "1");
            dParam.add("deleteFileIds", "2");
            dParam.add("deleteFileIds", "3");

            MockMultipartFile deleteIds = new MockMultipartFile(
                    "deleteIds",
                    "",
                    "application/json",
                    mapper.writeValueAsString(dParam).getBytes());

            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, FREE_BOARD_UPDATE_URI, freeBoardId)
                    .file(freeBoard)
                    .file(deleteIds)
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .params(fParam)
                    .params(dParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("관리자 자유게시판 게시글 생성")
    public class CreateFreeBoardByAdminControllerTest {
        private static final String CREATE_FREE_BOARD_BY_ADMIN_URL = "/admin/boards/free";

        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            Long memberId = 253L;
            long boardId = 10L;

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("title", "title");
            params.add("content", "content");
            params.add("category", "category");

            MockMultipartFile file = new MockMultipartFile("files", "fileBytes".getBytes());
            MockMultipartFile title = new MockMultipartFile("freeBoard", "",
                    "application/json", mapper.writeValueAsString(params.toSingleValueMap()).getBytes());
            when(freeBoardService.createFreeBoard(any(), any(), anyLong()))
                    .thenReturn(boardId);

            //when
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(CREATE_FREE_BOARD_BY_ADMIN_URL)
                    .file(file)
                    .file(title)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .cookie(LoginTestUtil.getMockSessionCookie())
                    .accept(MediaType.APPLICATION_JSON)
                    .params(params));

            //then
            action

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId))


                    .andDo(restDocs.document(
                            requestParts(
                                    partWithName("freeBoard").description("게시글 생성 파라미터들"),
                                    partWithName("files").description("업로드할 파일들")
                            )))
                    .andDo(restDocs.document(
                            requestPartFields("freeBoard",
                                    fieldWithPath("category").description("카테고리"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID")
                                    .description("세션 쿠키")
                    )))
                    .andDo(restDocs.document(
                            responseFields(
                                    fieldWithPath("freeBoardId").description("생성한 게시글 식별자")
                            )
                    ));
        }

        @DisplayName("게시글 인자 테스트")
        @ParameterizedTest
        @MethodSource("invalidFreeBoardParam")
        public void invalidUpdateInput(String title, String content, String category) throws Exception {
            //given
            Long memberId = 485634L;

            FreeBoard board = FreeBoard.builder()
                    .title(title)
                    .content(content)
                    .category(category)
                    .build();

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(CREATE_FREE_BOARD_BY_ADMIN_URL, 10L)
                            .session(LoginTestUtil.getMockAdminSession(memberId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(board)))
                    .andExpect(result -> assertThat(result.getResolvedException())
                            .isInstanceOf(MethodArgumentNotValidException.class))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

        }

        private static Stream<Arguments> invalidFreeBoardParam() {
            return Stream.of(
                    Arguments.of(null, "content", "category"), // null일때
                    Arguments.of("title", null, "category"),
                    Arguments.of("title", "content", null),
                    Arguments.of("", "content", "category"), // 빈 문자열일때
                    Arguments.of("title", "", "category"),
                    Arguments.of("title", "content", "")
            );
        }

        @DisplayName("첨부파일이 없을때")
        @Test
        public void emptyAttachmentFile() throws Exception{
            //given
            Long memberId = 253L;
            long boardId = 10L;

            LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("title", "title");
            params.add("content", "content");
            params.add("category", "category");

            MockMultipartFile title = new MockMultipartFile("freeBoard", "",
                    "application/json", mapper.writeValueAsString(params).getBytes());
            when(freeBoardService.createFreeBoard(any(), any(), anyLong()))
                    .thenReturn(boardId);

            //when
            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(CREATE_FREE_BOARD_BY_ADMIN_URL)
                    .file(title)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .cookie(LoginTestUtil.getMockSessionCookie())
                    .accept(MediaType.APPLICATION_JSON)
                    .params(params));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId));

        }
    }

    @Nested
    @DisplayName("관리자 자유게시글 삭제")
    public class DeleteFreeBoardByAdminControllerTest {
        private static final String BOARD_DELETE_URI_BY_ADMIN = "/admin/boards/free/{freeBoardId}";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long boardId = 1L;
            Long memberId = 763L;

            //when
            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.delete(BOARD_DELETE_URI_BY_ADMIN, boardId)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .cookie(LoginTestUtil.getMockSessionCookie()));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            pathParameters(
                                    parameterWithName("freeBoardId").description("게시글 식별자")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID")
                                    .description("세션 쿠키")
                    )));
        }
    }

    @Nested
    @DisplayName("관리자의 자유게시판 게시글 수정 테스트")
    public class EditFreeBoardByAdminControllerTest {
        private static final String EDIT_FREE_BOARD_BY_ADMIN_URL = "/admin/boards/free/{freeBoardId}";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            LinkedMultiValueMap<String, String> dParam = new LinkedMultiValueMap<>();
            dParam.add("deleteFileIds", "1");
            dParam.add("deleteFileIds", "2");
            dParam.add("deleteFileIds", "3");

            MockMultipartFile file = new MockMultipartFile("saveFiles", "fdsafds".getBytes());
            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam.toSingleValueMap()).getBytes());
            MockMultipartFile deleteIds = new MockMultipartFile(
                    "deleteIds",
                    "",
                    "application/json",
                    mapper.writeValueAsString(dParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, EDIT_FREE_BOARD_BY_ADMIN_URL, freeBoardId)
                    .file(file)
                    .file(freeBoard)
                    .file(deleteIds)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .cookie(LoginTestUtil.getMockSessionCookie())
                    .headers(authenticationTestUtil.getLoginTokenHeaders(memberId))
                    .params(fParam)
                    .params(dParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(restDocs.document(
                            requestParts(
                                    partWithName("freeBoard").description("게시글 수정 파라미터들"),
                                    partWithName("saveFiles").description("업로드할 파일들"),
                                    partWithName("deleteIds").description("삭제할 파일 식별자들")
                            )))
                    .andDo(restDocs.document(
                            requestPartFields("freeBoard",
                                    fieldWithPath("category").description("카테고리"),
                                    fieldWithPath("title").description("제목"),
                                    fieldWithPath("content").description("내용")
                            ),
                            requestPartFields("deleteIds",
                                    fieldWithPath("deleteFileIds").type(JsonFieldType.ARRAY).description("삭제할 파일 식별자 리스트")
                            )
                    ))
                    .andDo(restDocs.document(requestCookies(
                            CookieDocumentation.cookieWithName("JSESSIONID")
                                    .description("세션 쿠키")
                    )));
        }

        @DisplayName("삭제할 파일이 없을때")
        @Test
        public void emptyDeleteFile() throws Exception{
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            MockMultipartFile file = new MockMultipartFile("saveFiles", "fdsafds".getBytes());
            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, EDIT_FREE_BOARD_BY_ADMIN_URL, freeBoardId)
                    .file(file)
                    .file(freeBoard)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .params(fParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @DisplayName("생성할 파일이 없을때")
        @Test
        public void emptyCreateFile() throws Exception{
            //given
            Long freeBoardId = 11L;
            Long memberId = 45354L;

            LinkedMultiValueMap<String, String> fParam = new LinkedMultiValueMap<>();
            fParam.add("title", "title");
            fParam.add("content", "content");
            fParam.add("category", "category");

            LinkedMultiValueMap<String, String> dParam = new LinkedMultiValueMap<>();
            dParam.add("deleteFileIds", "1");
            dParam.add("deleteFileIds", "2");
            dParam.add("deleteFileIds", "3");

            MockMultipartFile deleteIds = new MockMultipartFile(
                    "deleteIds",
                    "",
                    "application/json",
                    mapper.writeValueAsString(dParam).getBytes());

            MockMultipartFile freeBoard = new MockMultipartFile(
                    "freeBoard",
                    "",
                    "application/json",
                    mapper.writeValueAsString(fParam).getBytes());
            //when

            ResultActions action = mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT, EDIT_FREE_BOARD_BY_ADMIN_URL, freeBoardId)
                    .file(freeBoard)
                    .file(deleteIds)
                    .session(LoginTestUtil.getMockAdminSession(memberId))
                    .params(fParam)
                    .params(dParam));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

}