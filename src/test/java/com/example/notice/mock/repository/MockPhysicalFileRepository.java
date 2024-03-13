package com.example.notice.mock.repository;

import com.example.notice.exception.FileSaveCheckedException;
import com.example.notice.files.PhysicalFileRepository;

import java.io.File;

public class MockPhysicalFileRepository implements PhysicalFileRepository {
    @Override
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException {
        return null;
    }

    @Override
    public void delete(String fullPath) {

    }

    @Override
    public File getFile(String path) {
        return null;
    }
}
