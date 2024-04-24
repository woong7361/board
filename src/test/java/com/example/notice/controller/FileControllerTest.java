package com.example.notice.controller;

import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.restdocs.RestDocsHelper;
import com.example.notice.service.FileService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.io.File;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;


@NoFilterMvcTest(FileController.class)
class FileControllerTest extends RestDocsHelper {

    @MockBean
    private FileService fileService;

    @BeforeEach
    public void initMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Nested
    @DisplayName("파일 다운로드 컨트롤러 테스트")
    public class FileDownloadTest {

        private static String FILE_DOWNLOAD_URI = "/api/boards/files/{fileId}/download";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 13L;
            String originalName = "originalName";

            File file = new File("src/test/resources/test.txt");

            //when
            Mockito.when(fileService.getPhysicalFile(fileId))
                    .thenReturn(file);
            Mockito.when(fileService.getFileOriginalNameById(fileId))
                    .thenReturn(originalName);

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders
                    .post(FILE_DOWNLOAD_URI, fileId));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())

                    .andDo(document("file.download",
                            RequestDocumentation.pathParameters(
                                    parameterWithName("fileId")
                                            .description("파일 식별자")
                                            .attributes(new Attributes.Attribute("key", "value"))
                            ),
                            responseHeaders(
                                    headerWithName("content-disposition")
                                            .description("파일 다운로드 & 파일 이름")
                            )
                    ));
        }
    }


    @Nested
    @DisplayName("파일 조회 컨트롤러 테스트")
    public class getFileByFreeBoardIdControllerTest {

        private static final String FREE_BOARD_FILE_URI = "/api/boards/free/{freeBoardId}/files";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long freeBoardId = 15631L;

            FileResponseDTO file1 = FileResponseDTO.builder()
                    .freeBoardId(freeBoardId)
                    .fileId(1531L)
                    .originalName("name1")
                    .build();

            FileResponseDTO file2 = FileResponseDTO.builder()
                    .freeBoardId(freeBoardId)
                    .fileId(1531L)
                    .originalName("name1")
                    .build();

            //when
            Mockito.when(fileService.getFileByFreeBoardId(freeBoardId))
                    .thenReturn(List.of(file1, file2));

            ResultActions action = mockMvc.perform(RestDocumentationRequestBuilders.get(FREE_BOARD_FILE_URI, freeBoardId));

            //then
            action
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].freeBoardId").value(file1.getFreeBoardId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].fileId").value(file1.getFileId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].originalName").value(file1.getOriginalName()))

                    //rest docs
                    .andDo(
                            document("file-get",
                                    RequestDocumentation.pathParameters(
                                            parameterWithName("freeBoardId")
                                                    .description("자유게시판 게시글 식별자")
                                    ),
                                    responseFields(
                                            fieldWithPath("[].freeBoardId").description("부모 게시글 식별자")
                                                    .attributes(key("constraints").value("LONG")),
                                            fieldWithPath("[].fileId").description("파일 식별자")
                                                    .attributes(key("constraints").value("LONG")),
                                            fieldWithPath("[].originalName").description("원본 파일 이름")
                                                    .attributes(key("constraints").value("LONG"))
                                    )));
        }
    }

}