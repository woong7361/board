package com.example.notice.service;

import com.example.notice.dto.response.FileResponseDTO;

import java.io.File;
import java.util.List;

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

    /**
     * 자유게시판 게시글에 해당하는 파일들 가져오기
     *
     * @param freeBoardId 게시글 식별자
     * @return 파일 리스트
     */
    List<FileResponseDTO> getFileByFreeBoardId(Long freeBoardId);

    /**
     * 파일 원본 이름 가져오기
     * @param fileId 파일 식별자
     * @return 원본 이름
     */
    String getFileOriginalNameById(Long fileId);
}
