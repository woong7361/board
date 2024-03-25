package com.example.notice.mock.repository;

import com.example.notice.entity.AttachmentFile;
import com.example.notice.entity.FreeBoard;
import com.example.notice.mock.database.MemoryDataBase;
import com.example.notice.repository.AttachmentFileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.notice.mock.database.MemoryDataBase.ATTACHMENT_FILE_STORAGE;
import static com.example.notice.mock.database.MemoryDataBase.FREE_BOARD_STORAGE;

public class MockAttachmentFileRepository implements AttachmentFileRepository {

    @Override
    public void saveWithFreeBoardId(AttachmentFile file, Long boardId) {
        AttachmentFile addFile = attachmentFileBuilderMapper(file)
                .freeBoardId(boardId)
                .build();

        ATTACHMENT_FILE_STORAGE.add(addFile);
    }

    @Override
    public Optional<AttachmentFile> findByFileId(Long fileId) {
        return ATTACHMENT_FILE_STORAGE.stream()
                .filter((af) -> af.getFileId().equals(fileId))
                .findFirst();
    }

    @Override
    public void deleteByFileId(Long fileId) {
        ATTACHMENT_FILE_STORAGE
                .removeIf((af) -> af.getFileId().equals(fileId));
    }

    @Override
    public Optional<AttachmentFile> findByFileIdAndMemberId(Long fileId, Long memberId) {
        return ATTACHMENT_FILE_STORAGE.stream()
                .filter((af) -> af.getFileId().equals(fileId))
                .filter((af) -> FREE_BOARD_STORAGE.stream()
                            .filter((fb) -> af.getFreeBoardId().equals(fb.getFreeBoardId()))
                            .anyMatch((fb) -> fb.getMemberId().equals(memberId)))
                .findFirst();
    }

    public static AttachmentFile.AttachmentFileBuilder attachmentFileBuilderMapper(AttachmentFile file) {
        return AttachmentFile.builder()
                .fileId(file.getFileId())
                .physicalName(file.getPhysicalName())
                .originalName(file.getOriginalName())
                .extension(file.getExtension())
                .path(file.getPath());
    }
}
