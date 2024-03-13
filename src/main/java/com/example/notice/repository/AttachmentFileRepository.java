package com.example.notice.repository;

import com.example.notice.entity.AttachmentFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

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

    /**
     * 파일 식별자에 해당하는 파일 가져오기
     * @param fileId 파일 식별자
     * @return 파일 엔티티
     */
    Optional<AttachmentFile> findByFileId(@Param("fileId") Long fileId);

    /**
     * 파일 식별자에 해당하는 파일 삭제
     * @param fileId 파일 식별자
     */
    void deleteByFileId(@Param("fileId") Long fileId);

    /**
     * 파일 식별자에 해당하는 파일이 요청자가 작성했다면 파일을 가져온다.
     * @param fileId 파일 식별자
     * @param memberId 파일 요청자의 식별자
     */
    Optional<AttachmentFile> findByFileIdAndMemberId(@Param("fileId") Long fileId, @Param("memberId") Long memberId);
}
