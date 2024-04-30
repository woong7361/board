package com.example.notice.repository;

import com.example.notice.dto.response.FileResponseDTO;
import com.example.notice.entity.AttachmentFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * DB 파일 repository
 */
@Mapper
public interface AttachmentFileRepository {

    /**
     * 첨부 파일 저장
     *
     * @param file    첨부 파일
     * @param boardId 부모 게시글 식별자
     */
    public void saveWithFreeBoardId(@Param("file") AttachmentFile file, @Param("freeBoardId") Long boardId);

    /**
     * 파일 조회
     *
     * @param fileId 파일 식별자
     * @return 파일
     */
    Optional<AttachmentFile> findByFileId(@Param("fileId") Long fileId);

    /**
     * 파일 삭제
     *
     * @param fileId 파일 식별자
     */
    void deleteByFileId(@Param("fileId") Long fileId);

    /**
     * 게시글에 연결된 파일 조회
     *
     * @param freeBoardId 자유게시판 식별자
     * @return 파일 리스트
     */
    List<FileResponseDTO> findByFreeBoardId(@Param("freeBoardId") Long freeBoardId);

    /**
     * 파일 원본 이름 조회
     *
     * @param fileId 파일 식별자
     * @return 파일 원본 이름
     */
    String findOriginalNameById(@Param("fileId") Long fileId);
}
