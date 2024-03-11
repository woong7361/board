package com.example.notice.files;

import com.example.notice.exception.FileSaveCheckedException;

import java.io.File;

/**
 * 물리적 파일 저장소
 */
public interface PhysicalFileRepository {

    /**
     * 파일 저장
     * @param bytes 파일 bytes
     * @param originalFileName 파일 원본 이름
     * @return 저장된 파일 경로
     */
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException;

    /**
     * 파일 삭제
     * @param fullPath 파일 경로
     */
    public void delete(String fullPath);

    File getFile(String path);
}
