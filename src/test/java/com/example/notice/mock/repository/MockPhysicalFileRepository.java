package com.example.notice.mock.repository;

import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.mock.service.MockConfigurationService;

import java.io.File;
import java.util.ArrayList;

import static com.example.notice.mock.database.MemoryDataBase.PHYSICAL_FILE_STORAGE;

public class MockPhysicalFileRepository implements PhysicalFileRepository {

    public static final String IO_ERROR_FILE_NAME = "error.jpg";

    //TODO static과 local의 저장공간의 차이성?
    // 둘다 저장을 하면 같이 저장되지만
    // 삭제를 하면 하나만 삭제되어 문제가 일어난다.
//    public List<String> physicalFileRepository = MemoryDataBase.physicalFileRepository;

    MockConfigurationService configurationService = new MockConfigurationService();

    /**
     * @implNote originalFileName이 error 일때 checkedException 발생
     */
    @Override
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException {
        if (IO_ERROR_FILE_NAME.equals(originalFileName)) {
            throw new FileSaveCheckedException("file save error in tets", originalFileName);
        }

        String savedFilePath = configurationService.getFilePath() + "/" + originalFileName;
        PHYSICAL_FILE_STORAGE.add(savedFilePath);

        return savedFilePath;
    }

    @Override
    public void delete(String fullPath) {
        PHYSICAL_FILE_STORAGE.remove(fullPath);
    }

    @Override
    public File getFile(String path) {
        return null;
    }

    public void clearRepository() {
        PHYSICAL_FILE_STORAGE = new ArrayList<>();
//        physicalFileRepository = new ArrayList<>();
    }
}
