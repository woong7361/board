package com.example.notice;

import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.repository.MockAttachmentFileRepository;
import com.example.notice.mock.repository.MockPhysicalFileRepository;
import com.example.notice.mock.service.MockConfigurationService;
import com.example.notice.service.FileService;
import com.example.notice.service.FileServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//TODO 테스트 확인
public class CautionTest {
    MockPhysicalFileRepository physicalFileRepository = new MockPhysicalFileRepository();
    MockAttachmentFileRepository fileRepository = new MockAttachmentFileRepository();
    MockConfigurationService configurationService = new MockConfigurationService();

    FileService fileService = new FileServiceImpl(
            physicalFileRepository,
            fileRepository,
            configurationService);

    @DisplayName("정상 처리")
    @Test
    public void success() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile(
                "name1",
                "originalName4.jpg",
                "image/png",
                "data".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "name2",
                "originalName3.jpg",
                "image/png",
                "data".getBytes());
        List<MultipartFile> files = List.of(file1, file2);
        //when
        //then
        fileService.save(files, 1L);

        physicalFileRepository.clearRepository();
//        MemoryDataBase.PHYSICAL_FILE_STORAGE = new ArrayList<>();

        for (String s : MemoryDataBase.PHYSICAL_FILE_STORAGE) {
            System.out.println("s = " + s);
        }

//        for (String s : physicalFileRepository.PHYSICAL_FILE_STORAGE) {
//            System.out.println("sss = " + s);
//        }

        //결과 - 한쪽만 지우면 한쪽만 한쪽만 나온다.

    }
}
