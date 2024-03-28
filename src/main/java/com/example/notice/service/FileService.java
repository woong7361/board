package com.example.notice.service;

import java.io.File;

/**
 * file 서비스
 */
public interface FileService {

    /**
     * 물리적 파일을 반환하는 서비스
     * @param fileId 파일 식별자
     * @return 물리적 파일
     */
    File getPhysicalFile(Long fileId);
}
