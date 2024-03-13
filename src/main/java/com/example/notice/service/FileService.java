package com.example.notice.service;

import com.example.notice.dto.IdList;
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
     * @param boardId 파일의 부모 게시글
     * @return 파일들의 성공/실패
     */
    SuccessesAndFails<AttachmentFile> save(List<MultipartFile> files, Long boardId);

    /**
     * 파일 삭제
     * @param fileIds 삭제할 파일들
     */
    void delete(IdList fileIds);

    /**
     * 파일들 권한 확인
     * @param fileIds 권한 확인하는 파일 식별자들
     * @param memberId 삭제를 요청하는 사용자
     */
    void checkFilesAuthorization(IdList fileIds, Long memberId);
}
