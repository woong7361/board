package com.example.notice.files;


import com.example.notice.config.ConfigurationService;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.exception.PhysicalFileNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

class DiskFileRepositoryTest {
    ConfigurationService configurationService = Mockito.mock(ConfigurationService.class);
    PhysicalFileRepository fileRepository = new DiskFileRepository(configurationService);

    @Nested
    @DisplayName("실파일 저장")
    public class PhysicalFileSave {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            String path = "src/test/resources/save-test";
            byte[] saveData = "content".getBytes();
            String originalFileName = "fileSaveTest.txt";

            //when
            Mockito.when(configurationService.getFilePath())
                    .thenReturn(path);

            String fullPath = fileRepository.save(saveData, originalFileName);
            File file = new File(fullPath);
            file.deleteOnExit();

            //then
            Assertions.assertThat(file.exists()).isTrue();
        }

        @DisplayName("파일 저장중 IO EXCEPTION이 일어나면")
        @Test
        public void test() throws Exception{
            //given
            String path = "src/test/resources/save-test/1/2/3";
            byte[] saveData = "content".getBytes();
            String originalFileName = "fileSaveTest.txt";

            //when
            Mockito.when(configurationService.getFilePath())
                    .thenReturn(path);

            //then
            Assertions.assertThatThrownBy(() -> fileRepository.save(saveData, originalFileName))
                    .isInstanceOf(FileSaveCheckedException.class);
        }
    }

    @Nested
    @DisplayName("실파일 삭제")
    public class DeletePhysicalFile {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 543L;

            //when
            //then
            fileRepository.delete(fileId);
        }
    }

    @Nested
    @DisplayName("실파일 조회")
    public class GetPhysicalFile {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            String path = "src/test/resources/test.txt";

            //when
            File file = fileRepository.getFile(path);

            //then
            Assertions.assertThat(file.exists()).isTrue();
        }

        @DisplayName("해당 경로에 파일이 존재하지 않을때")
        @Test
        public void notExistFIle() throws Exception{
            //given
            String path = "src/test/resources/invalid.txt";

            //when
            //then
            Assertions.assertThatThrownBy(() -> fileRepository.getFile(path))
                    .isInstanceOf(PhysicalFileNotFoundException.class);
        }
    }

}