package com.example.notice.controller;

import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;


@NoFilterMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    private ObjectMapper mapper = new ObjectMapper();


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


    @Nested
    @DisplayName("파일 조회 컨트롤러 테스트")
    public class getFileByFreeBoardIdControllerTest {

        private static final String FREE_BOARD_FILE_URI = "/api/boards/free/%s/files";
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

            //then
            mockMvc.perform(MockMvcRequestBuilders.get(FREE_BOARD_FILE_URI.formatted(freeBoardId)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].freeBoardId").value(file1.getFreeBoardId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].fileId").value(file1.getFileId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].originalName").value(file1.getOriginalName()));
        }
    }

}