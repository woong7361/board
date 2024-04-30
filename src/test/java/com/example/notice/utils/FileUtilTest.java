package com.example.notice.utils;

import com.example.notice.config.ConfigurationService;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.FileSaveCheckedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;


class FileUtilTest {

    ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
    FileUtil fileUtil = new FileUtil(configurationService);


    @Nested
    @DisplayName("허용되는 확장자 체크")
    public class NestedClass {
        @DisplayName("허용되는 확장자가 들어올때")
        @Test
        public void allowExtension() throws Exception {
            //given
            List<String> allowExtensions = List.of("jpg", "zip");

            MockMultipartFile jpgFile = new MockMultipartFile("file1",
                    "file.jpg",
                    "jpg",
                    "jpg".getBytes());
            MockMultipartFile zipFile = new MockMultipartFile("file2",
                    "file.zip",
                    "zip",
                    "zip".getBytes());

            //when
            Mockito.when(configurationService.getAllowExtension())
                    .thenReturn(allowExtensions);

            //then
            fileUtil.checkAllowFileExtension(jpgFile);
            fileUtil.checkAllowFileExtension(zipFile);
        }

        @DisplayName("허용되지 않은 확장자가 들어올때")
        @Test
        public void notAllowedExtension() throws Exception{
            //given
            List<String> allowExtensions = List.of("jpg", "zip");

            MockMultipartFile pngFile = new MockMultipartFile("file1",
                    "file.png",
                    "png",
                    "png".getBytes());

            //when
            Mockito.when(configurationService.getAllowExtension())
                    .thenReturn(allowExtensions);

            //then
            Assertions.assertThatThrownBy(() -> fileUtil.checkAllowFileExtension(pngFile))
                    .isInstanceOf(FileSaveCheckedException.class);
        }
    }

    @Nested
    @DisplayName("bytes 추출")
    public class GetBytes {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            byte[] inputBytes = "png".getBytes();

            MockMultipartFile file = new MockMultipartFile("file1", inputBytes);

            //when
            byte[] bytes = fileUtil.getBytes(file);

            //then
            Assertions.assertThat(bytes).isEqualTo(inputBytes);
        }

    }

    @DisplayName("확장자 추출")
    @Test
    public void getExtension() throws Exception{
        //given
        String extension = "jpg";
        String fileName = "fdsaf153." + extension;

        //when
        String result = fileUtil.getExtension(fileName);

        //then
        Assertions.assertThat(result).isEqualTo(result);
    }

    @DisplayName("파일 이름 추출")
    @Test
    public void getFileName() throws Exception{
        //given
        String fileName = "6412134.png";
        String path = "/a/b/c/d/" + fileName;

        //when
        String result = fileUtil.getFileNameFromPath(path);

        //then
        Assertions.assertThat(result).isEqualTo(fileName);
    }

    @DisplayName("파일 경로 구하기")
    @Test
    public void getFileSubPath() throws Exception{
        //given
        String fileName = "6412134.png";
        String subPath = "/a/b/c/d";

        //when
        String result = fileUtil.getFileSubPath(subPath + "/" + fileName);

        //then
        Assertions.assertThat(result).isEqualTo(subPath);
    }

    @DisplayName("파일 전체 경로 구하기")
    @Test
    public void getFileFullPath() throws Exception{
        //given
        String diskPath = "/a/b/c";

        String fileName = "fileName";

        AttachmentFile attachmentFile = AttachmentFile.builder()
                .path(diskPath)
                .physicalName(fileName)
                .build();

        //when
        String fileFullPath = fileUtil.getFileFullPath(attachmentFile);

        //then
        Assertions.assertThat(fileFullPath).isEqualTo(diskPath + "/" + fileName);
    }
}