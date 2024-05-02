package com.example.notice.service;

import com.example.notice.dto.common.SuccessesAndFails;
import com.example.notice.dto.response.FileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * 파일 서비스
 */
public interface FileService {

    /**
     * 물리적 파일을 반환
     *
     * @param fileId 파일 식별자
     * @return 물리적 파일
     */
    File getPhysicalFile(Long fileId);

    /**
     * 자유게시판 게시글에 해당하는 파일들 조회
     *
     * @param freeBoardId 게시글 식별자
     * @return 파일 리스트
     */
    List<FileResponseDTO> getFileByFreeBoardId(Long freeBoardId);

    /**
     * 파일 원본 이름 조회
     *
     * @param fileId 파일 식별자
     * @return 파일 원본 이름
     */
    String getFileOriginalNameById(Long fileId);

    /**
     * 파일을 저장
     *
     * @param multipartFiles 저장할 파일
     * @param freeBoardId    저장할 FK
     * @return 성공/실패
     */
    SuccessesAndFails<String> saveFiles(List<MultipartFile> multipartFiles, Long freeBoardId);

    /**
     * 파일을 삭제
     *
     * @param fileIds 삭제할 파일 식별자들
     */
    void deleteFileByFileIds(List<Long> fileIds);

    /**
     * 파일 삭제
     *
     * @param fileId 파일 식별자
     */
    void deleteFileByFileId(Long fileId);

    /**
     * 게시글에 달린 파일 삭제
     *
     * @param freeBoardId 게시글 식별자
     */
    void deleteFileByFreeBoardId(Long freeBoardId);
}
