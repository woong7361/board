package com.example.notice.controller;

import com.example.notice.constant.ResponseConstant;
import com.example.notice.service.FileService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static com.example.notice.constant.ResponseConstant.FREE_BOARD_ID_PARAM;


@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @Nested
    @DisplayName("파일 생성/저장 테스트")
    public class NestedClass {
        private static final String CREATE_FILE_URI = "/api/boards/free/files";

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String boardId = "1";
            MockMultipartFile file1 = new MockMultipartFile("files", "imageFile.jpg", "image/png", "data".getBytes());
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.multipart(CREATE_FILE_URI)
                            .file(file1)
                            .param("freeBoardId", "1")
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$." + FREE_BOARD_ID_PARAM).value(boardId));
        }

        @DisplayName("게시글 식별자가 null 일때")
        @Test
        public void nullBoardId() throws Exception{
            //given
            MockMultipartFile file1 = new MockMultipartFile("files", "imageFile.jpg", "image/png", "data".getBytes());
            //when
            //then
            mockMvc.perform(MockMvcRequestBuilders.multipart(CREATE_FILE_URI)
                            .file(file1)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect((result -> {
                        Assertions.assertThat(result.getResolvedException())
                                .isInstanceOf(MissingServletRequestParameterException.class);
                    }));
        }
    }



}