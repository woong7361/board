package com.example.notice.service;


import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockAttachmentFileRepository;
import com.example.notice.mock.repository.MockPhysicalFileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;


class FileServiceTest {

    MockPhysicalFileRepository physicalFileRepository = new MockPhysicalFileRepository();
    MockAttachmentFileRepository fileRepository = new MockAttachmentFileRepository();

    FileService fileService = new FileServiceImpl(
            physicalFileRepository,
            fileRepository);

    @BeforeEach
    public void resetRepository123() {
        MemoryDataBase.initRepository();
    }

//    @Nested
//    @DisplayName("파일 저장 서비스 테스트")
//    public class FileSaveTest {
//
//        @DisplayName("정상 처리")
//        @Test
//        public void success() throws Exception {
//            //given
//            MockMultipartFile file1 = new MockMultipartFile(
//                    "name1",
//                    "originalName1.jpg",
//                    "image/png",
//                    "data".getBytes());
//            MockMultipartFile file2 = new MockMultipartFile(
//                    "name2",
//                    "originalName2.jpg",
//                    "image/png",
//                    "data".getBytes());
//            List<MultipartFile> files = List.of(file1, file2);
//            //when
//            //then
//            fileService.save(files, 1L);
//        }
//
//        @DisplayName("지원하지 않는 확장자가 파일에 포함될때")
//        @Test
//        public void notAllowedExtension() throws Exception{
//            //given
//            MockMultipartFile file1 = new MockMultipartFile(
//                    "name1",
//                    "originalName11032.exe123",
//                    "exe",
//                    "data".getBytes());
//            MockMultipartFile file2 = new MockMultipartFile(
//                    "name2",
//                    "originalName4.jpg",
//                    "image/png",
//                    "data".getBytes());
//            List<MultipartFile> files = List.of(file1, file2);
//
//            //when
//            fileService.save(files, 1L);
//
//            //then
//            Assertions.assertThat(MemoryDataBase.ATTACHMENT_FILE_STORAGE.size()).isEqualTo(1);
//            Assertions.assertThat(MemoryDataBase.PHYSICAL_FILE_STORAGE.size()).isEqualTo(1);
//        }
//
//        @DisplayName("I/O 작업에 문제가 생겨 일부 파일 저장에 실패했을때")
//        @Test
//        public void IOExceptionInFileService() throws Exception{
//            //given
//            MockMultipartFile file1 = new MockMultipartFile(
//                    "name1",
//                    IO_ERROR_FILE_NAME,  // exception을 일으키는 fileName
//                    "image/png",
//                    "data".getBytes());
//            MockMultipartFile file2 = new MockMultipartFile(
//                    "name2",
//                    "originalName5.jpg",
//                    "image/png",
//                    "data".getBytes());
//            List<MultipartFile> files = List.of(file1, file2);
//
//            //when
//            fileService.save(files, 1L);
//
//            //then
//            Assertions.assertThat(MemoryDataBase.ATTACHMENT_FILE_STORAGE.size()).isEqualTo(1);
//            Assertions.assertThat(MemoryDataBase.PHYSICAL_FILE_STORAGE.size()).isEqualTo(1);
//        }
//    }
//
//
//    @Nested
//    @DisplayName("파일 삭제 서비스 테스트")
//    public class FileDeleteTest {
//
//        @DisplayName("정상 처리")
//        @Test
//        public void success() throws Exception {
//            for (String s : MemoryDataBase.PHYSICAL_FILE_STORAGE) {
//                System.out.println("s = " + s);
//            }
//
//            //given
//            long fileId = 1000L;
//            String fileName = "ofn.jpg";
//            AttachmentFile attachmentFile = AttachmentFile.builder()
//                    .fileId(fileId)
//                    .path(configurationService.getFilePath())
//                    .physicalName(fileName)
//                    .build();
//
//            IdList ids = new IdList();
//            ids.addId(fileId);
//
//            //when
//            MemoryDataBase.ATTACHMENT_FILE_STORAGE
//                    .add(attachmentFile);
//            physicalFileRepository.save(null, fileName);
//
//            fileService.delete(ids);
//
//            //then
//            Assertions.assertThat(MemoryDataBase.ATTACHMENT_FILE_STORAGE.size()).isEqualTo(0);
//            Assertions.assertThat(MemoryDataBase.PHYSICAL_FILE_STORAGE.size()).isEqualTo(0);
//        }
//    }
//
//    @Nested
//    @DisplayName("파일 권한 확인 테스트")
//    public class CheckFileAuthorization {
//        @DisplayName("정상 처리")
//        @Test
//        public void success() throws Exception {
//            //given
//            long fileId = 1000L;
//            AttachmentFile attachmentFile = AttachmentFile.builder()
//                    .fileId(fileId)
//                    .freeBoardId(SAVED_FREE_BOARD.getFreeBoardId())
//                    .build();
//
//            IdList ids = new IdList();
//            ids.addId(fileId);
//
//            //when
//            MemoryDataBase.ATTACHMENT_FILE_STORAGE
//                    .add(attachmentFile);
//
//            //then
//            fileService.checkFilesAuthorization(ids, SAVED_MEMBER.getMemberId());
//        }
//
//        @DisplayName("권한이 없는 사용자가 접근할때")
//        @Test
//        public void test() throws Exception{
//            //given
//            long fileId = 1000L;
//            AttachmentFile attachmentFile = AttachmentFile.builder()
//                    .fileId(fileId)
//                    .freeBoardId(SAVED_FREE_BOARD.getFreeBoardId())
//                    .build();
//
//            IdList ids = new IdList();
//            ids.addId(fileId);
//
//            //when
//            MemoryDataBase.ATTACHMENT_FILE_STORAGE
//                    .add(attachmentFile);
//
//            //then
//            Assertions.assertThatThrownBy(() -> fileService.checkFilesAuthorization(ids, 1L))
//                    .isInstanceOf(AuthorizationException.class);
//        }
//    }

    @Nested
    @DisplayName("물리적 파일 가져오기 테스트")
    public class GetPhysicalFile {
        @Disabled
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given

            //when

            //then
        }

        @DisplayName("파일이 DB에 존재하지 않음")
        @Test
        public void fileNotExist() throws Exception{
            //given
            long notExistFileId = 1531352L;

            //when
            Assertions.assertThatThrownBy(() -> fileService.getPhysicalFile(notExistFileId))
                    .isInstanceOf(EntityNotExistException.class);
            //then
        }

        //TODO 파일을 다루는 부분에서 실제 파일을 다루는 부분을 직접적으로 쓴다?
        // 이상하다. 얘도 mocking? 하기에도 좀 애매하다.
        @Disabled
        @DisplayName("파일의 경로에 물리적 파일이 존재하지 않음")
        @Test
        public void physicalFileNotExist() throws Exception{
            //given

            //when

            //then
        }
    }

    @Nested
    @DisplayName("자유게시판 게시글에 속한 파일 가져오기")
    public class GetFileByFreeBoardId {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            Long freeBoardId = 1454314L;

            AttachmentFile file1 = AttachmentFile.builder()
                    .freeBoardId(freeBoardId)
                    .fileId(44561L)
                    .originalName("name23")
                    .build();

            AttachmentFile file2 = AttachmentFile.builder()
                    .freeBoardId(freeBoardId)
                    .build();

            fileRepository.saveWithFreeBoardId(file1, freeBoardId);
            fileRepository.saveWithFreeBoardId(file2, freeBoardId);

            //when
            List<FileResponseDTO> results = fileService.getFileByFreeBoardId(freeBoardId);
            //then

            Assertions.assertThat(results.size()).isEqualTo(2);
            Assertions.assertThat(results.get(0)).usingRecursiveComparison().isEqualTo(file1);
        }
    }
}