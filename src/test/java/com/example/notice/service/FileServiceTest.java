package com.example.notice.service;


import com.example.notice.dto.IdList;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.AuthorizationException;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockAttachmentFileRepository;
import com.example.notice.mock.repository.MockPhysicalFileRepository;
import com.example.notice.mock.service.MockConfigurationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.notice.mock.repository.MockFreeBoardRepository.SAVED_FREE_BOARD;
import static com.example.notice.mock.repository.MockMemberRepository.SAVED_MEMBER;
import static com.example.notice.mock.repository.MockPhysicalFileRepository.IO_ERROR_FILE_NAME;

class FileServiceTest {

    MockPhysicalFileRepository physicalFileRepository = new MockPhysicalFileRepository();
    MockAttachmentFileRepository fileRepository = new MockAttachmentFileRepository();
    MockConfigurationService configurationService = new MockConfigurationService();

    FileService fileService = new FileServiceImpl(
            physicalFileRepository,
            fileRepository,
            configurationService);

    @BeforeEach
    public void resetRepository123() {
        physicalFileRepository.clearRepository();
        fileRepository.clearRepository();
    }

    @Nested
    @DisplayName("파일 저장 서비스 테스트")
    public class FileSaveTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            MockMultipartFile file1 = new MockMultipartFile(
                    "name1",
                    "originalName1.jpg",
                    "image/png",
                    "data".getBytes());
            MockMultipartFile file2 = new MockMultipartFile(
                    "name2",
                    "originalName2.jpg",
                    "image/png",
                    "data".getBytes());
            List<MultipartFile> files = List.of(file1, file2);
            //when
            //then
            fileService.save(files, 1L);
        }

        @DisplayName("지원하지 않는 확장자가 파일에 포함될때")
        @Test
        public void notAllowedExtension() throws Exception{
            //given
            MockMultipartFile file1 = new MockMultipartFile(
                    "name1",
                    "originalName11032.exe123",
                    "exe",
                    "data".getBytes());
            MockMultipartFile file2 = new MockMultipartFile(
                    "name2",
                    "originalName4.jpg",
                    "image/png",
                    "data".getBytes());
            List<MultipartFile> files = List.of(file1, file2);

            //when
            fileService.save(files, 1L);

            //then
            Assertions.assertThat(MemoryDataBase.attachmentFileRepository.size()).isEqualTo(1);
            Assertions.assertThat(MemoryDataBase.physicalFileRepository.size()).isEqualTo(1);
        }

        @DisplayName("I/O 작업에 문제가 생겨 일부 파일 저장에 실패했을때")
        @Test
        public void IOExceptionInFileService() throws Exception{
            //given
            MockMultipartFile file1 = new MockMultipartFile(
                    "name1",
                    IO_ERROR_FILE_NAME,  // exception을 일으키는 fileName
                    "image/png",
                    "data".getBytes());
            MockMultipartFile file2 = new MockMultipartFile(
                    "name2",
                    "originalName5.jpg",
                    "image/png",
                    "data".getBytes());
            List<MultipartFile> files = List.of(file1, file2);

            //when
            fileService.save(files, 1L);

            //then
            Assertions.assertThat(MemoryDataBase.attachmentFileRepository.size()).isEqualTo(1);
            Assertions.assertThat(MemoryDataBase.physicalFileRepository.size()).isEqualTo(1);
        }
    }


    @Nested
    @DisplayName("파일 삭제 서비스 테스트")
    public class FileDeleteTest {

        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            for (String s : MemoryDataBase.physicalFileRepository) {
                System.out.println("s = " + s);
            }

            //given
            long fileId = 1000L;
            String fileName = "ofn.jpg";
            AttachmentFile attachmentFile = AttachmentFile.builder()
                    .fileId(fileId)
                    .path(configurationService.getFilePath())
                    .physicalName(fileName)
                    .build();

            IdList ids = new IdList();
            ids.addId(fileId);

            //when
            MemoryDataBase.attachmentFileRepository
                    .add(attachmentFile);
            physicalFileRepository.save(null, fileName);

            fileService.delete(ids);

            //then
            Assertions.assertThat(MemoryDataBase.attachmentFileRepository.size()).isEqualTo(0);
            Assertions.assertThat(MemoryDataBase.physicalFileRepository.size()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("파일 권한 확인 테스트")
    public class CheckFileAuthorization {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 1000L;
            AttachmentFile attachmentFile = AttachmentFile.builder()
                    .fileId(fileId)
                    .freeBoardId(SAVED_FREE_BOARD.getFreeBoardId())
                    .build();

            IdList ids = new IdList();
            ids.addId(fileId);

            //when
            MemoryDataBase.attachmentFileRepository
                    .add(attachmentFile);

            //then
            fileService.checkFilesAuthorization(ids, SAVED_MEMBER.getMemberId());
        }

        @DisplayName("권한이 없는 사용자가 접근할때")
        @Test
        public void test() throws Exception{
            //given
            long fileId = 1000L;
            AttachmentFile attachmentFile = AttachmentFile.builder()
                    .fileId(fileId)
                    .freeBoardId(SAVED_FREE_BOARD.getFreeBoardId())
                    .build();

            IdList ids = new IdList();
            ids.addId(fileId);

            //when
            MemoryDataBase.attachmentFileRepository
                    .add(attachmentFile);

            //then
            Assertions.assertThatThrownBy(() -> fileService.checkFilesAuthorization(ids, 1L))
                    .isInstanceOf(AuthorizationException.class);
        }
    }
}