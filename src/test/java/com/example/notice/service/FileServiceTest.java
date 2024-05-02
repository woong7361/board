package com.example.notice.service;


import com.example.notice.dto.common.SuccessesAndFails;
import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import com.example.notice.exception.EntityNotExistException;
import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.repository.AttachmentFileRepository;
import com.example.notice.utils.FileUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


class FileServiceTest {
    PhysicalFileRepository physicalFileRepository = Mockito.mock(PhysicalFileRepository.class);
    AttachmentFileRepository fIleRepository = Mockito.mock(AttachmentFileRepository.class);
    FileUtil fileUtil = Mockito.mock(FileUtil.class);
    FileService fileService = new FileServiceImpl(
            physicalFileRepository,
            fIleRepository,
            fileUtil);

    @Nested
    @DisplayName("물리적 파일 가져오기 테스트")
    public class GetPhysicalFile {
        @DisplayName("정상처리")
        @Test
        public void success() throws Exception {
            //given
            long fileId = 123L;

            //when
            Mockito.when(fIleRepository.findByFileId(fileId))
                    .thenReturn(Optional.of(AttachmentFile.builder().build()));

            //then
            fileService.getPhysicalFile(fileId);
        }

        @DisplayName("파일이 DB에 존재하지 않음")
        @Test
        public void fileNotExist() throws Exception{
            //given
            long fileId = 123L;

            //when
            Mockito.when(fIleRepository.findByFileId(fileId))
                    .thenReturn(Optional.empty());

            //then
            Assertions.assertThatThrownBy(() -> fileService.getPhysicalFile(fileId))
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

            List<FileResponseDTO> findFiles = List.of(
                    new FileResponseDTO(file1.getFileId(), file1.getFreeBoardId(), file1.getOriginalName()),
                    new FileResponseDTO(file2.getFileId(), file2.getFreeBoardId(), file2.getOriginalName())
            );

            //when
            Mockito.when(fIleRepository.findByFreeBoardId(freeBoardId))
                    .thenReturn(findFiles);

            List<FileResponseDTO> results = fileService.getFileByFreeBoardId(freeBoardId);

            //then
            Assertions.assertThat(results.size()).isEqualTo(findFiles.size());
            Assertions.assertThat(results).usingRecursiveComparison().isEqualTo(findFiles);
        }
    }

    @DisplayName("파일 원본 이름 가져오기")
    @Test
    public void getOriginalFileNameById() throws Exception{
        //given
        Long fileId = 123L;

        //when
        //then
        fileService.getFileOriginalNameById(fileId);
    }


    @Nested
    @DisplayName("파일 저장")
    public class FileSave {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            byte[] bytes = "fdava".getBytes();
            MockMultipartFile file1 = new MockMultipartFile("file","fileName1", "jpg", bytes);
            MockMultipartFile file2 = new MockMultipartFile("file","fileName2", "jpg", bytes);
            Long freeBoardId = 6857456341L;

            //when
            Mockito.when(fileUtil.getBytes(any()))
                    .thenReturn(bytes);
            Mockito.when(physicalFileRepository.save(any(), any()))
                    .thenReturn("path");

            SuccessesAndFails<String> result = fileService.saveFiles(List.of(file1, file2), freeBoardId);

            //then
            Assertions.assertThat(result.getSuccesses())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(file1.getOriginalFilename(), file2.getOriginalFilename()));
        }

        @DisplayName("실패할때")
        @Test
        public void fail() throws Exception{
            //given
            byte[] bytes = "fdava".getBytes();
            MockMultipartFile file1 = new MockMultipartFile("file","fileName1", "jpg", bytes);
            MockMultipartFile file2 = new MockMultipartFile("file","fileName2", "jpg", bytes);
            Long freeBoardId = 6857456341L;

            //when
            Mockito.when(fileUtil.getBytes(any()))
                    .thenReturn(bytes);
            Mockito.when(physicalFileRepository.save(any(), any()))
                    .thenReturn("path");
            Mockito.doThrow(new FileSaveCheckedException("message")).when(fileUtil)
                    .checkAllowFileExtension(file1);

            SuccessesAndFails<String> result = fileService.saveFiles(List.of(file1, file2), freeBoardId);

            //then
            Assertions.assertThat(result.getSuccesses())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(file2.getOriginalFilename()));
            Assertions.assertThat(result.getFails())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(file1.getOriginalFilename()));
        }
    }


    @Nested
    @DisplayName("게시글 식별자로 파일 삭제")
    public class DeleteFileByFileIds {
        @DisplayName("정상 처리")
        @Test
        public void success() throws Exception {
            //given
            long freeBoardId = 564534L;
            List<FileResponseDTO> fileIds = List.of(
                    FileResponseDTO.builder().fileId(1L).build(),
                    FileResponseDTO.builder().fileId(2L).build(),
                    FileResponseDTO.builder().fileId(3L).build());

            //when
            Mockito.when(fIleRepository.findByFreeBoardId(freeBoardId))
                    .thenReturn(fileIds);

            Mockito.when(fIleRepository.findByFileId(any()))
                    .thenReturn(Optional.of(AttachmentFile.builder().build()));

            fileService.deleteFileByFreeBoardId(freeBoardId);
            //then
        }
    }
}