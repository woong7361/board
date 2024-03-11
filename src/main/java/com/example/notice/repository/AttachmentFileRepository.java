package com.example.notice.repository;

import com.example.notice.entity.AttachmentFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 첨부 파일 repository
 */
@Mapper
public interface AttachmentFileRepository {

    /**
     * 첨부 파일 저장
     *
     * @param file    첨부 파일
     * @param boardId FK freeBoardId
     */
    public void saveWithFreeBoardId(@Param("file") AttachmentFile file, @Param("freeBoardId") Long boardId);
}
