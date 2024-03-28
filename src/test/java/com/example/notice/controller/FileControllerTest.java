package com.example.notice.controller;

import com.example.notice.mock.config.NoFilterMvcTest;
import com.example.notice.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



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

}