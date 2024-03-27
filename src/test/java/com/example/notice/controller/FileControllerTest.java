package com.example.notice.controller;

import com.example.notice.auth.AuthenticationHolder;
import com.example.notice.auth.principal.MemberPrincipal;
import com.example.notice.dto.common.IdList;
import com.example.notice.entity.Member;
import com.example.notice.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;


@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private ObjectMapper mapper = new ObjectMapper();
    @Nested
    @DisplayName("파일 생성/저장 테스트")
    public class SaveFile {
        private static final String CREATE_FILE_URI = "/api/boards/free/%s/files";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String boardId = "1";
            MockMultipartFile file1 = new MockMultipartFile("files", "imageFile.jpg", "image/png", "data".getBytes());
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.multipart(CREATE_FILE_URI.formatted(boardId))
                            .file(file1)
                            .param("freeBoardId", "1")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId));
        }
    }


    @Nested
    @DisplayName("파일 삭제")
    public class DeleteFile {

        @BeforeEach
        public void createMember() {
            Member member = Member.builder()
                    .memberId(1L)
                    .name("name")
                    .build();

            AuthenticationHolder.setPrincipal(new MemberPrincipal(member));
        }

        private static final String FILE_DELETE_URI = "/api/boards/free/files";
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            IdList ids = new IdList();
            ids.addId(1L);
            ids.addId(2L);
            ids.addId(3L);

            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.delete(FILE_DELETE_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(ids)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    //TODO 파일 다운로드 테스트에서 더미파일 사용?
    @Disabled
    @Nested
    @DisplayName("파일 다운로드 컨트롤러 테스트")
    public class FileDownloadTest {

        private static String FILE_DOWNLOAD_URI = "/api/boards/files/%s/download";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 13L;
            //when
//            Mockito.when(fileService.getPhysicalFile(fileId))
//                    .thenReturn(new File());
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(FILE_DOWNLOAD_URI.formatted(fileId)));

        }
    }

}