package com.example.notice.mock.repository;

import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.mock.service.MockConfigurationService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.notice.mock.database.MemoryDataBase.PHYSICAL_FILE_STORAGE;

public class MockPhysicalFileRepository implements PhysicalFileRepository {

    public static final String IO_ERROR_FILE_NAME = "error.jpg";

    MockConfigurationService configurationService = new MockConfigurationService();

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
    public void delete(Long fileId) {

    }

    @Override
    public File getFile(String path) {
        return null;
    }

}
