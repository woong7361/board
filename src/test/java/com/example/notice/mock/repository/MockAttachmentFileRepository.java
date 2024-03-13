package com.example.notice.mock.repository;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.repository.AttachmentFileRepository;

import java.util.Optional;

public class MockAttachmentFileRepository implements AttachmentFileRepository {
    @Override
    public void saveWithFreeBoardId(AttachmentFile file, Long boardId) {

    }

    @Override
    public Optional<AttachmentFile> findByFileId(Long fileId) {
        return Optional.empty();
    }

    @Override
    public void deleteByFileId(Long fileId) {

    }

    @Override
    public Optional<AttachmentFile> findByFileIdAndMemberId(Long fileId, Long memberId) {
        return Optional.empty();
    }
}
