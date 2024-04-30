package com.example.notice.service;


import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockAttachmentFileRepository;
import com.example.notice.mock.repository.MockPhysicalFileRepository;
import com.example.notice.repository.AttachmentFileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;


class FileServiceTest {

    MockPhysicalFileRepository physicalFileRepository = new MockPhysicalFileRepository();
    MockAttachmentFileRepository fileRepository = new MockAttachmentFileRepository();

    FileService fileService = new FileServiceImpl(
            physicalFileRepository,
            fileRepository);

    PhysicalFileRepository mockitoPhysicalFileRepository = Mockito.mock(PhysicalFileRepository.class);
    AttachmentFileRepository mockitoFIleRepository = Mockito.mock(AttachmentFileRepository.class);
    FileService mockitoFileService = new FileServiceImpl(
            mockitoPhysicalFileRepository,
            mockitoFIleRepository);

    @BeforeEach
    public void resetRepository123() {
        MemoryDataBase.initRepository();
    }

    @Nested
    @DisplayName("물리적 파일 가져오기 테스트")
    public class GetPhysicalFile {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 123L;

            //when
            Mockito.when(mockitoFIleRepository.findByFileId(fileId))
                    .thenReturn(Optional.of(AttachmentFile.builder().build()));

            //then
            mockitoFileService.getPhysicalFile(fileId);
        }

        @DisplayName("파일이 DB에 존재하지 않음")
        @Test
        public void fileNotExist() throws Exception{
            //given
            long fileId = 123L;

            //when
            Mockito.when(mockitoFIleRepository.findByFileId(fileId))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> mockitoFileService.getPhysicalFile(fileId))
                    .isInstanceOf(EntityNotExistException.class);
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

    @DisplayName("파일 원본 이름 가져오기")
    @Test
    public void getOriginalFileNameById() throws Exception{
        //given
        Long fileId = 123L;

        //when
        //then
        mockitoFileService.getFileOriginalNameById(fileId);
    }

    @DisplayName("")
    @Test
    public void test() throws Exception{
        //given

        //when

        //then
    }
}