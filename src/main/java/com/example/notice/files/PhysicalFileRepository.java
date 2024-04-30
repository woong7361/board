package com.example.notice.files;

import com.example.notice.exception.FileSaveCheckedException;

import java.io.File;

/**
 * 물리적 파일 저장소
 */
public interface PhysicalFileRepository {

    /**
     * 파일 저장
     *
     * @param bytes 파일 bytes
     * @param originalFileName 파일 원본 이름
     * @return 저장된 파일 경로
     */
    String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException;

    /**
     * 파일 삭제
     *
     * @param fileId 파일 식별자
     */
    void delete(Long fileId);

    /**
     * 물리적 파일을 조회
     *
     * @param path 파일의 이름을 포함한 경로
     * @return 물리적 파일
     */
    File getFile(String path);
}
