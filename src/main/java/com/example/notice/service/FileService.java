package com.example.notice.service;

import com.example.notice.dto.SuccessesAndFails;
import com.example.notice.entity.AttachmentFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * file 서비스
 */
public interface FileService {

    /**
     * 파일 저장
     *
     * @param files   저장할 파일들
     * @param boardId
     * @return 파일들의 성공/실패
     */
    SuccessesAndFails<AttachmentFile> save(List<MultipartFile> files, Long boardId);
}
